package model;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import util.Investimento;

@Entity
@Table(name = "LANCAMENTO")
public class Lancamento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "TIPO_INV")
	private String tipoInvestimento;
	@Column(name = "ID_INVESTIMENTO")
	private Integer idInvestimento;
	@Column(name = "DESCRICAO_INV")
	private String descricaoInv;
	@Column(name = "VALOR")
	private Double valor;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA")
	private Date data;
	@JoinColumn(name = "ID_OBJETIVO", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Objetivo objetivo;
	@JoinColumn(name = "ID_CATEGORIA", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Categoria categoria;
	@JoinColumn(name = "ID_ORCAMENTO", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Orcamento orcamento;

	public Lancamento() {
		super();
	}

	public Lancamento(String tipoInvestimento, Integer idInvestimento, Double valor, Date data, Objetivo objetivo,
			Categoria categoria, Orcamento orcamento) {
		super();
		this.tipoInvestimento = tipoInvestimento;
		this.idInvestimento = idInvestimento;
		this.valor = valor;
		this.data = data;
		this.objetivo = objetivo;
		this.categoria = categoria;
		this.orcamento = orcamento;
	}

	public Lancamento(Integer id, String tipoInvestimento, Integer idInvestimento, Double valor, Date data,
			Objetivo objetivo, Categoria categoria, Orcamento orcamento) {
		super();
		this.id = id;
		this.tipoInvestimento = tipoInvestimento;
		this.idInvestimento = idInvestimento;
		this.valor = valor;
		this.data = data;
		this.objetivo = objetivo;
		this.categoria = categoria;
		this.orcamento = orcamento;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipoInvestimento() {
		return tipoInvestimento;
	}

	public void setTipoInvestimento(String tipoInvestimento) {
		this.tipoInvestimento = tipoInvestimento;
	}

	public Integer getIdInvestimento() {
		return idInvestimento;
	}

	public void setIdInvestimento(Integer idInvestimento) {
		this.idInvestimento = idInvestimento;
	}
	
	public String getDescricaoInv() {
		return descricaoInv;
	}

	public void setDescricaoInv(String descricaoInv) {
		this.descricaoInv = descricaoInv;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Objetivo getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(Objetivo objetivo) {
		this.objetivo = objetivo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Transient
	public Investimento getInvestimento() {
		if(this.idInvestimento == null)
			return null;
		return new Investimento(this.idInvestimento, this.descricaoInv);
	}
}
