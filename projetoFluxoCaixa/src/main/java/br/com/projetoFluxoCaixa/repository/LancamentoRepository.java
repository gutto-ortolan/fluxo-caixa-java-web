package br.com.projetoFluxoCaixa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.projetoFluxoCaixa.model.Lancamento;


@Repository
@Transactional
public interface LancamentoRepository extends CrudRepository<Lancamento, Integer>{
	

}
