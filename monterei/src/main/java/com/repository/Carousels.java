package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.Carousel;
import com.repository.helper.carousel.CarouselsQueries;


public interface Carousels extends JpaRepository<Carousel, Long>, CarouselsQueries {

	public Carousel findByCodigo(Long codigo);
	public List<Carousel> findByTituloStartingWithIgnoreCase(String nome);
	public Carousel findByFoto(String foto);

}
