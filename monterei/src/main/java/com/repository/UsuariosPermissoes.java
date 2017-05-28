package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.UsuarioPermissao;
import com.repository.helper.usuarioPermissao.UsuariosPermissoesQueries;


public interface UsuariosPermissoes extends JpaRepository<UsuarioPermissao, Long>, UsuariosPermissoesQueries{

}
