package br.com.projetoFluxoCaixa.service;

import java.util.List;


import br.com.projetoFluxoCaixa.model.Usuario;

public interface UsuarioService {
	
	List<Usuario> findAll();
	Usuario findById(Integer id);
	Usuario save(Usuario usuario);
	

}
