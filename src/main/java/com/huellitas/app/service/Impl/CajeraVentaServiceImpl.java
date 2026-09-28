package com.huellitas.app.service.Impl;

import com.huellitas.app.entity.CajeraVenta;
import com.huellitas.app.repository.CajeraVentaRepository;
import com.huellitas.app.service.CajeraVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CajeraVentaServiceImpl implements CajeraVentaService {

    @Autowired
    private CajeraVentaRepository repository;

    @Override 
    @Transactional(readOnly = true) 
    public List<CajeraVenta> listarTodos() { 
        return repository.findAll(); 
    }

    @Override
    @Transactional(readOnly = true) 
    public CajeraVenta buscarPorId(Integer id) { 
        return repository.findById(id).orElse(null); 
    }

    @Override
    @Transactional
    public CajeraVenta guardar(CajeraVenta c) { 
        return repository.save(c); 
    }

    @Override
    @Transactional
    public void eliminar(Integer id) { 
        repository.deleteById(id); 
    }

    // CORREGIDO: Se agregó el método de actualización con su respectivo @Transactional
    @Override
    @Transactional
    public CajeraVenta actualizar(CajeraVenta c) {
        return repository.save(c);
    }
}