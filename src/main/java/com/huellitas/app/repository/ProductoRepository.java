package com.huellitas.app.repository;

import com.huellitas.app.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    
    @Query(value="SELECT p.id_producto, p.codigo, p.descripcion, p.precio_compra, "
            + "p.precio_venta, p.stock, p.id_categoria FROM producto p "
            + "WHERE p.codigo = :codigo", nativeQuery=true)
    public Producto buscarProductoByCodigo(@Param("codigo") String codigo);
    
    
    @Query(value = "SELECT codigo FROM producto ORDER BY id_producto DESC LIMIT 1", nativeQuery=true)
    public String obtenerUltimoCodigo();
}