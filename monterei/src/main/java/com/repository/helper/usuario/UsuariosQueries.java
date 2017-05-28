package com.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.Usuario;
import com.repository.filter.UsuarioFilter;

public interface UsuariosQueries {

	public Optional<Usuario> porEmailEAtivo(String email);
	
	public List<String> permissoesPorGrupo(Usuario usuario);
	
	public List<String> permissoesPorUsuario(Usuario usuario);
	
	public Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable);
	
	public Usuario buscarComGrupos(Long codigo);

	List<Usuario> filtrar(UsuarioFilter filtro);
	
}
