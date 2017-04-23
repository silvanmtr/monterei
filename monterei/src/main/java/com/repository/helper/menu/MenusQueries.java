package com.repository.helper.menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.Menu;
import com.repository.filter.MenuFilter;

public interface MenusQueries {

	public Page<Menu> filtrar(MenuFilter filtro, Pageable pageable);
	
}
