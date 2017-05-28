package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.Permissao;


public interface Permissoes extends JpaRepository<Permissao, Long> {

	public Permissao findByNome(String nome);

}
