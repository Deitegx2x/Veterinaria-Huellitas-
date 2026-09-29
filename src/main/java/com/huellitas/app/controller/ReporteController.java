package com.huellitas.app.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huellitas.app.service.ReporteService;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    private static final Map<String, String> REPORTES = Map.of(
        "ventas", "ReportVentas.jasper",
        "inventario", "ReportInventario.jasper",
        "clientes", "ReportClientes.jasper",
        "mascotas", "ReportMascotas.jasper",
        "ganancias", "ReportGanancias.jasper",
        "boleta", "BoletaVenta.jasper"
    );

    @GetMapping("/{tipo}")
    public ResponseEntity<byte[]> generarReporte(@PathVariable String tipo,
            @RequestParam(defaultValue = "ver") String modo) {

        String jasperFile = REPORTES.get(tipo.toLowerCase());
        if (jasperFile == null) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("El tipo de reporte '" + tipo + "' no es válido.").getBytes());
        }

        try {
            Map<String, Object> parametros = new HashMap<>();
            byte[] pdf = reporteService.generarReporte(jasperFile, parametros);

            String disposition = modo.equalsIgnoreCase("descargar") ? "attachment" : "inline";
            String nombrePdf = jasperFile.replace(".jasper", ".pdf");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=" + nombrePdf)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("Error al generar el reporte: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/boleta/{idVenta}")
    public ResponseEntity<byte[]> generarBoleta(@PathVariable Long idVenta,
            @RequestParam(defaultValue = "ver") String modo) {

        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("idVenta", idVenta);

            // Carga segura del logo sin cerrar el InputStream antes de tiempo
            ClassPathResource logoResource = new ClassPathResource("static/images/logo.jpg");
            if (logoResource.exists()) {
                try (InputStream logoStream = logoResource.getInputStream()) {
                    parametros.put("logo", logoStream);
                    return construirRespuestaPdf("BoletaVenta.jasper", "BoletaVenta_" + idVenta + ".pdf", parametros, modo);
                }
            } else {
                return construirRespuestaPdf("BoletaVenta.jasper", "BoletaVenta_" + idVenta + ".pdf", parametros, modo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("Error al generar la boleta de venta: " + e.getMessage()).getBytes());
        }
    }

    private ResponseEntity<byte[]> construirRespuestaPdf(String jasperFile, String nombrePdf, Map<String, Object> parametros, String modo) throws Exception {
        byte[] pdf = reporteService.generarReporte(jasperFile, parametros);
        String disposition = modo.equalsIgnoreCase("descargar") ? "attachment" : "inline";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=" + nombrePdf)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
