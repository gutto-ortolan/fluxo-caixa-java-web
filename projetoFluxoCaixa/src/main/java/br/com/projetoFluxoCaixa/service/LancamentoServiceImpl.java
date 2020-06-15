
package br.com.projetoFluxoCaixa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projetoFluxoCaixa.model.Lancamento;
import br.com.projetoFluxoCaixa.repository.LancamentoRepository;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	@Autowired
	LancamentoRepository lr;


	@Override
	public Lancamento findById(Integer id) {
		return lr.findById(id).get();
	}

	@Override
	public Lancamento save(Lancamento lancamento) {
		return lr.save(lancamento);

	}

	@Override
	public List<Lancamento> findAll() {
		return (List<Lancamento>) lr.findAll();
	}

	@Override
	public List<Lancamento> findLancamentoPorUsuario(Integer id_usuario) {
		return (List<Lancamento>)lr.findLancamentoPorUsuario(id_usuario);
	}
	
}