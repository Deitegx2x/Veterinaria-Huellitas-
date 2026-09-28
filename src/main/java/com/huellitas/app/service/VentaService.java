package com.huellitas.app.service;

import java.util.List;

import com.huellitas.app.entity.ProductoParaVender;
import com.huellitas.app.entity.Venta;

public interface VentaService {
	Venta guardarVenta(Venta venta);
	
	List<Venta> listarTodosVentas();
	
	Venta registrarVenta(Venta venta, List<ProductoParaVender> carrito);

}
