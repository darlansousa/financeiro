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
@Table(name = "ORCAMENTO")
public class Orcamento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// ID, DESCRICAO, DATA INI, DATA FIM
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "DESCRICAO")
	private String descricao;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_INI")
	private Date dataInicial;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_FIM")
	private Date dataFinal;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "orcamento", fetch = FetchType.LAZY)
	private List<Rendimento> rendimentos;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "orcamento", fetch = FetchType.LAZY)
	private List<Gasto> gastos;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "orcamento", fetch = FetchType.LAZY)
	private List<Lancamento> lancamentos;

	public Orcamento(Integer id, String descricao, Date dataInicial, Date dataFinal) {
		this.id = id;
		this.descricao = descricao;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
	}

	public Orcamento(String descricao, Date dataInicial, Date dataFinal) {
		this.descricao = descricao;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
	}

	public Orcamento() {

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

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<Rendimento> getRendimentos() {
		return rendimentos;
	}

	public void setRendimentos(List<Rendimento> rendimentos) {
		this.rendimentos = rendimentos;
	}

	@Override
	public String toString() {
		return descricao;
	}

	public List<Gasto> getGastos() {
		return gastos;
	}

	public void setGastos(List<Gasto> gastos) {
		this.gastos = gastos;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	@Transient
	public Long getDias() {
		LocalDate fim = this.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate ini = this.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Long dias = (Long) ChronoUnit.DAYS.between(ini, fim);
		if (dias < 0)
			return new Long(0);

		return dias;

	}

}
