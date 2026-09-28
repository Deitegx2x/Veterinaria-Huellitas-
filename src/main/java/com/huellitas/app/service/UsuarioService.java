package com.huellitas.app.service;
import com.huellitas.app.entity.Usuario;
import java.util.List;


public interface UsuarioService {
	
    List<Usuario> listarTodos();
    Usuario buscarPorId(Long id);
    Usuario guardar(Usuario u);
    public boolean login(Usuario u);
    public Usuario buscarPorUsuario(String usuario);
    
    
}