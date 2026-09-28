package com.huellitas.app.repository;

import com.huellitas.app.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	@Query(value = "SELECT u.idusuario,u.usuario,u.nombres,u.apellidos,u.clave,u.idrol "
			+ "FROM usuario u where u.usuario = :username", nativeQuery = true)
	public Usuario findByUsuario(@Param("username") String username);

	@Query(value = "SELECT u.idusuario,u.usuario,u.nombres,u.apellidos,u.clave,u.idrol "
			+ "FROM usuario u where u.usuario = :username and u.clave = :clave", nativeQuery = true)
	public Usuario findByUsuarioAndClave(@Param("username") String username, @Param("clave") String clave);	
}