package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "GRUPO")
public class Grupo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// ID, DESCRICAO, PERCENTUAL, TIPO, COR
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "DESCRICAO")
	private String descricao;
	@Column(name = "PERCENTUAL")
	private Double percentual;
	@Column(name = "TIPO")
	private String tipo;
	@Column(name = "COR")
	private String cor;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "grupo", fetch = FetchType.LAZY)
	private List<Categoria> categorias;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "grupo", fetch = FetchType.LAZY)
	private List<InvestimentoFixo> investimentosFixos;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "grupo", fetch = FetchType.LAZY)
	private List<InvestimentoVariavel> investimentosVariaveis;

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

	public Double getPercentual() {
		return percentual;
	}

	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	@Override
	public String toString() {
		return descricao;
	}

	@Transient
	public Double getTotalInvestido() {
		if (this.investimentosFixos != null && !this.investimentosFixos.isEmpty()) {
			Double valor = this.investimentosFixos.stream().filter(ivf -> ivf.getResgatado().equals("N"))
					.map(ivf -> ivf.getValorLiquido()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0));
			return valor;
		}

		if (this.investimentosVariaveis != null && !this.investimentosVariaveis.isEmpty()) {
			Double val = this.investimentosVariaveis.stream().filter(ivv -> ivv.getVendido().equals("N"))
					.map(ivv -> ivv.getTotalAtual()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0));
			return val;
		}

		return new Double(0);
	}

	@Transient
	public Double getTotalLancamentosByOrcamento(Orcamento orcamento) {

		List<Lancamento> lancamentos = new ArrayList<Lancamento>();
		if (this.categorias != null && !this.categorias.isEmpty()) {
			this.categorias.stream().forEach(c -> lancamentos.addAll(c.getLancamentos()
					.stream()
					.filter(l -> l.getOrcamento().getId().equals(orcamento.getId()))
					.collect(Collectors.toList())
					));

			return lancamentos.stream().map(l -> l.getValor()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0));
		}

		return new Double(0);
	}
	
	
	@Transient
	public Double getTotalGastosByOrcamento(Orcamento orcamento) {

		List<Gasto> gastos = new ArrayList<Gasto>();
		if (this.categorias != null && !this.categorias.isEmpty()) {
			this.categorias.stream()
			.forEach(c -> gastos.addAll(c.getGastos().stream()
					.filter(g -> g.getStatus().equals("BAIXADO"))
					.filter(l -> l.getOrcamento().getId().equals(orcamento.getId()))
					.collect(Collectors.toList())));

			return gastos.stream().map(l -> l.getValor()).reduce((v1, v2) -> v1 + v2).orElse(new Double(0));
		}

		return new Double(0);
	}


	@Transient
	public Boolean isInvestimento() {
		return this.descricao.equals("Objetivos médio/curto prazo") || this.descricao.equals("Aposentadoria");
	}

	@Transient
	public Boolean isFixo() {
		return this.descricao.equals("CAIXA");
	}

	@Transient
	public Boolean isVariavel() {
		return this.descricao.equals("AÇÕES BR") || this.descricao.equals("AÇÕES EUA")
				|| this.descricao.equals("FUNDOS IMOBILIÁRIOS");
	}

	@Transient
	public Boolean isDespesa() {
		return this.descricao.equals("Essencial") || this.descricao.equals("Educação")
				|| this.descricao.equals("Com o que quiser");
	}

	@Transient
	public Boolean isRenda() {
		return this.descricao.equals("Renda");
	}

}
