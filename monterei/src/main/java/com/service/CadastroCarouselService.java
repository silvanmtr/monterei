package com.service;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model.Carousel;
import com.repository.Carousels;
import com.service.exception.ImpossivelExcluirEntidadeException;

@Service
public class CadastroCarouselService {

	@Autowired
	private Carousels carousels;
	
	@Transactional
	public void salvar(Carousel carousel) {		
		carousels.save(carousel);
	}
	
	@Transactional
	public void excluir(Carousel carousel) {
		try {
			carousels.delete(carousel);
			carousels.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível excluir.");
		}
	}
	
}
