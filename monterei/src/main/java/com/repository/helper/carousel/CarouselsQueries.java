package com.repository.helper.carousel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.Carousel;
import com.repository.filter.CarouselFilter;

public interface CarouselsQueries {

	public Page<Carousel> filtrar(CarouselFilter filtro, Pageable pageable);
	
}
