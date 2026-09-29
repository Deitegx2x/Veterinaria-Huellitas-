package com.huellitas.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private ClienteService clienteService;

    @GetMapping("/mascota")
    public String listMascotas(Model model) {
        model.addAttribute("mascotas", mascotaService.listarTodos());
        return "Mascota/index";
    }

    @GetMapping("/mascota/new")
    public String createMascota(Model model) {
        Mascota mascota = new Mascota();
        mascota.setCliente(new Cliente());

        model.addAttribute("mascota", mascota);
        model.addAttribute("clientes", clienteService.listarTodos());
        return "Mascota/create";
    }

    @PostMapping("/mascota")
    public String saveMascota(@ModelAttribute("mascota") Mascota mascota) {
        mascotaService.guardar(mascota);
        return "redirect:/mascota";
    }

    @GetMapping("/mascota/edit/{id}")
    public String editMascota(@PathVariable Integer id, Model model) {
        Mascota mascota = mascotaService.buscarPorId(id);
        model.addAttribute("mascota", mascota);
        model.addAttribute("clientes", clienteService.listarTodos());
        return "Mascota/edit";
    }

    @PostMapping("/mascota/{id}")
    public String updateMascota(@PathVariable Integer id, @ModelAttribute("mascota") Mascota mascota) {
        Mascota existentMascota = mascotaService.buscarPorId(id);

        if (existentMascota != null) {
            existentMascota.setNombre(mascota.getNombre());
            existentMascota.setRaza(mascota.getRaza());
            existentMascota.setEspecie(mascota.getEspecie());
            existentMascota.setFecha_nacimiento(mascota.getFecha_nacimiento());
            existentMascota.setGenero(mascota.getGenero());
            existentMascota.setHistorial(mascota.getHistorial());
            existentMascota.setCliente(mascota.getCliente());

            mascotaService.guardar(existentMascota);
        }

        return "redirect:/mascota";
    }

    @GetMapping("/mascota/delete/{id}")
    public String deleteMascota(@PathVariable Integer id) {
        mascotaService.eliminar(id);
        return "redirect:/mascota";
    }
}
