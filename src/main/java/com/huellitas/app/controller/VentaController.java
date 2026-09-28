package com.huellitas.app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.huellitas.app.entity.CajeraVenta;
import com.huellitas.app.entity.Cliente;
import com.huellitas.app.entity.Mascota;
import com.huellitas.app.entity.Producto;
import com.huellitas.app.entity.ProductoParaVender;
import com.huellitas.app.entity.Venta;
import com.huellitas.app.service.CajeraVentaService;
import com.huellitas.app.service.ClienteService;
import com.huellitas.app.service.DetalleVentaService;
import com.huellitas.app.service.MascotaService;
import com.huellitas.app.service.ProductoService;
import com.huellitas.app.service.VentaService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/venta")
public class VentaController {

    private final ProductoService productoService;
    private final VentaService ventaService;
    private final DetalleVentaService detalleVentaService;
    private final ClienteService clienteService;
    private final CajeraVentaService cajeraVentaService;
    private final MascotaService mascotaService;

    public VentaController(ProductoService productoService, VentaService ventaService,
            DetalleVentaService detalleVentaService, ClienteService clienteService,
            CajeraVentaService cajeraVentaService, MascotaService mascotaService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.detalleVentaService = detalleVentaService;
        this.clienteService = clienteService;
        this.cajeraVentaService = cajeraVentaService;
        this.mascotaService = mascotaService;
    }

    @GetMapping
    public String listVentas(Model model) {
        model.addAttribute("ventas", ventaService.listarTodosVentas());
        return "venta/index";
    }

    @SuppressWarnings("unchecked")
    private ArrayList<ProductoParaVender> obtenerCarrito(HttpServletRequest request) {
        ArrayList<ProductoParaVender> carrito = (ArrayList<ProductoParaVender>) request.getSession().getAttribute("carrito");
        return (carrito == null) ? new ArrayList<>() : carrito;
    }

    private void guardarCarrito(ArrayList<ProductoParaVender> carrito, HttpServletRequest request) {
        request.getSession().setAttribute("carrito", carrito);
    }

    private void limpiarCarrito(HttpServletRequest request) {
        guardarCarrito(new ArrayList<>(), request);
    }

    @GetMapping("/new")
    public String interfazVender(Model model, HttpServletRequest request, RedirectAttributes redirectAttrs) {
        try {
            Venta venta = new Venta();
            
            // Asignar primera mascota disponible de la BD de forma dinámica si existe
            List<Mascota> listaMascotas = mascotaService.listarTodos();
            if (listaMascotas != null && !listaMascotas.isEmpty()) {
                venta.setIdMascota(listaMascotas.get(0).getIdMascota());
            }

            ArrayList<ProductoParaVender> carrito = obtenerCarrito(request);

            model.addAttribute("venta", venta);
            model.addAttribute("carrito", carrito);
            model.addAttribute("clienteList", clienteService.listarTodos());
            model.addAttribute("mascotaList", listaMascotas);
            model.addAttribute("cajeraVentaList", cajeraVentaService.listarTodos());
            model.addAttribute("producto", new Producto());

            // Cálculo blindado del total acumulado
            BigDecimal total = BigDecimal.ZERO;
            if (carrito != null) {
                for (ProductoParaVender p : carrito) {
                    if (p != null && p.getTotal() != null) {
                        total = total.add(p.getTotal());
                    }
                }
            }
            model.addAttribute("total", total);
            return "venta/create";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("mensaje", "Error al cargar la interfaz de venta: " + e.getMessage());
            redirectAttrs.addFlashAttribute("clase", "danger");
            return "redirect:/venta";
        }
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam(name = "codigo", required = false) String codigo,
            HttpServletRequest request, RedirectAttributes redirectAttrs) {

        if (codigo == null || codigo.isBlank()) {
            return "redirect:/venta/new";
        }

        ArrayList<ProductoParaVender> carrito = obtenerCarrito(request);
        Producto prodBD = productoService.buscarPorCodigo(codigo);

        if (prodBD == null) {
            redirectAttrs.addFlashAttribute("mensaje", "Producto no encontrado");
            redirectAttrs.addFlashAttribute("clase", "danger");
            return "redirect:/venta/new";
        }

        if (prodBD.sinExistencia()) {
            redirectAttrs.addFlashAttribute("mensaje", "Producto sin stock");
            redirectAttrs.addFlashAttribute("clase", "warning");
            return "redirect:/venta/new";
        }

        boolean encontrado = false;
        for (ProductoParaVender item : carrito) {
            if (item.getCodigo().equals(prodBD.getCodigo())) {
                if (item.getCantidad() < prodBD.getStock()) {
                    item.aumentarCantidad();
                } else {
                    redirectAttrs.addFlashAttribute("mensaje", "Stock máximo alcanzado");
                    redirectAttrs.addFlashAttribute("clase", "warning");
                }
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            carrito.add(new ProductoParaVender(prodBD.getIdProd(), prodBD.getCodigo(), prodBD.getDescripcion(),
                    prodBD.getPrecioCompra(), prodBD.getPrecioVenta(), prodBD.getStock(), 1));
        }

        guardarCarrito(carrito, request);
        return "redirect:/venta/new";
    }

    @PostMapping("/quitar/{indice}")
    public String quitarDelCarrito(@PathVariable("indice") int indice, HttpServletRequest request) {
        ArrayList<ProductoParaVender> carrito = obtenerCarrito(request);
        if (indice >= 0 && indice < carrito.size()) {
            carrito.remove(indice);
            guardarCarrito(carrito, request);
        }
        return "redirect:/venta/new";
    }

    @PostMapping("/terminar")
    public String terminarVenta(@ModelAttribute("venta") Venta venta, HttpServletRequest request,
            RedirectAttributes redirectAttrs) {

        if (venta.getIdCliente() == null || venta.getCodCajera() == null) {
            redirectAttrs.addFlashAttribute("mensaje", "Debe seleccionar un Cliente y una Cajera");
            redirectAttrs.addFlashAttribute("clase", "warning");
            return "redirect:/venta/new";
        }

        ArrayList<ProductoParaVender> carrito = obtenerCarrito(request);
        if (carrito == null || carrito.isEmpty()) {
            redirectAttrs.addFlashAttribute("mensaje", "No hay productos en el carrito");
            redirectAttrs.addFlashAttribute("clase", "warning");
            return "redirect:/venta/new";
        }

        try {
            Cliente clienteBD = clienteService.buscarPorId(venta.getIdCliente());
            CajeraVenta cajeraBD = cajeraVentaService.buscarPorId(venta.getCodCajera());
            
            Mascota mascotaBD = null;
            if (venta.getIdMascota() != null) {
                mascotaBD = mascotaService.buscarPorId(venta.getIdMascota());
            }

            venta.setCliente(clienteBD);
            venta.setCajeraVenta(cajeraBD);
            if (mascotaBD != null) {
                venta.setMascota(mascotaBD);
            }

            ventaService.registrarVenta(venta, carrito);

            limpiarCarrito(request);

            redirectAttrs.addFlashAttribute("mensaje", "Venta registrada con éxito");
            redirectAttrs.addFlashAttribute("clase", "success");

            return "redirect:/venta";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("mensaje", "Error al registrar la venta: " + e.getMessage());
            redirectAttrs.addFlashAttribute("clase", "danger");
            return "redirect:/venta/new";
        }
    }

    @GetMapping("/limpiar")
    public String cancelarVenta(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        limpiarCarrito(request);
        redirectAttributes.addFlashAttribute("mensaje", "Venta cancelada");
        redirectAttributes.addFlashAttribute("clase", "info");
        return "redirect:/venta/new";
    }

    @GetMapping("/verDetalle/{id}")
    public String verDetalleForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("listDetalleVenta", detalleVentaService.buscarDetalleVentaPorNroVenta(id));
        return "venta/detail";
    }
}