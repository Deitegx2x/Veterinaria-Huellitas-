package com.huellitas.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.huellitas.app.entity.Cliente;
import com.huellitas.app.entity.Mascota;
import com.huellitas.app.service.MascotaService;
import com.huellitas.app.service.ClienteService; 

@Controller
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;
    
    @Autowired
    private ClienteService clienteService; // Necesario para cargar el combo de dueños

    @GetMapping("/mascota")
    public String listMascotas(Model model) {
        model.addAttribute("mascotas", mascotaService.listarTodos());
        return "mascota/index";
    }

    @GetMapping("/mascota/new")
    public String createMascota(Model model) {
        Mascota mascota = new Mascota();
        mascota.setCliente(new Cliente());
        
        model.addAttribute("mascota", mascota);
        model.addAttribute("clientes", clienteService.listarTodos()); 
        return "mascota/create";
    }

    @PostMapping("/mascota")
    public String saveMascota(Mascota mascota) {
        mascotaService.guardar(mascota);
        return "redirect:/mascota";
    }

    @GetMapping("/mascota/edit/{id}")
    public String editMascota(@PathVariable Integer id, Model model) {
        Mascota mascota = mascotaService.buscarPorId(id);
        model.addAttribute("mascota", mascota);
        model.addAttribute("clientes", clienteService.listarTodos()); 
        return "mascota/edit";
    }

    @PostMapping("/mascota/{id}")
    public String updateMascota(@PathVariable Integer id, Mascota mascota) {
        Mascota existentMascota = mascotaService.buscarPorId(id);

        // Actualizamos todos los atributos de la mascota existente
        existentMascota.setNombre(mascota.getNombre());
        existentMascota.setRaza(mascota.getRaza());
        existentMascota.setEspecie(mascota.getEspecie());
        existentMascota.setFecha_nacimiento(mascota.getFecha_nacimiento());
        existentMascota.setGenero(mascota.getGenero());
        existentMascota.setHistorial(mascota.getHistorial());
        
        // Actualizamos la llave foránea del cliente dueño
        existentMascota.setCliente(mascota.getCliente());

        // Nota: En tu MascotaService no definiste 'actualizar', 
        // pero en Spring Data JPA 'guardar' (save) sirve tanto para insertar como para actualizar.
        mascotaService.guardar(existentMascota); 
        
        return "redirect:/mascota";
    }

    @GetMapping("/mascota/delete/{id}")
    public String deleteMascota(@PathVariable Integer id) {
        mascotaService.eliminar(id);
        return "redirect:/mascota";
    }
}
