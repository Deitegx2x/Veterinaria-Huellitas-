package com.huellitas.app.service;
import com.huellitas.app.entity.Mascota;

import java.util.List;

public interface MascotaService {
    List<Mascota> listarTodos();
    Mascota buscarPorId(Integer id);
    Mascota guardar(Mascota m);
    void eliminar(Integer id);
}