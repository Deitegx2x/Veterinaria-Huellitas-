package com.huellitas.app.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.huellitas.app.entity.Usuario;
import com.huellitas.app.service.RolService;
import com.huellitas.app.service.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private RolService rolService;

	@GetMapping("/")
	public String login(Model model) {
		if (!model.containsAttribute("usuario")) {
			model.addAttribute("usuario", new Usuario());
		}
		return "login";
	}

	@GetMapping("/home")
	public String home(HttpSession session) {
		if (session.getAttribute("usuarioLogueado") == null) {
			return "redirect:/";
		}
		return "home";
	}

	@PostMapping("/login")
	public String iniciarSesion(@ModelAttribute Usuario usuario, HttpSession session, Model model) {
		boolean esValido = usuarioService.login(usuario);

		if (esValido) {
			Usuario u = usuarioService.buscarPorUsuario(usuario.getUsername());
			session.setAttribute("usuarioLogueado", u);
			return "redirect:/home";
		} else {
			model.addAttribute("error", "Usuario o contraseña incorrectos");
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		Usuario userDto = new Usuario();
		model.addAttribute("usuario", userDto);
		return "register";
	}

	@PostMapping("/register/save")
	public String registration(@ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
		Usuario existingUser = usuarioService.buscarPorUsuario(usuario.getUsername());

		if (usuario.getNombres() == null || usuario.getNombres().trim().isEmpty()) {
			result.rejectValue("nombres", null, "Ingresar nombres");
		}
		if (usuario.getApellidos() == null || usuario.getApellidos().trim().isEmpty()) {
			result.rejectValue("apellidos", null, "Ingresar apellidos");
		}
		if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
			// CORREGIDO: "username" en lugar de "usuario"
			result.rejectValue("username", null, "Ingresar usuario");
		}
		if (usuario.getClave() == null || usuario.getClave().trim().isEmpty()) {
			result.rejectValue("clave", null, "Ingresar clave");
		}
		if (existingUser != null) {
			// CORREGIDO: "username" en lugar de "usuario"
			result.rejectValue("username", null, "Ya existe una cuenta con este usuario");
		}

		if (result.hasErrors()) {
			return "register";
		}

		usuario.setRol(rolService.buscarPorId(2));
		usuarioService.guardar(usuario);
		return "redirect:/register?success";
	}

	@GetMapping("/usuario/new")
	public String createUsuarioForm(Model model) {
		Usuario usuario = new Usuario();
		model.addAttribute("usuario", usuario);
		model.addAttribute("rolList", rolService.listarTodos());
		return "usuario/create";
	}

	@PostMapping("/usuario")
	public String saveUsuario(@ModelAttribute Usuario usuario) {
		usuarioService.guardar(usuario);
		return "redirect:/usuario";
	}

	@GetMapping("/usuario")
	public String lsitUsuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.listarTodos());
		model.addAttribute("rolList", rolService.listarTodos());
		return "usuario/index";
	}

}
