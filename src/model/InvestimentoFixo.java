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
@Table(name = "INV_FIXO")
public class InvestimentoFixo implements Serializable {

	/**
	 * ATIVO, DESCRICAO, QUANTIDADE, VENCIMENTO, VALOR_APLICADO, VALOR_LIQUIDO,
	 * VALOR_RESGATE, RESGATADO, ID_GRUPO
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
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
	@Column(name = "VALOR_APLICADO")
	private Double valorAplicado;
	@Column(name = "VALOR_LIQUIDO")
	private Double valorLiquido;
	@Column(name = "VALOR_RESGATE")
	private Double valorResgate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VENCIMENTO")
	private Date vencimento;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_RESGATE")
	private Date dataResgate;
	@Column(name = "RESGATADO")
	private String resgatado;
	@JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Grupo grupo;

	public InvestimentoFixo() {

	}

	public InvestimentoFixo(String ativo, String descricao, Integer quantidade, Date dataCompra, Double valorAplicado,
			Double valorLiquido, Double valorResgate, Date dataResgate, Date vencimento, String resgatado,
			Grupo grupo) {
		this.ativo = ativo;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.dataCompra = dataCompra;
		this.valorAplicado = valorAplicado;
		this.valorLiquido = valorLiquido;
		this.valorResgate = valorResgate;
		this.dataResgate = dataResgate;
		this.vencimento = vencimento;
		this.resgatado = resgatado;
		this.grupo = grupo;
	}

	public InvestimentoFixo(Integer id, String ativo, String descricao, Integer quantidade, Date dataCompra,
			Double valorAplicado, Double valorLiquido, Double valorResgate, Date dataResgate, Date vencimento,
			String resgatado, Grupo grupo) {
		this.id = id;
		this.ativo = ativo;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.dataCompra = dataCompra;
		this.valorAplicado = valorAplicado;
		this.valorLiquido = valorLiquido;
		this.valorResgate = valorResgate;
		this.dataResgate = dataResgate;
		this.vencimento = vencimento;
		this.resgatado = resgatado;
		this.grupo = grupo;
	}

	public Double getValorAplicado() {
		return valorAplicado;
	}

	public void setValorAplicado(Double valorAplicado) {
		this.valorAplicado = valorAplicado;
	}

	public Double getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(Double valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public Double getValorResgate() {
		return valorResgate;
	}

	public void setValorResgate(Double valorResgate) {
		this.valorResgate = valorResgate;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

	public String getResgatado() {
		return resgatado;
	}

	public void setResgatado(String resgatado) {
		this.resgatado = resgatado;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
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

	public Date getDataResgate() {
		return dataResgate;
	}

	public void setDataResgate(Date dataResgate) {
		this.dataResgate = dataResgate;
	}
	
	@Transient
	public Double getRendimentos() {
		if(this.resgatado.equals("N"))
			return this.valorLiquido - this.valorAplicado;
		else
			return this.valorResgate - this.valorAplicado;
	}

}
