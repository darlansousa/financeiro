package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
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
@Table(name = "GASTO")
public class Gasto implements Serializable{

	
	/**
	 * DATA, VALOR, DESCRICAO, ID_CATEGORIA, STATUS(BAIXADO,CONFIRMADO,PENDENTE), ID_ORCAMENTO
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "DESCRICAO")
	private String descricao;
	@Column(name = "VALOR")
	private Double valor;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA")
	private Date data;
	@Column(name ="STATUS")
	private String status;
	@Column(name ="CARTAO")
	private String cartao;
	@JoinColumn(name = "ID_CATEGORIA", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Categoria categoria;
	@JoinColumn(name = "ID_ORCAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Orcamento orcamento;
	
	
	public Gasto() {
		
	}
	public Gasto(Integer id, String descricao, Double valor, Date data, String status, String cartao,
			Categoria categoria, Orcamento orcamento) {
		
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
		this.status = status;
		this.cartao = cartao;
		this.categoria = categoria;
		this.orcamento = orcamento;
	}
	public Gasto(String descricao, Double valor, Date data, String status, String cartao, Categoria categoria,
			Orcamento orcamento) {
		
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
		this.status = status;
		this.cartao = cartao;
		this.categoria = categoria;
		this.orcamento = orcamento;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCartao() {
		return cartao;
	}
	public void setCartao(String cartao) {
		this.cartao = cartao;
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
	
	@Transient
	public LocalDate getDataToLocalDate() {
		if(this.data != null)
			return this.data.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate();
		
		return null;
	}
}
