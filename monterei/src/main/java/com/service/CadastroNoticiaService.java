package com.service;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model.Noticia;
import com.repository.Noticias;
import com.service.exception.ImpossivelExcluirEntidadeException;
import com.storage.FotoStorage;

@Service
public class CadastroNoticiaService {

	@Autowired
	private Noticias noticias;

	
	@Autowired
	private FotoStorage fotoStorage;
	
	@Transactional
	public void salvar(Noticia noticia) {		
		noticias.save(noticia);
	}
	
	@Transactional
	public void excluir(Noticia noticia) {
		try {
			String foto = noticia.getFoto();
			noticias.delete(noticia);
			noticias.flush();
			fotoStorage.excluir(foto);

		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar menu.");
		}
	}
	
}
