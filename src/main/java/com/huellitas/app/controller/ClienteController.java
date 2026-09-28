package com.huellitas.app.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.huellitas.app.entity.Cliente;
import com.huellitas.app.service.ClienteService;

@Controller
public class ClienteController {
	
	@Autowired
	private ClienteService service;
	
	@GetMapping("/cliente")
	public String listClientes(Model model) {
		model.addAttribute("clientes", service.listarTodos());
		return "cliente/index";
	}
	
	@GetMapping("/cliente/new")
	public String createCliente(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		return "cliente/create";
	}
	
	@PostMapping("/cliente")
	public String saveCliente(Cliente cliente) {
		service.guardar(cliente);
		return "redirect:/cliente";
	}
	
	@GetMapping("/cliente/edit/{id}")
	public String editCliente(@PathVariable Integer id, Model model) {
		Cliente cliente = service.buscarPorId(id);
		model.addAttribute("cliente", cliente);
		return "cliente/edit";
	}
	
	@PostMapping("/cliente/{id}")
	public String updateCliente(@PathVariable Integer id, Cliente cliente) {
	    Cliente existentCliente = service.buscarPorId(id);

	    existentCliente.setIdClie(id);
	    existentCliente.setNombre(cliente.getNombre());
	    existentCliente.setApellido(cliente.getApellido());
	    existentCliente.setTelefono(cliente.getTelefono());
	    existentCliente.setDni(cliente.getDni());

	    service.actualizar(existentCliente);
	    return "redirect:/cliente";
	}

	@GetMapping("/cliente/delete/{id}")
	public String deleteCliente(@PathVariable Integer id) {
	    service.eliminar(id);
	    return "redirect:/cliente";
	}	

}
