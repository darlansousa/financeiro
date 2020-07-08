package util;

public class Investimento {
	
	public Investimento(Integer id, String descricao) {
		super();
		this.id = id;
		this.descricao = descricao;
	}
	private Integer id;
	private String descricao;
	
	
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
	@Override
	public String toString() {
		return descricao;
	}
	
	

}
