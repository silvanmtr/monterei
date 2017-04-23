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
import com.model.Carousel;
import com.repository.Carousels;
import com.repository.filter.CarouselFilter;
import com.service.CadastroCarouselService;
import com.service.exception.ImpossivelExcluirEntidadeException;

@Controller
@RequestMapping("/carousel")
public class CarouselController {

	
	@Autowired
	private CadastroCarouselService cadastroCarouselService;
	
	@Autowired
	private Carousels carousels;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Carousel carousel) {
		ModelAndView mv = new ModelAndView("carousel/CadastroCarousel");
		return mv;
	}
	
	@PostMapping(value = { "/novo", "{\\+d}" },  params = "salvar")
	public ModelAndView salvar(@Valid Carousel carousel, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(carousel);
		}
		
		try {
			cadastroCarouselService.salvar(carousel);
		} catch (Exception e) {
			e.printStackTrace();
			return novo(carousel);
		}		
			attributes.addFlashAttribute("mensagem", "Carousel salvo com sucesso!");
		
		return new ModelAndView("redirect:/carousel");
	}
	
	@PostMapping(value={ "/novo", "{\\+d}" }, params="definirComoCarousel")
	public ModelAndView definirComoCarousel(@Valid Carousel carousel, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(carousel);
		}
		System.out.println("Salvou");
		/*try {
			cadastroCarouselService.salvar(carousel);
		} catch (Exception e) {
			e.printStackTrace();
			return novo(carousel);
		}		
			attributes.addFlashAttribute("mensagem", "Carousel salvo com sucesso!");*/
		
		return new ModelAndView("redirect:/carousel");
	}
	
	@GetMapping
	public ModelAndView pesquisar(CarouselFilter carouselFilter, BindingResult result
			, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("carousel/PesquisaCarousel");
		
		PageWrapper<Carousel> paginaWrapper = new PageWrapper<>(carousels.filtrar(carouselFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Carousel carousel = carousels.findByCodigo(codigo);
		ModelAndView mv = novo(carousel);
		mv.addObject(carousel);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Carousel carousel) {
		try {
			cadastroCarouselService.excluir(carousel);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<Carousel> pesquisar(String nome) {
		validarTamanhoNome(nome);
		return carousels.findByTituloStartingWithIgnoreCase(nome);
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
