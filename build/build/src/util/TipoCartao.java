package util;

public class TipoCartao {
	
	private String cartao;
	private Double valorTotal;
	
	public TipoCartao(String cartao, Double valorTotal) {
		super();
		this.cartao = cartao;
		this.valorTotal = valorTotal;
	}
	public String getCartao() {
		return cartao;
	}
	public void setCartao(String cartao) {
		this.cartao = cartao;
	}
	public Double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	

}
