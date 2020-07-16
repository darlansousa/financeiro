package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
@Entity
@Table(name = "OBJETIVO")
public class Objetivo implements Serializable{

	
	/**
	 * ID, DESCRICAO,VALOR,DATA_INI,QUANDO,COR
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
	@Column(name = "DATA_INI")
	private Date dataInicial;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "QUANDO")
	private Date quando;
	@Column(name = "COR")
	private String cor;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "objetivo", fetch = FetchType.LAZY)
	private List<ItemObjetivo> items;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "objetivo", fetch = FetchType.LAZY)
	private List<Lancamento> lancamentos;
	
	
	public Objetivo() {
		
	}
	
	public Objetivo(String descricao, Double valor, Date dataInicial, Date quando, String cor) {
		this.descricao = descricao;
		this.valor = valor;
		this.dataInicial = dataInicial;
		this.quando = quando;
		this.cor = cor;
	}
	
	public Objetivo(Integer id, String descricao, Double valor, Date dataInicial, Date quando, String cor) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.dataInicial = dataInicial;
		this.quando = quando;
		this.cor = cor;
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
	public Date getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	public Date getQuando() {
		return quando;
	}
	public void setQuando(Date quando) {
		this.quando = quando;
	}
	public String getCor() {
		return cor;
	}
	public void setCor(String cor) {
		this.cor = cor;
	}
	
	public List<ItemObjetivo> getItems() {
		return items;
	}

	public void setItems(List<ItemObjetivo> items) {
		this.items = items;
	}
	
	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
	@Transient
	public Double getPlanejado() {
		if(this.getMeses() > 0)
		return this.getValor() / this.getMeses();
		
		return this.getValor();
	}
	
	@Transient
	public Long getMeses() {
		
		LocalDate quando =  this.getQuando().toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
		LocalDate ini = this.getDataInicial().toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate();
		
		Long meses = (Long) ChronoUnit.MONTHS.between(ini, quando);
		
		if(meses < 0)
			return new Long(0);

        return meses;

	}
	
	@Transient
	public Double getTotalOrcado() {
		if(this.items != null && !this.items.isEmpty())
			return this.items.stream().map(i -> i.getValor()).reduce((v1,v2) -> v1 +v2).orElse(this.valor);
		return this.valor;
	}
	
	@Transient
	public Double getTotalAplicado() {
		if(this.lancamentos != null && !this.lancamentos.isEmpty())
			return this.lancamentos.stream().map(l -> l.getValor()).reduce((v1,v2) -> v1 + v2).orElse(new Double(0));
		return new Double(0);
	}
	
	@Transient
	public Double getPercentualAtingido() {
		return this.getTotalAplicado() / this.getTotalOrcado();
	}

	@Override
	public String toString() {
		return descricao;
	}
}
