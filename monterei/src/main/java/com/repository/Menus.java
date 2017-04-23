package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.Menu;
import com.repository.helper.menu.MenusQueries;

public interface Menus extends JpaRepository<Menu, Long>, MenusQueries {

	public List<Menu> findByNomeStartingWithIgnoreCase(String nome);
	public Menu findByCodigo(Long codigo);
	public Menu findByCaminho(String nome);

}
