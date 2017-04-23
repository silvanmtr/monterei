package com.repository.helper.noticia;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.model.Menu;
import com.model.Noticia;
import com.repository.filter.NoticiaFilter;
import com.repository.paginacao.PaginacaoUtil;

public class NoticiasImpl implements NoticiasQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Noticia> filtrar(NoticiaFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Noticia.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		adicionarFiltro(filtro, criteria);
				
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(NoticiaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Noticia.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(NoticiaFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getTitulo())) {
				criteria.add(Restrictions.ilike("nome", filtro.getTitulo(), MatchMode.ANYWHERE));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Noticia> buscarNoticias(Menu menu) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Noticia.class);
		
		criteria.createAlias("menu", "m");
		criteria.add(Restrictions.eq("m.codigo", menu.getCodigo()));
		List<Noticia> results  = criteria.list();
		return results;
	}

}
