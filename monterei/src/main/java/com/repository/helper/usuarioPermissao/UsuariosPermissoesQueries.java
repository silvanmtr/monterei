package com.repository.helper.usuarioPermissao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.UsuarioPermissao;
import com.repository.filter.UsuarioPermissaoFilter;

public interface UsuariosPermissoesQueries {

	public Page<UsuarioPermissao> filtrar(UsuarioPermissaoFilter filtro, Pageable pageable);
	
	public void salvar(Long codigoUsuario, Long codigoPermissao);
}
