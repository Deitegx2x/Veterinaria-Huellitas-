package com.huellitas.app.service.Impl;

import com.huellitas.app.entity.Producto;
import com.huellitas.app.repository.ProductoRepository;
import com.huellitas.app.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired 
    private ProductoRepository repository;

    @Override
    @Transactional(readOnly = true) 
    public List<Producto> listarTodos() { 
        return repository.findAll(); 
    }

    @Override 
    @Transactional(readOnly = true) 
    public Producto buscarPorId(Integer id) { 
        return repository.findById(id).orElse(null); 
    }

    @Override
    @Transactional 
    public Producto guardar(Producto p) { 
        return repository.save(p); 
    }

    @Override
    @Transactional
    public void eliminar(Integer id) { 
        repository.deleteById(id); 
    }

    
    @Override
    @Transactional
    public Producto actualizar(Producto producto) {
        return repository.save(producto);
    }

    
    @Override
    @Transactional(readOnly = true)
    public String generarCodigo() {
        String ultimoCodigo = repository.obtenerUltimoCodigo();
        if (ultimoCodigo == null || ultimoCodigo.trim().isEmpty()) {
            return "P000001";
        }
        
        try {
            // Remueve cualquier letra o prefijo (ej. "PROD" o "P") quedándose solo con los dígitos
            String numeroTexto = ultimoCodigo.replaceAll("[^0-9]", "");
            if (numeroTexto.isEmpty()) {
                return "P000001";
            }
            
            int numero = Integer.parseInt(numeroTexto);
            numero++; // Secuencial
            
            // Retorna el nuevo código formateado con un prefijo estándar 'P' y 6 dígitos
            return String.format("P%06d", numero);
        } catch (NumberFormatException e) {
            return "P000001"; // En caso de cualquier anomalía, resetea un código base seguro
        }
    }

    @Override
    public void eliminar() {
        // Se mantiene vacío por firma de interfaz alternativa si aplica
    }

    @Override
    @Transactional(readOnly = true)
    public Producto buscarPorCodigo(String codigo) {
        return repository.buscarProductoByCodigo(codigo);
    }
}