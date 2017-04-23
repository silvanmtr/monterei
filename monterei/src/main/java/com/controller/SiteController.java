package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.model.Menu;
import com.repository.Menus;
import com.repository.Noticias;


@Controller
@RequestMapping("/site")
public class SiteController {

	@Autowired
	private Menus menus;

	@Autowired
	private Noticias noticias;
	
	@GetMapping("/{categoria}")
	public ModelAndView home(@PathVariable String categoria) {
		
		System.out.println("Cat: " + categoria);
		
		Menu menu = menus.findByCaminho("site/" + categoria);
		ModelAndView mv = new ModelAndView("site/TemplateNoticiasEsporte");
		mv.addObject("menus", menus.findAll());
		mv.addObject("menuAtivo", menu.getNome());
		mv.addObject("noticiasEsporte", noticias.buscarNoticias(menu));

		return mv;		
	}	
}
