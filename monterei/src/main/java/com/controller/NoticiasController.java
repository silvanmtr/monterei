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
import com.model.Noticia;
import com.repository.Carousels;
import com.repository.Menus;
import com.repository.Noticias;
import com.repository.filter.NoticiaFilter;
import com.service.CadastroCarouselService;
import com.service.CadastroNoticiaService;
import com.service.exception.ImpossivelExcluirEntidadeException;

@Controller
@RequestMapping("/noticias")
public class NoticiasController {

	
	@Autowired
	private CadastroNoticiaService cadastroNoticiaService;
	
	@Autowired
	private Noticias noticias;
	
	@Autowired
	private Carousels carousels;
	

	@Autowired
	private CadastroCarouselService cadastroCarouselService;
	
	@Autowired
	private Menus menus;
	
	
	@RequestMapping("/nova")
	public ModelAndView novo(Noticia noticia) {
		ModelAndView mv = new ModelAndView("noticia/CadastroNoticia");
		mv.addObject("menus", menus.findAll());
		mv.addObject("carousels", carousels.findAll());
		return mv;
	}
	
	@PostMapping(value = { "/nova", "{\\+d}" },  params = "salvar")
	public ModelAndView salvar(@Valid Noticia noticia, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(noticia);
		}
		
		
		if(noticia.getCarousel() != null && noticia.isNovaFoto()){
			
			if(noticia.getCarousel().getCodigo() != null){
				attributes.addFlashAttribute("mensagem", "Essa notícia está definida como carousel, "
						+ "para alterar a imagem do carousel, salve e adicone ao carousel.");
				return novo(noticia);
			}
		}
		
		try {
			cadastroNoticiaService.salvar(noticia);
		} catch (Exception e) {
			e.printStackTrace();
			return novo(noticia);
		}		
			attributes.addFlashAttribute("mensagem", "Notícia salva com sucesso!");
		
		return new ModelAndView("redirect:/noticias");
	}	
	
	@PostMapping(value = { "/nova", "{\\+d}" }, params="definirComoCarousel")
	public ModelAndView definirComoCarousel(@Valid Noticia noticia, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(noticia);
		}
		
		Noticia n = noticias.findByCodigo(noticia.getCodigo());
		Carousel carousel = new Carousel();
		
		if(!noticia.isNova() && n.getCarousel() != null){
			carousel.setCodigo(n.getCarousel().getCodigo());
		}
		carousel.setContentType(noticia.getContentType());
		carousel.setDescricao(noticia.getDescricao());
		carousel.setTitulo(noticia.getTitulo());
		carousel.setFoto(noticia.getFoto());
		carousel.setUrlFoto(noticia.getUrlFoto());
		carousel.setUrlThumbnailFoto(noticia.getUrlThumbnailFoto());
		
		cadastroCarouselService.salvar(carousel);

		noticia.setCarousel(carousel);
		
		
		try {
			cadastroNoticiaService.salvar(noticia);
		} catch (Exception e) {
			e.printStackTrace();
			return novo(noticia);
		}		
			attributes.addFlashAttribute("mensagem", "Notícia salva com sucesso!");
		
		return new ModelAndView("redirect:/noticias");
	}	
	
	@PostMapping(value = { "/nova", "{\\+d}" }, params="removerCarousel")
	public ModelAndView removerCarousel(@Valid Noticia noticia, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(noticia);
		}
		
		Noticia n = noticias.findByCodigo(noticia.getCodigo());
		Carousel carousel = new Carousel();
		
		if(n.getCarousel() != null){
			carousel.setCodigo(n.getCarousel().getCodigo());			
		} else {
			attributes.addFlashAttribute("mensagem", "Não existe uma imagem definida como Carousel para essa notícia.");			
			return novo(noticia);
		}
		
		
		
		cadastroCarouselService.excluir(carousel);

		noticia.setCarousel(null);
		
		
		try {
			cadastroNoticiaService.salvar(noticia);
		} catch (Exception e) {
			e.printStackTrace();
			return novo(noticia);
		}		
			attributes.addFlashAttribute("mensagem", "Notícia salva com sucesso!");
		
		return new ModelAndView("redirect:/noticias");
	}
	
	@GetMapping
	public ModelAndView pesquisar(NoticiaFilter noticiaFilter, BindingResult result
			, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("noticia/PesquisaNoticias");
		
		PageWrapper<Noticia> paginaWrapper = new PageWrapper<>(noticias.filtrar(noticiaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Noticia noticia = noticias.findByCodigo(codigo);
		ModelAndView mv = novo(noticia);
		mv.addObject(noticia);
		return mv;
	}
	
	@GetMapping("/noticia/{codigo}")
	public ModelAndView exibir(@PathVariable Long codigo) {
		
		System.out.println(menus.count());
		
		Noticia noticia = noticias.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("site/TemplateNoticias");
		mv.addObject("menus", menus.findAll());
		mv.addObject("noticias",noticia);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Noticia noticia) {
		try {
			cadastroNoticiaService.excluir(noticia);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<Noticia> pesquisar(String nome) {
		validarTamanhoNome(nome);
		return noticias.findByTituloStartingWithIgnoreCase(nome);
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
