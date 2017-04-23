package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.controller.page.PageWrapper;
import com.model.Noticia;
import com.repository.Carousels;
import com.repository.Menus;
import com.repository.Noticias;
import com.repository.filter.NoticiaFilter;


@Controller
public class HomeController {

	@Autowired
	private Menus menus;

	@Autowired
	private Carousels carousels;
	
	@Autowired
	private Noticias noticias;
	
	@GetMapping("/home")
	public ModelAndView home(NoticiaFilter noticiaFilter, BindingResult result
			, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		
		ModelAndView mv = new ModelAndView("site/Home");
		
		PageWrapper<Noticia> paginaWrapper = new PageWrapper<>(noticias.filtrar(noticiaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		mv.addObject("menus", menus.findAll());
		mv.addObject("carousels", carousels.findAll());
		mv.addObject("noticias", noticias.findAll());
		return mv;
	}
	
	/*
	 * @GetMapping
	public ModelAndView pesquisar(NoticiaFilter noticiaFilter, BindingResult result
			, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("noticia/PesquisaNoticias");
		
		PageWrapper<Noticia> paginaWrapper = new PageWrapper<>(noticias.filtrar(noticiaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	 */
	
	
}
