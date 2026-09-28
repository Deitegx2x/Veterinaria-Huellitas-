package com.huellitas.app.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cajera_vta")
public class CajeraVenta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cod_cajera")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefono;
}