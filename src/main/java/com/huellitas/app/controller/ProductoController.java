package com.huellitas.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.huellitas.app.entity.Producto;
import com.huellitas.app.service.CategoriaService;
import com.huellitas.app.service.ProductoService;

@Controller
public class ProductoController {
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	
	@GetMapping("/producto")
	public String listProducto(Model model) {
		model.addAttribute("productos", productoService.listarTodos());
		model.addAttribute("categoriaList", categoriaService.listarTodos());
		return "producto/index";
	}

	@GetMapping("/producto/new")
	public String createProducto(Model model) {
		Producto producto = new Producto();
		producto.setCodigo(productoService.generarCodigo());
		model.addAttribute("producto", producto);
		model.addAttribute("categoriaList", categoriaService.listarTodos());
		return "producto/create";
	}
	
	@PostMapping("/producto")
	public String saveProducto(Producto producto) {
		productoService.guardar(producto);
		return "redirect:/producto";
	}
	
	@GetMapping("/producto/edit/{id}")
	public String editProducto(@PathVariable Integer id, Model model) {
		Producto producto = productoService.buscarPorId(id);
		model.addAttribute("producto", producto);
		model.addAttribute("categoriaList", categoriaService.listarTodos());
		return "producto/edit";
	}
	
	@PostMapping("/producto/{id}")
	public String updateProducto(@PathVariable Integer id, Producto producto) {
		Producto existentProducto = productoService.buscarPorId(id);
		existentProducto.setIdProd(id);
		existentProducto.setDescripcion(producto.getDescripcion());
		existentProducto.setPrecioVenta(producto.getPrecioVenta());
		existentProducto.setPrecioCompra(producto.getPrecioCompra());
		existentProducto.setStock(producto.getStock());
		existentProducto.setCategoria(producto.getCategoria());
		productoService.actualizar(existentProducto);
		return "redirect:/producto";
	}
	
	@GetMapping("/producto/delete/{id}")
	public String deleteProducto(@PathVariable Integer id) {
		productoService.eliminar(id);
		return "redirect:/producto";
	}	

}
