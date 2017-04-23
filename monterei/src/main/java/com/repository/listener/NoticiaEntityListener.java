/**
 * 
 */
package com.repository.listener;

import javax.persistence.PostLoad;

import org.springframework.stereotype.Component;

import com.model.Noticia;
import com.storage.FotoStorage;



/**
 * @author Silvan
 *
 */
@Component
public class NoticiaEntityListener {
	@PostLoad
	public void postLoad(final Noticia noticia) {
		
		noticia.setUrlFoto(FotoStorage.URL + noticia.getFotoOuMock());
		noticia.setUrlThumbnailFoto(FotoStorage.URL + FotoStorage.THUMBNAIL_PREFIX + noticia.getFotoOuMock());
	}
}
