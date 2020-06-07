package br.com.projetoFluxoCaixa.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="lancamento")
public class Lancamento implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQLANCAMENTO", sequenceName = "SEQLANCAMENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQLANCAMENTO")
	private Integer idLancamento;
	
	private String descricao;
	
	private String operacao;
	
	private Double valor;
	
	private Date data;
	
	@ManyToOne
    @JoinColumn(name="idUsuario")
    private Usuario usuario;

	public Integer getIdLancamento() {
		return idLancamento;
	}

	public void setIdLancamento(Integer idLancamento) {
		this.idLancamento = idLancamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getData() {		
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");    
		String dataFormatada = fmt.format(this.data);
		return dataFormatada;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	
	
}
