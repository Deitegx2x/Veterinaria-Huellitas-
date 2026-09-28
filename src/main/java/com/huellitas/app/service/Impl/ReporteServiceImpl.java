package com.huellitas.app.service.Impl;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.huellitas.app.service.ReporteService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final DataSource dataSource;

    public ReporteServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public byte[] generarReporte(String nombreReporte, Map<String, Object> parametros) throws Exception {
        // Asegúrate de pasar el nombre del archivo compilado (ej: "ReportClientes.jasper")
        try (Connection connection = dataSource.getConnection()) {
            InputStream jasperStream = new ClassPathResource("reportes/" + nombreReporte).getInputStream();

            // Carga y llena directamente el reporte sin recompilar
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametros, connection);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }
}