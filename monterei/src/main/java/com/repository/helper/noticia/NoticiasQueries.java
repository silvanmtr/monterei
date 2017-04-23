package com.repository.helper.noticia;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.Menu;
import com.model.Noticia;
import com.repository.filter.NoticiaFilter;

public interface NoticiasQueries {

	public Page<Noticia> filtrar(NoticiaFilter filtro, Pageable pageable);
	public List<Noticia> buscarNoticias(Menu menu);
	
}
