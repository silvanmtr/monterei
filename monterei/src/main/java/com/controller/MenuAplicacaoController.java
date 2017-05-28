/**
 * 
 */
package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.controller.page.PageWrapper;
import com.model.MenuAplicacao;
import com.repository.MenusAplicacao;
import com.repository.filter.MenuAplicacaoFilter;

/**
 * @author Silvan
 *
 */
@Controller
@RequestMapping("/menusAplicacao")
public class MenuAplicacaoController {

	@Autowired
	private MenusAplicacao menusAplicacao;
	

	@GetMapping
	public ModelAndView pesquisar(MenuAplicacaoFilter noticiaFilter, BindingResult result
			, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("menusAplicacao/PesquisaMenusAplicacao");
		
		PageWrapper<MenuAplicacao> paginaWrapper = new PageWrapper<>(menusAplicacao.filtrar(noticiaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(MenuAplicacaoFilter noticiaFilter, BindingResult result
			, @PageableDefault(size = 15) Pageable pageable, HttpServletRequest httpServletRequest, @PathVariable Long codigo) {
		ModelAndView mv = new ModelAndView("menusAplicacao/PesquisaMenusAplicacao");
		
		PageWrapper<MenuAplicacao> paginaWrapper = new PageWrapper<>(menusAplicacao.filtrar(noticiaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
}
