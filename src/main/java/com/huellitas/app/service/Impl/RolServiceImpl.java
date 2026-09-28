package com.huellitas.app.service.Impl;
import com.huellitas.app.entity.Rol;
import com.huellitas.app.repository.RolRepository;
import com.huellitas.app.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RolServiceImpl implements RolService {
    @Autowired 
    private RolRepository repository;
    @Override 
    @Transactional(readOnly = true) 
    public List<Rol> listarTodos() { return repository.findAll(); }
    @Override 
    @Transactional(readOnly = true) 
    public Rol buscarPorId(Integer id) { return repository.findById(id).orElse(null); }
    @Override 
    @Transactional public Rol guardar(Rol r) { return repository.save(r); }
}