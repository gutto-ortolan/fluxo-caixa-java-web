package br.com.projetoFluxoCaixa.utils;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.projetoFluxoCaixa.model.Lancamento;
import br.com.projetoFluxoCaixa.model.Usuario;
import br.com.projetoFluxoCaixa.repository.LancamentoRepository;
import br.com.projetoFluxoCaixa.repository.UsuarioRepository;

@Component
public class DummyData {
	
	@Autowired
	UsuarioRepository ur;
	
	@Autowired
	LancamentoRepository lr;
	
	//@PostConstruct
	public void saveUsuario() {
		
		Usuario usuario = new Usuario();
                usuario.setEmail("augustoo");
		usuario.setNome("teste");
                usuario.setSobrenome("teste");
		usuario.setSenha("$2a$10$EKdGixjUS6VbgCn5pRQJ0upm5WuilxLjn2vsR6Kk9UIdJZWmDHhpS");
		ur.save(usuario);
		
		Lancamento lancamento = new Lancamento();
		lancamento.setData(new Date());
		lancamento.setDescricao("teste");
		lancamento.setOperacao("ENTRADA");
		lancamento.setValor(50.0);
		lr.save(lancamento);
		
		
	}

}
