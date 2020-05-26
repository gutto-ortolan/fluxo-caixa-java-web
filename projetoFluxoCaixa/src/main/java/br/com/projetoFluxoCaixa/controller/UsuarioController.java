package br.com.projetoFluxoCaixa.controller;
//teste
//teste2
//mudei o teste


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.projetoFluxoCaixa.model.Usuario;
import br.com.projetoFluxoCaixa.repository.UsuarioRepository;

@Controller
public class UsuarioController {
	
	@Autowired
	UsuarioRepository ur;
	
	@RequestMapping("/")
	public String form() {
		return "index";
	}
	
	@RequestMapping("/login")
	public String entrar() {
		return "login";
	}
	
	@RequestMapping("/cadastro")
	public String cadastrar() {
		return "cadastro";
	}
	
	
	@RequestMapping("/menu")
	public String menu(HttpSession session) {
		Usuario usuario = (Usuario)session.getAttribute("usuarioLogado");
		System.out.println("Usuario Logado: "+ usuario.getNome());
		return "menu";
	}
	
	
	@RequestMapping("/efetuarLogin")
	public String efetuarLogin(@RequestParam("email") String email, @RequestParam("senha") String senha, RedirectAttributes ra, HttpSession session) {
		
		System.out.println(email);
		
		Usuario usuarioPesquisa = ur.findUsuarioPorEmail(email, senha);
		System.out.println(usuarioPesquisa);
		
		if(usuarioPesquisa == null) {
			ra.addFlashAttribute("mensagem", "E-mail ou senha inválido.");
			return "redirect:/login";
		}else {
			session.setAttribute("usuarioLogado", usuarioPesquisa);
			return "redirect:/menu";
		}
	}
	
	@RequestMapping(value="/efetuarCadastro", method=RequestMethod.POST)
	public String efetuarCadastro(Usuario usuario, @RequestParam("senha") String senha, @RequestParam("senha1") String senha1, RedirectAttributes ra) {
		
		Usuario usuarioPesquisa = ur.findUsuarioPorEmailIgual(usuario.getEmail());
		
		if(usuarioPesquisa != null) {
			ra.addFlashAttribute("mensagem", "E-mail já cadastrado.");
			return "redirect:/cadastro";
		}
		
		if(senha.equals(senha1)) {
			ur.save(usuario);
			return "redirect:/login";
		}else {
			ra.addFlashAttribute("mensagem", "As senhas devem ser iguais.");
			return "redirect:/cadastro";
		}
	}
	
	

}
