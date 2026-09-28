package com.huellitas.app.entity;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class DetalleVentaId implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "nroventa")
    private Venta venta;

    // CORREGIDO: El nombre debe ser "id_prod" para coincidir con tu BD
    @ManyToOne
    @JoinColumn(name = "id_producto") 
    private Producto producto;
}