/**
 * 
 */
package com.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.model.Permissao;
import com.model.Usuario;
import com.repository.MenusAplicacao;
import com.repository.Permissoes;
import com.repository.Usuarios;
import com.repository.UsuariosPermissoes;
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
	
	@Autowired
	private Permissoes permissoes;

	@Autowired
	private UsuariosPermissoes usuariosPermissoes;

	@Autowired
	private Usuarios usuarios;
	
	public Boolean save = false;
	public Boolean view = false;
	
	public String regraSave = "";

	@GetMapping
	public ModelAndView pesquisar(MenuAplicacaoFilter noticiaFilter, BindingResult result
			, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("menusAplicacao/PesquisaMenusAplicacao");
		
		PageWrapper<MenuAplicacao> paginaWrapper = new PageWrapper<>(menusAplicacao.filtrar(noticiaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@GetMapping("{codigo}/{permissaoPorUsuario}")
	public ModelAndView editar(MenuAplicacaoFilter noticiaFilter, BindingResult result
			, @PageableDefault(size = 15) Pageable pageable, HttpServletRequest httpServletRequest
			, @PathVariable Long codigo, @PathVariable Boolean permissaoPorUsuario) {
		ModelAndView mv = new ModelAndView("menusAplicacao/PesquisaMenusAplicacao");
		
		PageWrapper<MenuAplicacao> paginaWrapper = new PageWrapper<>(menusAplicacao.filtrar(noticiaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		mv.addObject("codigo", codigo);
		mv.addObject("permissaoPorUsuario", permissaoPorUsuario);
		
		List<String> permissoes =  carregaPermissoes(permissaoPorUsuario, codigo);
		
		mv.addObject("permissoes", permissoes);
		/*
		for(String p : permissoes){
			if(p.equals("ROLE_CADASTRAR_CIDADE")){
				save = true;
				mv.addObject("save", save);
			}
			if(p.equals("ROLE_PESQUISAR_CIDADE")){
				view = true;
				mv.addObject("view", view);
			}			
		}*/
		

		
		return mv;
	}
	
	@GetMapping("permissao/{codigo}/{permissaoPorUsuario}/{regra}")
	public ModelAndView permissoes(MenuAplicacaoFilter menuAplicacaoFilter, BindingResult result
			, @PageableDefault(size = 15) Pageable pageable, HttpServletRequest httpServletRequest
			, @PathVariable Long codigo, @PathVariable Boolean permissaoPorUsuario, @PathVariable String regra) {
		ModelAndView mv = new ModelAndView("menusAplicacao/PesquisaMenusAplicacao");
		
		Permissao permissao = permissoes.findByNome(regra);
		
		try {
			usuariosPermissoes.salvar(codigo, permissao.getCodigo());

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao salvar");
		}
		
		List<String> permissoes =  carregaPermissoes(permissaoPorUsuario, codigo);
		
		mv.addObject("permissoes", permissoes);

		
		/*for(String p : permissoes){
			if(p.equals(regra)){
				save = true;
				regraSave = regra;
				mv.addObject("regra", regraSave);
			}if(p.equals(regra)){
				view = true;
				mv.addObject("view", view);
			}
		}*/
		
		
		PageWrapper<MenuAplicacao> paginaWrapper = new PageWrapper<>(menusAplicacao.filtrar(menuAplicacaoFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		mv.addObject("permissaoPorUsuario", permissaoPorUsuario);
		
		return mv;
	}
	
	public List<String> carregaPermissoes(Boolean permissaoPorUsuario, Long codigo){
		
		List<String> permissoes  = new ArrayList<String>();
		
		Usuario usuario = usuarios.findByCodigo(codigo);
		
		if(permissaoPorUsuario){
			 permissoes = usuarios.permissoesPorUsuario(usuario);
		} else {
			permissoes = usuarios.permissoesPorGrupo(usuario);
		}
		
		return permissoes;
	}
	
	public Boolean getSave() {
		return save;
	}
	
	public void setSave(Boolean save) {
		this.save = save;
	}
	
	public Boolean getView() {
		return view;
	}
	
	public void setView(Boolean view) {
		this.view = view;
	}
	
	public String getRegraSave() {
		return regraSave;
	}
	
	public void setRegraSave(String regraSave) {
		this.regraSave = regraSave;
	}	
}
