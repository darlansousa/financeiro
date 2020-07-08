package service;

import java.util.Date;
import java.util.List;

import model.Grupo;
import model.InvestimentoFixo;
import repository.GrupoRepository;
import repository.InvestimentoFixoRepository;
import util.Response;

public class InvestimentoFixoService {
	
	InvestimentoFixoRepository repo;
	GrupoRepository grupoRepo;
	
	public InvestimentoFixoService() {
		this.repo = new InvestimentoFixoRepository();
		this.grupoRepo = new GrupoRepository();
	}
	
	
	public List<InvestimentoFixo> getAll(){
		return this.repo.getAll();
	}
	
	
	
	public Response save(String ativo, String descricao, String quantidade, Date dataCompra, String valorAplicado,
			String valorLiquido, Double valorResgate, Date dataResgate, Date vencimento, String resgatado, Integer idGrupo) {

		Response resp = new Response(false, "Erro ao salvar");
		if (ativo == null) {
			resp.setMessage("Ativo inválido.");
			return resp;
		}
		if (descricao == null) {
			resp.setMessage("Descrição inválida.");
			return resp;
		}

		if (quantidade == null || quantidade.isEmpty()) {
			resp.setMessage("Quantidade inválida.");
			return resp;
		}

		if (dataCompra == null) {
			resp.setMessage("Data compra inválida.");
			return resp;
		}

		if (valorAplicado == null || valorAplicado.isEmpty()) {
			resp.setMessage("Valor Aplicado inválido.");
			return resp;
		}
		
		if (valorLiquido == null || valorLiquido.isEmpty()) {
			resp.setMessage("Valor Liquido inválido.");
			return resp;
		}
		
		if (vencimento == null) {
			resp.setMessage("Vencimento inválido.");
			return resp;
		}

		if (resgatado == null) {
			resp.setMessage("Resgatado inválido.");
			return resp;
		}

		if (idGrupo == null) {
			resp.setMessage("Grupo inválido.");
			return resp;
		}

		Grupo grupo = this.grupoRepo.findById(idGrupo);

		if (grupo == null) {
			resp.setMessage("Grupo não encontrado.");
			return resp;
		}

		if (resgatado.equals("Sim")) {
			resgatado = "S";
		} else {
			resgatado = "N";
		}

		InvestimentoFixo investimento = new InvestimentoFixo(
				ativo,
				descricao,
				new Integer(quantidade),
				dataCompra,
				new Double(valorAplicado),
				new Double(valorLiquido),
				valorResgate,
				dataResgate,
				vencimento,
				resgatado,
				grupo);

		InvestimentoFixo invSaved = this.repo.save(investimento);

		if (invSaved != null) {
			resp.setMessage("Salvo com sucesso!");
			resp.setSuccess(true);
			return resp;
		}

		return resp;

	}
	
	public Response update(Integer id,String ativo, String descricao, String quantidade, Date dataCompra, String valorAplicado,
			String valorLiquido, Double valorResgate, Date dataResgate, Date vencimento, String resgatado, Integer idGrupo) {

		Response resp = new Response(false, "Erro ao salvar");
		if (id == null) {
			resp.setMessage("ID inválido.");
			return resp;
		}
		if (ativo == null) {
			resp.setMessage("Ativo inválido.");
			return resp;
		}
		if (descricao == null) {
			resp.setMessage("Descrição inválida.");
			return resp;
		}

		if (quantidade == null || quantidade.isEmpty()) {
			resp.setMessage("Quantidade inválida.");
			return resp;
		}

		if (dataCompra == null) {
			resp.setMessage("Data compra inválida.");
			return resp;
		}

		if (valorAplicado == null || valorAplicado.isEmpty()) {
			resp.setMessage("Valor Aplicado inválido.");
			return resp;
		}
		
		if (valorLiquido == null || valorLiquido.isEmpty()) {
			resp.setMessage("Valor Liquido inválido.");
			return resp;
		}
		
		if (vencimento == null) {
			resp.setMessage("Vencimento inválido.");
			return resp;
		}

		if (resgatado == null) {
			resp.setMessage("Resgatado inválido.");
			return resp;
		}

		if (idGrupo == null) {
			resp.setMessage("Grupo inválido.");
			return resp;
		}

		Grupo grupo = this.grupoRepo.findById(idGrupo);

		if (grupo == null) {
			resp.setMessage("Grupo não encontrado.");
			return resp;
		}

		if (resgatado.equals("Sim")) {
			resgatado = "S";
		} else {
			resgatado = "N";
		}

		InvestimentoFixo investimento = new InvestimentoFixo(
				id,
				ativo,
				descricao,
				new Integer(quantidade),
				dataCompra,
				new Double(valorAplicado),
				new Double(valorLiquido),
				valorResgate,
				dataResgate,
				vencimento,
				resgatado,
				grupo);

		InvestimentoFixo invSaved = this.repo.save(investimento);

		if (invSaved != null) {
			resp.setMessage("Salvo com sucesso!");
			resp.setSuccess(true);
			return resp;
		}

		return resp;

	}
	
	public Response removeById(Integer id) {
		Response resp =  new Response(false, "Erro ao remover");
		
		InvestimentoFixo investimento = this.repo.findById(id);
		
		if(investimento == null) {
			resp.setMessage("Não encontrado!");
			return resp;
		}
		
		if(this.repo.remove(investimento)) {
			resp.setSuccess(true);
			resp.setMessage("Removido com sucesso!");
		}
		
		return resp;
	}
	

}
