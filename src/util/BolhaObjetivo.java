package util;

public class BolhaObjetivo {
	
	private String objetivo;
	private Double valorAtingido;
	private Double percentual;
	private Double valorTotal;

	public BolhaObjetivo(String objetivo, Double valorAtingido, Double percentual, Double valorTotal) {
		super();
		this.objetivo = objetivo;
		this.valorAtingido = valorAtingido;
		this.percentual = percentual;
		this.valorTotal = valorTotal;
	}

	public String getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}
	public Double getValorAtingido() {
		return valorAtingido;
	}
	public void setValorAtingido(Double valorAtingido) {
		this.valorAtingido = valorAtingido;
	}
	public Double getPercentual() {
		return percentual;
	}
	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}
	public Double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

}
