package com.huellitas.app.repository;

import com.huellitas.app.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // 1. SELECT * limpia todo problema de mapeo de columnas intermedias
    @Query(value = "SELECT * FROM venta ORDER BY nroventa DESC", nativeQuery = true)
    public List<Venta> listarVentasNativo();

    // 2. Búsqueda por clave primaria física
    @Query(value = "SELECT * FROM venta WHERE nroventa = :nroventa", nativeQuery = true)
    public Venta findByNroVenta(@Param("nroventa") Long nroventa);

    // 3. Filtro nativo usando la columna real 'id_cliente' de la BD
    @Query(value = "SELECT * FROM venta WHERE id_cliente = :idCliente ORDER BY nroventa DESC", nativeQuery = true)
    public List<Venta> findByClienteId(@Param("idCliente") Integer idCliente);
}