package br.com.projetoFluxoCaixa.service;

import java.util.List;


import br.com.projetoFluxoCaixa.model.Lancamento;

public interface LancamentoService {
	
	List<Lancamento> findAll();
	Lancamento findById(Integer id);
	Lancamento save(Lancamento lancamento);
	List<Lancamento> findLancamentoPorUsuario(Integer id_usuario);

}