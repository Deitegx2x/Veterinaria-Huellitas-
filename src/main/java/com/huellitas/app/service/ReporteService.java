package com.huellitas.app.service;

import java.util.Map;

public interface ReporteService {
    
    byte[] generarReporte(String nombreReporte, Map<String, Object> parametros) throws Exception;
}