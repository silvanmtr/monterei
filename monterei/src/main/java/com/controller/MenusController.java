package com.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.controller.page.PageWrapper;
import com.model.Menu;
import com.repository.Menus;
import com.repository.filter.MenuFilter;
import com.service.CadastroMenuService;
import com.service.exception.ImpossivelExcluirEntidadeException;

@Controller
@RequestMapping("/menus")
public class MenusController {

	
	@Autowired
	private CadastroMenuService cadastroMenuService;
	
	@Autowired
	private Menus menus;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Menu menu) {
		ModelAndView mv = new ModelAndView("menu/CadastroMenu");
		return mv;
	}
	
	@PostMapping({ "/novo", "{\\+d}" })
	public ModelAndView salvar(@Valid Menu menu, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(menu);
		}
		
		try {
			cadastroMenuService.salvar(menu);
		} catch (Exception e) {
			e.printStackTrace();
			return novo(menu);
		}		
			attributes.addFlashAttribute("mensagem", "Menu salvo com sucesso!");
		
		return new ModelAndView("redirect:/menus");
	}
	
	@GetMapping
	public ModelAndView pesquisar(MenuFilter menuFilter, BindingResult result
			, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("menu/PesquisaMenus");
		
		PageWrapper<Menu> paginaWrapper = new PageWrapper<>(menus.filtrar(menuFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Menu menu = menus.findByCodigo(codigo);
		ModelAndView mv = novo(menu);
		mv.addObject(menu);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Menu menu) {
		try {
			cadastroMenuService.excluir(menu);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<Menu> pesquisar(String nome) {
		validarTamanhoNome(nome);
		return menus.findByNomeStartingWithIgnoreCase(nome);
	}

	private void validarTamanhoNome(String nome) {
		if (StringUtils.isEmpty(nome) || nome.length() < 3) {
			throw new IllegalArgumentException();
		}
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> tratarIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().build();
	}
	
}
