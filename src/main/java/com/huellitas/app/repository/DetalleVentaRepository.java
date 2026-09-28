package com.huellitas.app.repository;

import com.huellitas.app.entity.DetalleVenta;

import com.huellitas.app.entity.DetalleVentaId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, DetalleVentaId> {

	@Query(value = "SELECT * FROM detalle_ventas WHERE nroventa = :nroVenta", nativeQuery = true)
	List<DetalleVenta> buscarPorNroVenta(@Param("nroVenta")Long nroVenta);
	
}