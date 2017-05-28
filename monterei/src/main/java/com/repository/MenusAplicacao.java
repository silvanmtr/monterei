package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.MenuAplicacao;
import com.repository.helper.menusAplicacao.MenusAplicacaoQueries;

public interface MenusAplicacao extends JpaRepository<MenuAplicacao, Long>, MenusAplicacaoQueries{
}
