package com.huellitas.app.service;
import com.huellitas.app.entity.Cliente;
import java.util.List;


public interface ClienteService {
    List<Cliente> listarTodos();
    Cliente buscarPorId(Integer id);
    Cliente guardar(Cliente c);
    Cliente actualizar(Cliente c);
    void eliminar(Integer id);
}