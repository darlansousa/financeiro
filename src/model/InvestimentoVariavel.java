package model;

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
import javax.persistence.Transient;

@Entity
@Table(name = "INV_VARIAVEL")
public class InvestimentoVariavel implements Serializable{

	/**
	 * ID, ATIVO, DESCRICAO, QUANTIDADE, VALOR_UNIDADE,VALOR_UNIDADE_ATUAL,  DATA_COMPRA, DATA_VENDA, VALOR_VENDA, DATA_ATUALIZACAO, VENDIDO, ID_GRUPO
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "ATIVO")
	private String ativo;
	@Column(name = "DESCRICAO")
	private String descricao;
	@Column(name = "QUANTIDADE")
	private Integer quantidade;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_COMPRA")
	private Date dataCompra;
	@Column(name = "VALOR_UNIDADE")
	private Double valorUnidade;
	@Column(name = "VALOR_UNIDADE_ATUAL")
	private Double valorUnidadeAtual;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_VENDA")
	private Date dataVenda;
	@Column(name = "VALOR_VENDA")
	private Double valorVenda;
	@Column(name = "DATA_ATUALIZACAO")
	private String dataAtualizacao;
	@Column(name = "VENDIDO")
	private String vendido;
	@JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Grupo grupo;
	
	public InvestimentoVariavel() {
		
	}
	
	public InvestimentoVariavel(Integer id, String ativo, String descricao, Integer quantidade, Date dataCompra,
			Double valorUnidade, Double valorUnidadeAtual, Date dataVenda, Double valorVenda, String dataAtualizacao,
			String vendido, Grupo grupo) {
		this.id = id;
		this.ativo = ativo;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.dataCompra = dataCompra;
		this.valorUnidade = valorUnidade;
		this.valorUnidadeAtual = valorUnidadeAtual;
		this.dataVenda = dataVenda;
		this.valorVenda = valorVenda;
		this.dataAtualizacao = dataAtualizacao;
		this.vendido = vendido;
		this.grupo = grupo;
	}

	public InvestimentoVariavel(String ativo, String descricao, Integer quantidade, Date dataCompra,
			Double valorUnidade, Double valorUnidadeAtual, Date dataVenda, Double valorVenda, String dataAtualizacao,
			String vendido, Grupo grupo) {
		this.ativo = ativo;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.dataCompra = dataCompra;
		this.valorUnidade = valorUnidade;
		this.valorUnidadeAtual = valorUnidadeAtual;
		this.dataVenda = dataVenda;
		this.valorVenda = valorVenda;
		this.dataAtualizacao = dataAtualizacao;
		this.vendido = vendido;
		this.grupo = grupo;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Date getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public Double getValorUnidade() {
		return valorUnidade;
	}
	public void setValorUnidade(Double valorUnidade) {
		this.valorUnidade = valorUnidade;
	}
	public Double getValorUnidadeAtual() {
		return valorUnidadeAtual;
	}
	public void setValorUnidadeAtual(Double valorUnidadeAtual) {
		this.valorUnidadeAtual = valorUnidadeAtual;
	}
	public Date getDataVenda() {
		return dataVenda;
	}
	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}
	public Double getValorVenda() {
		return valorVenda;
	}
	public void setValorVenda(Double valorVenda) {
		this.valorVenda = valorVenda;
	}
	public String getDataAtualizacao() {
		return dataAtualizacao;
	}
	public void setDataAtualizacao(String dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	public String getVendido() {
		return vendido;
	}
	public void setVendido(String vendido) {
		this.vendido = vendido;
	}
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	@Transient
	public Double getTotal() {
		if(this.vendido.equals("N"))
			return this.valorUnidade * this.quantidade;
		else
			return this.valorVenda;
	}
	@Transient
	public Double getTotalAtual() {
		if(this.vendido.equals("S"))
			return this.valorVenda;
		
		if(this.valorUnidadeAtual != null)
			return this.valorUnidadeAtual * this.quantidade;
		
		return new Double(0);
	}
	
	public Double getRendimento() {
		return getTotalAtual() - getTotal();
	}
	
}
