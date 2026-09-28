package com.huellitas.app.service.Impl;
import com.huellitas.app.entity.Usuario;
import com.huellitas.app.repository.UsuarioRepository;
import com.huellitas.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired 
    private UsuarioRepository repository;
    @Override 
    @Transactional(readOnly = true) 
    public List<Usuario> listarTodos() { return repository.findAll(); }
    @Override
    @Transactional(readOnly = true) 
    public Usuario buscarPorId(Long id) { return repository.findById(id).orElse(null); }
    @Override 
    @Transactional
    public Usuario guardar(Usuario u) { return repository.save(u); }
	@Override
	public boolean login(Usuario u) {
		Usuario entidad=repository.findByUsuarioAndClave(u.getUsername(), u.getClave());
		System.out.println("usuario.getUsername()---> " + u.getUsername());
		System.out.println("usuario.getClave()---> " +u.getClave());
		if(entidad==null)
			return false;
		else
			return true;
	}
	@Override
	@Transactional(readOnly = true)
	public Usuario buscarPorUsuario(String usuario) {
	    return repository.findByUsuario(usuario);
	}
	
}