/**
 * 
 */
package com.repository.listener;

import javax.persistence.PostLoad;

import org.springframework.stereotype.Component;

import com.model.Carousel;
import com.storage.FotoStorage;



/**
 * @author Silvan
 *
 */
@Component
public class CarouselEntityListener {
	@PostLoad
	public void postLoad(final Carousel carousel) {
		
		carousel.setUrlFoto(FotoStorage.URL + carousel.getFotoOuMock());
		carousel.setUrlThumbnailFoto(FotoStorage.URL + FotoStorage.THUMBNAIL_PREFIX + carousel.getFotoOuMock());
	}
}
