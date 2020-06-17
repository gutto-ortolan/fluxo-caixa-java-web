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
		
	@Query("select l from Lancamento l where id_usuario = ?1 order by l.data")
	ArrayList<Lancamento> findLancamentoPorUsuario(Integer id_usuario);
	
	@Query("select l from Lancamento l where id_usuario = ?3 and data between ?1 and ?2 order by l.data")
	List<Lancamento> findLancamentosPorMes(Date dtInicial, Date dtFinal, Integer id_usuario);
	
	@Query("select l from Lancamento l where id_usuario = ?3 and data between ?1 and ?2 and operacao = ?4 order by l.data")
	List<Lancamento> findLancamentosPorPeriodoComOperacao(Date dtInicial, Date dtFinal, Integer id_usuario, String operacao);
	
	@Query("select l from Lancamento l where id_usuario = ?3 and data between ?1 and ?2 order by l.data")
	List<Lancamento> findLancamentosPorPeriodoSemOperacao(Date dtInicial, Date dtFinal, Integer id_usuario);
	
	@Query("SELECT sum(case when operacao = 'ENTRADA' then valor ELSE -valor END) as saldo FROM Lancamento WHERE id_usuario = ?1 and data < ?2 ")
	Double findSaldoInicial(Integer id_usuario, Date dataInicio);
	
	@Query("SELECT sum(case when operacao = 'ENTRADA' then valor ELSE -valor END) as saldo FROM Lancamento WHERE id_usuario = ?1 and data BETWEEN ?2 AND ?3 ")
	Double findSaldoFinal(Integer id_usuario, Date dataInicio, Date dataFim);
	

}
