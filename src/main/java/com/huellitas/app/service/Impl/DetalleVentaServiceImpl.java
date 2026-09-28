package com.huellitas.app.service.Impl;
import com.huellitas.app.entity.DetalleVenta;
import com.huellitas.app.repository.DetalleVentaRepository;
import com.huellitas.app.service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {
    @Autowired 
    private DetalleVentaRepository repository;
    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() { return repository.findAll(); }
    @Override 
    @Transactional
    public DetalleVenta guardar(DetalleVenta d) { return repository.save(d); }
	@Override
	public List<DetalleVenta> buscarDetalleVentaPorNroVenta(Long nroVenta) { return repository.buscarPorNroVenta(nroVenta);}
}