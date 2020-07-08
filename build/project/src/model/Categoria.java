package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "CATEGORIA")
public class Categoria implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "DESCRICAO")
	private String descricao;
	@JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Grupo grupo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria", fetch = FetchType.LAZY)
	private List<Rendimento> rendimentos;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria", fetch = FetchType.LAZY)
	private List<Lancamento> lancamentos;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria", fetch = FetchType.LAZY)
	private List<Gasto> gastos;
	
	
	public Categoria() {
		
	}

	public Categoria(String descricao, Grupo grupo, List<Rendimento> rendimentos, List<Lancamento> lancamentos,
			List<Gasto> gastos) {
		super();
		this.descricao = descricao;
		this.grupo = grupo;
		this.rendimentos = rendimentos;
		this.lancamentos = lancamentos;
		this.gastos = gastos;
	}
	
	public Categoria(Integer id, String descricao, Grupo grupo, List<Rendimento> rendimentos,
			List<Lancamento> lancamentos, List<Gasto> gastos) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.grupo = grupo;
		this.rendimentos = rendimentos;
		this.lancamentos = lancamentos;
		this.gastos = gastos;
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
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
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
	
	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}
	
	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
	public List<Gasto> getGastos() {
		return gastos;
	}
	
	public void setGastos(List<Gasto> gastos) {
		this.gastos = gastos;
	}
	
	
	@Transient
	public Double getTotalGastosByOrcamento(Orcamento orcamento) {
		if(this.gastos != null && !this.gastos.isEmpty())
			return this.gastos.stream()
					.filter(g -> g.getOrcamento().getId().equals(orcamento.getId()))
					.map(g -> g.getValor()).reduce((v1,v2) -> v1 +v2).orElse(new Double(0));
		
		return new Double(0);
	}
}
