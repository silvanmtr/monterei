package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.Menu;
import com.model.Noticia;
import com.repository.helper.noticia.NoticiasQueries;


public interface Noticias extends JpaRepository<Noticia, Long>, NoticiasQueries {

	public List<Noticia> findByTituloStartingWithIgnoreCase(String nome);
	public Noticia findByCodigo(Long codigo);
	public Noticia findByMenu(Menu menu);

}
