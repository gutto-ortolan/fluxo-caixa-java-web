package br.com.projetoFluxoCaixa.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.projetoFluxoCaixa.model.Usuario;

@Repository
@Transactional
public interface UsuarioRepository extends CrudRepository<Usuario, Integer>{
	
	@Query("select u from Usuario u where u.email = ?1 and senha = ?2")
	Usuario findUsuarioPorEmail(String email, String senha);
	
	@Query("select u from Usuario u where u.email = ?1")
	Usuario findUsuarioPorEmailIgual(String email);

}
