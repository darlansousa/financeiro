package util;

public class BalancoItem {
	
	private String grupo;
	private Double investido;
	private Double reequilibrar;
	
	public BalancoItem(String grupo, Double investido, Double reequilibrar) {
		super();
		this.grupo = grupo;
		this.investido = investido;
		this.reequilibrar = reequilibrar;
	}

	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public Double getInvestido() {
		return investido;
	}
	public void setInvestido(Double investido) {
		this.investido = investido;
	}
	public Double getReequilibrar() {
		return reequilibrar;
	}
	public void setReequilibrar(Double reequilibrar) {
		this.reequilibrar = reequilibrar;
	}
	
	
	

}
