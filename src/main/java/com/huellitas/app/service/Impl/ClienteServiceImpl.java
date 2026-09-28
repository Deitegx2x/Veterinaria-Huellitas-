package com.huellitas.app.service.Impl;

import com.huellitas.app.entity.Cliente;
import com.huellitas.app.repository.ClienteRepository;
import com.huellitas.app.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() { 
        return repository.findAll(); 
    }

    @Override
    @Transactional(readOnly = true) 
    public Cliente buscarPorId(Integer id) { 
        return repository.findById(id).orElse(null); 
    }

    @Override
    @Transactional
    public Cliente guardar(Cliente c) { 
        return repository.save(c); 
    }

    @Override 
    @Transactional 
    public void eliminar(Integer id) { 
        repository.deleteById(id); 
    }

    // CORREGIDO: Se añadió @Transactional para asegurar la persistencia en actualizaciones
    @Override
    @Transactional
    public Cliente actualizar(Cliente c) {
        return repository.save(c);
    }
}