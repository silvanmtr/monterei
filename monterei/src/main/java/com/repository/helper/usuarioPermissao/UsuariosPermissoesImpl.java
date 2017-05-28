package com.repository.helper.usuarioPermissao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

import com.model.UsuarioPermissao;
import com.repository.filter.UsuarioPermissaoFilter;
import com.repository.paginacao.PaginacaoUtil;

public class UsuariosPermissoesImpl implements UsuariosPermissoesQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<UsuarioPermissao> filtrar(UsuarioPermissaoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(UsuarioPermissao.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		adicionarFiltro(filtro, criteria);
				
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(UsuarioPermissaoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(UsuarioPermissao.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(UsuarioPermissaoFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getTitulo())) {
				criteria.add(Restrictions.ilike("nome", filtro.getTitulo(), MatchMode.ANYWHERE));
			}
		}
	}

	@Override
	@Transactional
	public void salvar(Long codigoUsuario, Long codigoPermissao) {
		
		Query select =  manager.createNativeQuery("SELECT * FROM usuario_permissao "
												+ " WHERE codigo_usuario = " + codigoUsuario + " AND codigo_permissao = " + codigoPermissao);
		
		if(select.getResultList().size() > 0){
			Query queryExclusao = manager.createNativeQuery("DELETE FROM usuario_permissao "
					  + " WHERE codigo_usuario = " + codigoUsuario + " AND codigo_permissao = " + codigoPermissao);
			
			queryExclusao.executeUpdate();
			
		} else {
			Query queryInsercao =  manager.createNativeQuery("INSERT INTO usuario_permissao "
	  			   + " (codigo_usuario, codigo_permissao) "
	  			   + " VALUES(" + codigoUsuario + ", " + codigoPermissao + ")");
			queryInsercao.executeUpdate();
			
		}
		
		
		
		 
		
		
	}

	

}
