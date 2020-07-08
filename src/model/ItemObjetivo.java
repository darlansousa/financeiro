package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ITEM_OBJETIVO")
public class ItemObjetivo implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "DESCRICAO")
	private String descricao;
	@Column(name = "VALOR")
	private Double valor;
	@JoinColumn(name = "ID_OBJETIVO", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Objetivo objetivo;
	
	public ItemObjetivo() {
		
	}
	public ItemObjetivo(String descricao, Double valor, Objetivo objetivo) {
		this.descricao = descricao;
		this.valor = valor;
		this.objetivo = objetivo;
	}
	public ItemObjetivo(Integer id, String descricao, Double valor, Objetivo objetivo) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.objetivo = objetivo;
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
	public Objetivo getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(Objetivo objetivo) {
		this.objetivo = objetivo;
	}

}
