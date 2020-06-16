package br.com.projetoFluxoCaixa.repository;


import java.util.Date;
import java.util.ArrayList;
import java.util.List;

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
	
	@Query("select l from Lancamento l where id_usuario = ?3 and data between ?1 and ?2")
	List<Lancamento> findLancamentosPorMes(String dtInicial, String dtFinal, Integer id_usuario);
	
	@Query("select l from Lancamento l where id_usuario = ?3 and data between ?1 and ?2 and operacao = ?4")
	List<Lancamento> findLancamentosPorPeriodoComOperacao(Date dtInicial, Date dtFinal, Integer id_usuario, String operacao);
	
	@Query("select l from Lancamento l where id_usuario = ?3 and data between ?1 and ?2")
	List<Lancamento> findLancamentosPorPeriodoSemOperacao(Date dtInicial, Date dtFinal, Integer id_usuario);

}
