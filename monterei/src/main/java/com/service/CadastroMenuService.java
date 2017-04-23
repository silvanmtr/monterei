package com.service;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model.Menu;
import com.repository.Menus;
import com.service.exception.ImpossivelExcluirEntidadeException;

@Service
public class CadastroMenuService {

	@Autowired
	private Menus menus;
	
	@Transactional
	public void salvar(Menu menu) {		
		menus.save(menu);
	}
	
	@Transactional
	public void excluir(Menu menu) {
		try {
			menus.delete(menu);
			menus.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar menu.");
		}
	}
	
}
