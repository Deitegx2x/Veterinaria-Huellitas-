package com.huellitas.app.service;
import com.huellitas.app.entity.Rol;
import java.util.List;

public interface RolService {
    List<Rol> listarTodos();
    Rol buscarPorId(Integer id);
    Rol guardar(Rol r);
}