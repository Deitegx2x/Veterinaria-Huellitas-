package com.huellitas.app.service;
import com.huellitas.app.entity.DetalleVenta;


import java.util.List;

public interface DetalleVentaService {
    List<DetalleVenta> listarTodos();
    
    List<DetalleVenta> buscarDetalleVentaPorNroVenta(Long nroVenta);
    
    DetalleVenta guardar(DetalleVenta d);
}