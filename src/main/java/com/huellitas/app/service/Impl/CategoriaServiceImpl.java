package com.huellitas.app.service.Impl;
import com.huellitas.app.entity.Categoria;
import com.huellitas.app.repository.CategoriaRepository;
import com.huellitas.app.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository repository;
    @Override 
    @Transactional(readOnly = true) 
    public List<Categoria> listarTodos() { return repository.findAll(); }
    @Override @Transactional(readOnly = true) 
    public Categoria buscarPorId(String id) { return repository.findById(id).orElse(null); }
    @Override
    @Transactional
    public Categoria guardar(Categoria c) { return repository.save(c); }
    @Override 
    @Transactional 
    public void eliminar(String id) { repository.deleteById(id); }
}