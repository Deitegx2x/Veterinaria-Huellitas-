package com.huellitas.app.service.Impl;
import com.huellitas.app.entity.Mascota;

import com.huellitas.app.repository.MascotaRepository;
import com.huellitas.app.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MascotaServiceImpl implements MascotaService {
    @Autowired 
    private MascotaRepository repository;
    @Override 
    @Transactional(readOnly = true)
    public List<Mascota> listarTodos() { return repository.findAll(); }
    @Override
    @Transactional(readOnly = true) public Mascota buscarPorId(Integer id) { return repository.findById(id).orElse(null); }
    @Override 
    @Transactional public Mascota guardar(Mascota m) { return repository.save(m); }
    @Override 
    @Transactional public void eliminar(Integer id) { repository.deleteById(id); }
}