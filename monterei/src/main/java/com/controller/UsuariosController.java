package com.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.controller.page.PageWrapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.model.Usuario;
import com.report.UsuarioCsvReport;
import com.report.UsuarioExcelReport;
import com.report.UsuarioPdfReport;
import com.repository.Grupos;
import com.repository.Usuarios;
import com.repository.filter.UsuarioFilter;
import com.service.CadastroUsuarioService;
import com.service.StatusUsuario;
import com.service.exception.EmailUsuarioJaCadastradoException;
import com.service.exception.SenhaObrigatoriaUsuarioException;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;

	@Autowired
	private Grupos grupos;

	@Autowired
	private Usuarios usuarios;

	private static String FILE = "C://Users//Silvan//Desktop//Excel//FirstPdf.pdf";
	
	private UsuarioFilter usuarioFilter;

	@RequestMapping("/novo")
	public ModelAndView novo(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuario/CadastroUsuario");
		mv.addObject("grupos", grupos.findAll());
		return mv;
	}

	@PostMapping({ "/novo", "{\\+d}" })
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(usuario);
		}

		try {
			cadastroUsuarioService.salvar(usuario);
		} catch (EmailUsuarioJaCadastradoException e) {
			result.rejectValue("email", e.getMessage(), e.getMessage());
			return novo(usuario);
		} catch (SenhaObrigatoriaUsuarioException e) {
			result.rejectValue("senha", e.getMessage(), e.getMessage());
			return novo(usuario);
		}

		attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso");
		return new ModelAndView("redirect:/usuarios/novo");
	}

	@GetMapping
	public ModelAndView pesquisar(UsuarioFilter usuarioFilter, @PageableDefault(size = 3) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("/usuario/PesquisaUsuarios");
		mv.addObject("grupos", grupos.findAll());

		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(usuarios.filtrar(usuarioFilter, pageable),
				httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		
		this.usuarioFilter = usuarioFilter;
		return mv;
	}

	@PutMapping("/status")
	@ResponseStatus(HttpStatus.OK)
	public void atualizarStatus(@RequestParam("codigos[]") Long[] codigos,
			@RequestParam("status") StatusUsuario statusUsuario) {
		cadastroUsuarioService.alterarStatus(codigos, statusUsuario);
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Usuario usuario = usuarios.buscarComGrupos(codigo);
		ModelAndView mv = novo(usuario);
		mv.addObject(usuario);
		return mv;
	}

	/**
	 * Handle request to download an Excel document
	 * 
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	@GetMapping(value = "/download")
	public ModelAndView download(UsuarioFilter usuarioFilter, @PageableDefault(size = 3) Pageable pageable,
			HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, DocumentException {

		ModelAndView mv = new ModelAndView("/usuario/PesquisaUsuarios");
		mv.addObject("users", usuarios.findAll());

		// PDF
		UsuarioPdfReport pdf = new UsuarioPdfReport();
		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(FILE));

		// Excel
		// For xls -> Workbook wb = new HSSFWorkbook();
		// For xlsx -> Workbook wb = new XSSFWorkbook();
		UsuarioExcelReport excel = new UsuarioExcelReport();
		Workbook workbook = new HSSFWorkbook();

		// CSV
		UsuarioCsvReport csv = new UsuarioCsvReport();

		try {
			pdf.buildPdfDocument(mv.getModel(), document, pdfWriter, request, response);

			excel.buildExcelDocument(mv.getModel(), workbook, request, response);

			csv.buildCsvDocument(mv.getModel(), request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}

		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(usuarios.filtrar(usuarioFilter, pageable), request);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	/**
	 * Handle request to download an Excel document
	 * 
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	@GetMapping(value = "/excel")
	public ModelAndView excel(UsuarioFilter usuarioFilter,@PageableDefault(size = 3) Pageable pageable,
			HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, DocumentException {

		ModelAndView mv = new ModelAndView("/usuario/PesquisaUsuarios");
		
		mv.addObject("users", usuarios.filtrar(this.usuarioFilter));

		// Excel
		// For xls -> Workbook wb = new HSSFWorkbook();
		// For xlsx -> Workbook wb = new XSSFWorkbook();
		UsuarioExcelReport excel = new UsuarioExcelReport();
		Workbook workbook = new HSSFWorkbook();


		try {

			excel.buildExcelDocument(mv.getModel(), workbook, request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}

		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(usuarios.filtrar(usuarioFilter, pageable), request);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	/**
	 * Handle request to download an Excel document
	 * 
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	@GetMapping(value = "/pdf")
	public ModelAndView pdf(UsuarioFilter usuarioFilter, @PageableDefault(size = 3) Pageable pageable,
			HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, DocumentException {

		ModelAndView mv = new ModelAndView("/usuario/PesquisaUsuarios");
		
		mv.addObject("users", usuarios.filtrar(this.usuarioFilter));

		// PDF
		UsuarioPdfReport pdf = new UsuarioPdfReport();
		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(FILE));

		try {
			pdf.buildPdfDocument(mv.getModel(), document, pdfWriter, request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}

		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(usuarios.filtrar(usuarioFilter, pageable), request);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	public UsuarioFilter getUsuarioFilter() {
		return usuarioFilter;
	}
	
	public void setUsuarioFilter(UsuarioFilter usuarioFilter) {
		this.usuarioFilter = usuarioFilter;
	}

}
