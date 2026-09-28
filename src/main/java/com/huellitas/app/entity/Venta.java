package com.huellitas.app.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "venta")
public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nroventa")
    private Long nroventa; // CORREGIDO: Se cambió de 'id' a 'nroventa' para coincidir con tus queries nativas

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_venta")
    private Date fechaVenta;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal igv;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal ganancia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_cajera", nullable = false)
    private CajeraVenta cajeraVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    @Transient private Integer idCliente;
    @Transient private Integer codCajera;
    @Transient private Integer idMascota;
}