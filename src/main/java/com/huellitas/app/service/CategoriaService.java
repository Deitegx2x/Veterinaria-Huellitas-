package com.huellitas.app.service;
import com.huellitas.app.entity.Categoria;
import java.util.List;

public interface CategoriaService {
    List<Categoria> listarTodos();
    Categoria buscarPorId(String id);
    Categoria guardar(Categoria c);
    void eliminar(String id);
}