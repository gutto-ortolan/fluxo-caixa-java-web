package br.com.projetoFluxoCaixa.repository;


import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.projetoFluxoCaixa.model.Lancamento;

@Repository
@Transactional
public interface LancamentoRepository extends CrudRepository<Lancamento, Integer>{
		
	@Query("select l from Lancamento l where id_usuario = ?1")
	ArrayList<Lancamento> findLancamentoPorUsuario(Integer id_usuario);

}
