package com.repository.helper.menusAplicacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.MenuAplicacao;
import com.repository.filter.MenuAplicacaoFilter;

public interface MenusAplicacaoQueries {

	public Page<MenuAplicacao> filtrar(MenuAplicacaoFilter filtro, Pageable pageable);
	
}
