package service;

import java.util.Date;
import java.util.List;

import model.Grupo;
import model.InvestimentoVariavel;
import repository.GrupoRepository;
import repository.InvestimentoVariavelRepository;
import util.Response;
import util.Uteis;

public class InvestimentoVariavelService {

	InvestimentoVariavelRepository repo;
	GrupoRepository grupoRepo;

	public InvestimentoVariavelService() {
		this.repo = new InvestimentoVariavelRepository();
		this.grupoRepo = new GrupoRepository();
	}

	public List<InvestimentoVariavel> getAll() {
		return this.repo.getAll();
	}

	public Response update(Integer id, String ativo, String descricao, String quantidade, Date dataCompra,
			String valorUnidade, Date dataVenda, Double valorVenda, String vendido,Double valorAtual, String dataAtualizacao, Integer idGrupo) {
		Response resp = new Response(false, "Erro desconhecido.");

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

		if (valorUnidade == null || valorUnidade.isEmpty() || (!Uteis.isNumeric(valorUnidade))) {
			resp.setMessage("Valor unidade inválido.");
			return resp;
		}

		if (vendido == null) {
			resp.setMessage("Vandido inválido.");
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

		if (vendido.equals("Sim")) {
			vendido = "S";
		} else {
			vendido = "N";
		}

		InvestimentoVariavel investimento = new InvestimentoVariavel(id,ativo, descricao, new Integer(quantidade), dataCompra,
				new Double(valorUnidade), null, dataVenda, valorVenda, null, vendido, grupo);
		
		if(dataAtualizacao != null)
			investimento.setDataAtualizacao(dataAtualizacao);
		
		if(valorAtual != null)
			investimento.setValorUnidadeAtual(valorAtual);

		InvestimentoVariavel invSaved = this.repo.save(investimento);

		if (invSaved != null) {
			resp.setMessage("Salvo com sucesso!");
			resp.setSuccess(true);
			return resp;
		}

		return resp;

	}

	public Response save(String ativo, String descricao, String quantidade, Date dataCompra, String valorUnidade,
			Date dataVenda, Double valorVenda, String vendido, Integer idGrupo) {

		Response resp = new Response(false, "Erro desconhecido.");

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

		if (valorUnidade == null || valorUnidade.isEmpty() || (!Uteis.isNumeric(valorUnidade))) {
			resp.setMessage("Valor unidade inválido.");
			return resp;
		}

		if (vendido == null) {
			resp.setMessage("Vandido inválido.");
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

		if (vendido.equals("Sim")) {
			vendido = "S";
		} else {
			vendido = "N";
		}

		InvestimentoVariavel investimento = new InvestimentoVariavel(ativo, descricao, new Integer(quantidade), dataCompra,
				new Double(valorUnidade), null, dataVenda, valorVenda, null, vendido, grupo);

		InvestimentoVariavel invSaved = this.repo.save(investimento);

		if (invSaved != null) {
			resp.setMessage("Salvo com sucesso!");
			resp.setSuccess(true);
			return resp;
		}

		return resp;

	}
	
	public void save(InvestimentoVariavel iv) {
		this.repo.save(iv);
	}

	public Response removeById(Integer id) {
		Response resp = new Response(false, "Erro ao remover");

		InvestimentoVariavel investimento = this.repo.findById(id);

		if (investimento == null) {
			resp.setMessage("Não encontrado!");
			return resp;
		}

		if (this.repo.remove(investimento)) {
			resp.setSuccess(true);
			resp.setMessage("Removido com sucesso!");
		}

		return resp;
	}
}
