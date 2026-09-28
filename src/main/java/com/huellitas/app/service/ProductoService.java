package com.huellitas.app.service;
import com.huellitas.app.entity.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> listarTodos();
    Producto buscarPorId(Integer id);
    Producto guardar(Producto p);
    Producto actualizar(Producto producto);
    public Producto buscarPorCodigo(String codigo);
    void eliminar(Integer id);
    public String generarCodigo();
    public void eliminar();
}