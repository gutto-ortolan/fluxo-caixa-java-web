package br.com.projetoFluxoCaixa.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.projetoFluxoCaixa.model.Lancamento;
import br.com.projetoFluxoCaixa.model.Usuario;
import br.com.projetoFluxoCaixa.repository.LancamentoRepository;
import br.com.projetoFluxoCaixa.repository.UsuarioRepository;


@Controller
public class UsuarioController {
	
	@Autowired
	LancamentoRepository lr;
	
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
	public String menu(HttpSession session, RedirectAttributes ra) {
		System.out.println(session);
		
		return "menu";
		
	}	
		
	@RequestMapping("/efetuarLogin")
	public String efetuarLogin(@RequestParam("email") String email, @RequestParam("senha") String senha, RedirectAttributes ra, HttpSession session) {
		
		
		Usuario usuarioPesquisa = ur.findUsuarioPorEmail(email, senha);
		System.out.println(usuarioPesquisa);
		
		if(usuarioPesquisa == null) {
			ra.addFlashAttribute("mensagem", "E-mail ou senha inválido.");
			ra.addFlashAttribute("email", email);
			return "redirect:/login";
		}else {
			session.setAttribute("usuarioLogado", usuarioPesquisa);			
						
			Lancamento lancamento = new Lancamento();
			lancamento.setData(new Date());
			lancamento.setDescricao("luz");
			lancamento.setOperacao("ENTRADA");
			lancamento.setValor(50.0);
			lancamento.setUsuario(usuarioPesquisa);
			lr.save(lancamento);
			
			//Lancamento lancamentoPesquisa = lr.findUsuarioPorLancamento(1);
			ra.addFlashAttribute("user", usuarioPesquisa);
			ra.addFlashAttribute("users", ur.findAll());
	
			return "redirect:/menu";
		}
	}
	
	@RequestMapping(value="/efetuarCadastro", method=RequestMethod.POST)
	public String efetuarCadastro(Usuario usuario, @RequestParam("senha") String senha, @RequestParam("senha1") String senha1, RedirectAttributes ra) {
		
		Usuario usuarioPesquisa = ur.findUsuarioPorEmailIgual(usuario.getEmail());
		
		if(usuarioPesquisa != null) {
			ra.addFlashAttribute("mensagem", "E-mail já cadastrado.");
		}		
		else if(senha.equals(senha1)) {
			ur.save(usuario);
			return "redirect:/login";
		}else {
			ra.addFlashAttribute("mensagem", "As senhas devem ser iguais.");
		}
		ra.addFlashAttribute("nome", usuario.getNome());
		ra.addFlashAttribute("sobrenome", usuario.getSobrenome());
		ra.addFlashAttribute("email", usuario.getEmail());	
		return "redirect:/cadastro";


	}
	
	

}