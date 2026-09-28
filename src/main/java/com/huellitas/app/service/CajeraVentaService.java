package com.huellitas.app.service;

import com.huellitas.app.entity.CajeraVenta;
import java.util.List;

public interface CajeraVentaService {
    
    List<CajeraVenta> listarTodos();
    
    CajeraVenta buscarPorId(Integer id);
    
    CajeraVenta guardar(CajeraVenta cajera);
    
    void eliminar(Integer id);
    
    // AGREGADO: Firma necesaria para que tu implementación compile sin problemas
    CajeraVenta actualizar(CajeraVenta cajera);
}