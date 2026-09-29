package com.huellitas.app.service.Impl;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.huellitas.app.service.ReporteService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class ReporteServiceImpl implements ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteServiceImpl.class);
    private final DataSource dataSource;

    public ReporteServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public byte[] generarReporte(String nombreReporte, Map<String, Object> parametros) throws Exception {
        
        // Forzamos el uso de la extensión .jrxml
        String archivoJrxml = nombreReporte.endsWith(".jasper") 
                ? nombreReporte.replace(".jasper", ".jrxml") 
                : nombreReporte;

        if (!archivoJrxml.endsWith(".jrxml")) {
            archivoJrxml += ".jrxml";
        }

        String ruta = "reportes/" + archivoJrxml;
        log.info("Cargando plantilla JRXML desde ruta: {}", ruta);

        Resource resource = new ClassPathResource(ruta);
        if (!resource.exists()) {
            ruta = archivoJrxml;
            resource = new ClassPathResource(ruta);
        }

        if (!resource.exists()) {
            throw new IllegalArgumentException("No se encontró el archivo JRXML en el classpath: " + archivoJrxml);
        }

        try (InputStream inputStream = resource.getInputStream();
             Connection connection = dataSource.getConnection()) {

            log.info("Compilando reporte JRXML directamente en el servidor...");
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            log.info("Llenando el reporte con la base de datos...");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, connection);

            log.info("Exportando reporte a PDF...");
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }
}