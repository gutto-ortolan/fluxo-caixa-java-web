package br.com.projetoFluxoCaixa.controller;

import java.util.Date;
import java.util.List;

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
		
		//É preciso trazer o usuario para trazer seus lançamentos e mostrá-los no "menu" já filtrados

		return "menu";
		
	}	
		
	@RequestMapping("/efetuarLogin")
	public String efetuarLogin(@RequestParam("email") String email, @RequestParam("senha") String senha, RedirectAttributes ra, HttpSession session) {
		
		
		Usuario usuarioPesquisa = ur.findUsuarioPorEmail(email, senha);
		
		if(usuarioPesquisa == null) {
			ra.addFlashAttribute("mensagem", "E-mail ou senha inválido.");
			ra.addFlashAttribute("email", email);
			return "redirect:/login";
		}else {
			session.setAttribute("usuarioLogado", usuarioPesquisa);	
			
			//	Teste adiona lan	
			Lancamento lancamento = new Lancamento();
			Date suaData = new Date();
			lancamento.setData(suaData);			
			lancamento.setDescricao("Dizimo");
			lancamento.setOperacao("SAIDA");
			lancamento.setValor(200.0);
			lancamento.setUsuario(usuarioPesquisa);
			lr.save(lancamento);
			
			List<Lancamento> lancamentoPesquisa= lr.findLancamentoPorUsuario(usuarioPesquisa.getIdUsuario());
			
			ra.addFlashAttribute("lan", lancamentoPesquisa);	
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
	
	

