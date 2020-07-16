package util;

public class InvestimentoPlan {
	
	public InvestimentoPlan(String investimento, Double percentual) {
		super();
		this.investimento = investimento;
		this.percentual = percentual;
	}
	private String investimento;
	private Double percentual;
	
	public String getInvestimento() {
		return investimento;
	}
	public void setInvestimento(String investimento) {
		this.investimento = investimento;
	}
	public Double getPercentual() {
		return percentual;
	}
	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}
	
	

}
