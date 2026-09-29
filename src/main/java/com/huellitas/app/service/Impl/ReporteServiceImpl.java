package com.huellitas.app.service.Impl;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.huellitas.app.service.ReporteService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final DataSource dataSource;

    public ReporteServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public byte[] generarReporte(String nombreReporte, Map<String, Object> parametros) throws Exception {
        
        // 1. Reemplazamos la extensión .jasper por .jrxml si viene así
        String nombreJrxml = nombreReporte.replace(".jasper", ".jrxml");

        // 2. Intentamos buscar primero el archivo .jrxml
        Resource resource = buscarRecurso(nombreJrxml);
        boolean esJrxml = true;

        // 3. Si no existe el .jrxml, intentamos con el .jasper
        if (resource == null || !resource.exists()) {
            resource = buscarRecurso(nombreReporte);
            esJrxml = false;
        }

        if (resource == null || !resource.exists()) {
            throw new IllegalArgumentException("No se encontró el reporte (" + nombreJrxml + " ni " + nombreReporte + ")");
        }

        try (Connection connection = dataSource.getConnection();
             InputStream inputStream = resource.getInputStream()) {

            JasperReport jasperReport;

            if (esJrxml) {
                // Compilamos en vivo el .jrxml en el entorno Linux de Render
                jasperReport = JasperCompileManager.compileReport(inputStream);
            } else {
                // Fallback para .jasper
                jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            }

            // Llenamos y exportamos a PDF
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, connection);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }

    private Resource buscarRecurso(String nombreArchivo) {
        Resource resource = new ClassPathResource("reportes/" + nombreArchivo);
        if (resource.exists()) {
            return resource;
        }
        return new ClassPathResource(nombreArchivo);
    }
}