package util;

public class DespesaItem {


	private String grupo;
	private Double planejado;
	private Double real;
	
	public DespesaItem(String grupo, Double planejado, Double real) {
		this.grupo = grupo;
		this.planejado = planejado;
		this.real = real;
	}
	
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public Double getPlanejado() {
		return planejado;
	}
	public void setPlanejado(Double planejado) {
		this.planejado = planejado;
	}
	public Double getReal() {
		return real;
	}
	public void setReal(Double real) {
		this.real = real;
	}
	
	public Double getDiferenca() {
		return this.planejado - this.real;
	}
	

}
