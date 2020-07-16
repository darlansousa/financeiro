package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "RENDIMENTO")
public class Rendimento implements Serializable{

	/**
	 * ID, DATA, VALOR, DESCRICAO, ID_CATEGORIA,ID_ORCAMENTO, RECEBIDO
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "DESCRICAO")
	private String descricao;
	@Column(name = "VALOR")
	private Double valor;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA")
	private Date data;
	@Column(name ="RECEBIDO")
	private String recebido;
	@JoinColumn(name = "ID_CATEGORIA", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Categoria categoria;
	@JoinColumn(name = "ID_ORCAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Orcamento orcamento;
	
	public Rendimento() {
		
	}
	public Rendimento(Integer id, String descricao, Double valor, Date data, String recebido, Categoria categoria, Orcamento orcamento) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
		this.recebido = recebido;
		this.categoria = categoria;
		this.orcamento = orcamento;
	}
	
	public Rendimento(String descricao, Double valor, Date data, String recebido, Categoria categoria, Orcamento orcamento) {
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
		this.recebido = recebido;
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
	public String getRecebido() {
		return recebido;
	}
	public void setRecebido(String recebido) {
		this.recebido = recebido;
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

}
