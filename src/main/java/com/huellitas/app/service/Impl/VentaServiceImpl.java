package com.huellitas.app.service.Impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huellitas.app.entity.DetalleVenta;
import com.huellitas.app.entity.DetalleVentaId;
import com.huellitas.app.entity.Producto;
import com.huellitas.app.entity.ProductoParaVender;
import com.huellitas.app.entity.Venta;
import com.huellitas.app.entity.Mascota;
import com.huellitas.app.entity.Cliente;
import com.huellitas.app.entity.CajeraVenta;
import com.huellitas.app.repository.VentaRepository;
import com.huellitas.app.service.DetalleVentaService;
import com.huellitas.app.service.ProductoService;
import com.huellitas.app.service.VentaService;
import jakarta.persistence.EntityManager;

@Service
public class VentaServiceImpl implements VentaService {
    
    private final VentaRepository ventaRepository;
    private final ProductoService productoService;
    private final DetalleVentaService detalleVentaService;
    private final EntityManager entityManager;
    
    private static final BigDecimal IGV_PORCENTAJE = new BigDecimal("0.18");
    
    public VentaServiceImpl(VentaRepository ventaRepository, ProductoService productoService,
            DetalleVentaService detalleVentaService, EntityManager entityManager) {
        this.ventaRepository = ventaRepository;
        this.productoService = productoService;
        this.detalleVentaService = detalleVentaService;
        this.entityManager = entityManager;
    }
    
    @Override
    @Transactional
    public Venta guardarVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarTodosVentas() {
        return ventaRepository.listarVentasNativo();
    }

    @Override
    @Transactional
    public Venta registrarVenta(Venta venta, List<ProductoParaVender> carrito) {
        if (carrito == null || carrito.isEmpty()) {
            throw new RuntimeException("El carrito de compras se encuentra vacío.");
        }

        // 1. Marca de tiempo
        venta.setFechaVenta(new Date());
        
        // 2. Valores iniciales requeridos por la BD
        venta.setSubtotal(BigDecimal.ZERO);
        venta.setIgv(BigDecimal.ZERO);
        venta.setTotal(BigDecimal.ZERO);
        venta.setGanancia(BigDecimal.ZERO);
        
        // 3. Vincular referencias de entidades relacionales
        if (venta.getIdCliente() != null) {
            venta.setCliente(entityManager.getReference(Cliente.class, venta.getIdCliente()));
        }
        
        if (venta.getCodCajera() != null) {
            venta.setCajeraVenta(entityManager.getReference(CajeraVenta.class, venta.getCodCajera()));
        }
        
        if (venta.getIdMascota() != null) {
            venta.setMascota(entityManager.getReference(Mascota.class, venta.getIdMascota()));
        } else {
            venta.setMascota(entityManager.getReference(Mascota.class, 1));
        }
        
        // 4. Guardar cabecera para generar el 'nroventa' autogenerado
        Venta v = ventaRepository.save(venta);
        
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal ganancia = BigDecimal.ZERO;

        // 5. Procesar elementos del carrito
        for (ProductoParaVender item : carrito) {
            Producto p = productoService.buscarPorId(item.getIdProd());
            if (p == null) {
                throw new RuntimeException("El producto con ID " + item.getIdProd() + " no existe.");
            }
            
            if (item.getCantidad() > p.getStock()) {
                throw new RuntimeException("Stock insuficiente para: " + p.getDescripcion());
            }
            
            // Actualizar stock
            p.restarExistencia(item.getCantidad());
            productoService.guardar(p);

            // Importes unitarios acumulados
            BigDecimal importeVenta = item.getPrecioVenta().multiply(BigDecimal.valueOf(item.getCantidad()));
            BigDecimal importeCompra = item.getPrecioCompra().multiply(BigDecimal.valueOf(item.getCantidad()));

            // 6. ID Compuesto pasando (Venta v, Producto p)
            DetalleVentaId idDetalle = new DetalleVentaId(v, p);
            
            // 7. Instanciar DetalleVenta mapeado a tus atributos exactos
            DetalleVenta dv = new DetalleVenta(
                idDetalle, 
                item.getCantidad(), 
                item.getPrecioVenta(), 
                importeVenta, 
                item.getPrecioCompra()
            );
            
            detalleVentaService.guardar(dv);
            
            subtotal = subtotal.add(importeVenta);
            ganancia = ganancia.add(importeVenta.subtract(importeCompra));
        }

        // 8. Totales finales de la Venta
        BigDecimal igv = subtotal.multiply(IGV_PORCENTAJE);
        BigDecimal total = subtotal.add(igv);
        
        v.setSubtotal(subtotal);
        v.setIgv(igv);
        v.setTotal(total);
        v.setGanancia(ganancia);

        return ventaRepository.save(v);
    }
}