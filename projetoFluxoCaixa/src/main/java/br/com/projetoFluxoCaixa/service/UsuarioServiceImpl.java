package br.com.projetoFluxoCaixa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projetoFluxoCaixa.model.Usuario;
import br.com.projetoFluxoCaixa.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	UsuarioRepository ur;


	@Override
	public Usuario findById(Integer id) {
		return ur.findById(id).get();
	}

	@Override
	public Usuario save(Usuario usuario) {
		return ur.save(usuario);
	}


	@Override
	public List<Usuario> findAll() {
		return (List<Usuario>) ur.findAll();
	}
	
}
