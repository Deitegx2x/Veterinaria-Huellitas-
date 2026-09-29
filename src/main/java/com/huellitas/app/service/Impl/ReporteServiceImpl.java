package com.huellitas.app.service.Impl;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.huellitas.app.service.ReporteService;

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
        
        // 1. Intentar buscar primero en "reportes/nombre.jasper"
        Resource resource = new ClassPathResource("reportes/" + nombreReporte);
        
        // 2. Si no existe dentro de la subcarpeta 'reportes', buscar en la raíz del classpath ("nombre.jasper")
        if (!resource.exists()) {
            resource = new ClassPathResource(nombreReporte);
        }

        if (!resource.exists()) {
            throw new IllegalArgumentException("No se encontró el archivo del reporte: " + nombreReporte);
        }

        try (Connection connection = dataSource.getConnection();
             InputStream jasperStream = resource.getInputStream()) {

            // Cargar el objeto JasperReport usando JRLoader (Evita fallos de InputStream en Linux/Docker)
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

            // Llenar el reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, connection);

            // Exportar a PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }
}