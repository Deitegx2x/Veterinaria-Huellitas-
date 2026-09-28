package com.huellitas.app.repository;
import com.huellitas.app.entity.CajeraVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CajeraVentaRepository extends JpaRepository<CajeraVenta, Integer> {
	
}