package service;

import java.util.Date;
import java.util.List;

import model.ItemObjetivo;
import model.Lancamento;
import model.Objetivo;
import repository.ItemObjetivoRepository;
import repository.LancamentoRepository;
import repository.ObjetivoRepository;
import util.Response;
import util.Uteis;

public class ObjetivoService {

	ObjetivoRepository repo;
	ItemObjetivoRepository itemRepo;
	LancamentoRepository lancamentoRepo;

	public ObjetivoService() {
		this.repo = new ObjetivoRepository();
		this.itemRepo = new ItemObjetivoRepository();
		this.lancamentoRepo = new LancamentoRepository();
	}

	public List<Objetivo> getAll() {
		return this.repo.getAll();
	}

	public Response save(String descricao, String valor, Date ini, Date quando, String cor) {
		Response resp = new Response(false, "Erro não conhecido.");

		if (descricao == null || descricao.equals("")) {
			resp.setMessage("Descrição invalida.");
			return resp;
		}

		if (valor == null || valor.isEmpty() || (!Uteis.isNumeric(valor))) {
			resp.setMessage("Valor invalido.");
			return resp;
		}

		if (ini == null) {
			resp.setMessage("Data inicial inválida.");
			return resp;
		}
		if (quando == null) {
			resp.setMessage("Data Quando inválida.");
			return resp;
		}

		if (cor == null) {
			resp.setMessage("Cor inválida.");
			return resp;
		}

		if (quando.before(ini)) {
			resp.setMessage("Data Quando não pode ser menor que hoje.");
			return resp;
		}

		Double valorDouble = new Double(valor);

		Objetivo objetivo = new Objetivo(descricao, valorDouble, ini, quando, cor);
		Objetivo objSaved = this.repo.save(objetivo);

		if (objSaved != null) {
			resp.setSuccess(true);
			resp.setMessage("Salvo com sucesso!");
		}

		return resp;
	}

	public Response update(String id, String descricao, String valor, Date ini, Date quando, String cor) {
		Response resp = new Response(false, "Erro não conhecido.");

		if (id == null || id.isEmpty()) {
			resp.setMessage("ID inválido.");
			return resp;
		}

		if (descricao == null || descricao.equals("")) {
			resp.setMessage("Descrição invalida.");
			return resp;
		}

		if (valor == null || valor.isEmpty() || (!Uteis.isNumeric(valor))) {
			resp.setMessage("Valor invalido.");
			return resp;
		}

		if (ini == null) {
			resp.setMessage("Data inicial inválida.");
			return resp;
		}
		if (quando == null) {
			resp.setMessage("Data Quando inválida.");
			return resp;
		}

		if (cor == null) {
			resp.setMessage("Cor inválida.");
			return resp;
		}

		if (quando.before(ini)) {
			resp.setMessage("Data Quando não pode ser menor que hoje.");
			return resp;
		}

		Integer idInteger = new Integer(id);
		Double valorDouble = new Double(valor);

		Objetivo objetivo = new Objetivo(idInteger, descricao, valorDouble, ini, quando, cor);
		Objetivo objSaved = this.repo.save(objetivo);

		if (objSaved != null) {
			resp.setSuccess(true);
			resp.setMessage("Salvo com sucesso!");
		}
		return resp;
	}

	public Response removeById(Integer id) {
		Response resp = new Response(false, "Erro ao remover");

		Objetivo objetivo = this.repo.findById(id);

		if (objetivo == null) {
			resp.setMessage("Não encontrado!");
			return resp;
		}

		if (objetivo.getLancamentos() != null && !objetivo.getLancamentos().isEmpty()) {
			resp.setMessage("Não é possível remover, existem lançamentos vinculados a este registro");
			return resp;
		}

		if (objetivo.getItems() != null && !objetivo.getItems().isEmpty()) {
			resp.setMessage("Não é possível remover, existem Despesas vinculadas a este registro");
			return resp;
		}

		if (this.repo.remove(objetivo)) {
			resp.setSuccess(true);
			resp.setMessage("Removido com sucesso!");
		}

		return resp;
	}

	public Response saveItem(ItemObjetivo item) {

		Response resp = new Response(false, "Erro não conhcido.");

		if (item.getDescricao() == null) {
			resp.setMessage("Descrição inválida.");
			return resp;
		}

		if (item.getObjetivo() == null) {
			resp.setMessage("Objetivo inválido.");
			return resp;
		}

		if (item.getValor() == null) {
			resp.setMessage("Valor inválido.");
			return resp;
		}

		ItemObjetivo itemSaved = this.itemRepo.save(item);

		if (itemSaved != null) {
			resp.setMessage("Item salvo.");
			resp.setSuccess(true);
			return resp;
		}

		return resp;

	}

	public Response removeItemById(Integer id) {
		Response resp = new Response(false, "Erro ao remover.");

		ItemObjetivo item = this.itemRepo.findById(id);

		if (item == null) {
			resp.setMessage("Não encontrado!");
			return resp;
		}

		if (this.itemRepo.remove(item)) {
			resp.setSuccess(true);
			resp.setMessage("Removido com sucesso!");
		}

		return resp;

	}

	public List<ItemObjetivo> getAllItens() {
		return this.itemRepo.getAll();
	}

	public Response saveLancamento(Lancamento lancamento) {

		Response resp = new Response(false, "Erro não conhcido.");

		if (lancamento.getTipoInvestimento() == null) {
			resp.setMessage("Tipo investimento inválido.");
			return resp;
		}

		if (!(lancamento.getTipoInvestimento().equals("FIXO") || lancamento.getTipoInvestimento().equals("VARIAVEL"))) {
			resp.setMessage("Tipo investimento inválido.");
			return resp;
		}

		if (lancamento.getData() == null) {
			resp.setMessage("Data inválida.");
			return resp;
		}

		if (lancamento.getObjetivo() == null) {
			resp.setMessage("Objetivo inválido.");
			return resp;
		}

		if (lancamento.getCategoria() == null) {
			resp.setMessage("Categoria inválida.");
			return resp;
		}

		if (lancamento.getOrcamento() == null) {
			resp.setMessage("Orçamento inválido.");
			return resp;
		}

		if ((!(lancamento.getData().after(lancamento.getOrcamento().getDataInicial())
				& lancamento.getData().before(lancamento.getOrcamento().getDataFinal())))
				& !(lancamento.getData().equals(lancamento.getOrcamento().getDataInicial())
						|| lancamento.getData().equals(lancamento.getOrcamento().getDataFinal()))) {
			resp.setMessage("Rendimento fora do período do orçamento.\nInserir uma data entre "
					+ lancamento.getOrcamento().getDataInicial().toString() + "\n e "
					+ lancamento.getOrcamento().getDataFinal().toString() + ".");
			return resp;
		}

		if (lancamento.getValor() == null) {
			resp.setMessage("Valor inválido.");
			return resp;
		}

		Lancamento lanSaved = this.lancamentoRepo.save(lancamento);

		if (lanSaved != null) {
			resp.setMessage("Lançamento salvo com sucesso!");
			resp.setSuccess(true);
		}

		return resp;
	}

	public Response removeLancamentoById(Integer id) {
		Response resp = new Response(false, "Erro ao remover.");

		Lancamento lancamento = this.lancamentoRepo.findById(id);

		if (lancamento == null) {
			resp.setMessage("Não encontrado!");
			return resp;
		}

		if (this.lancamentoRepo.remove(lancamento)) {
			resp.setSuccess(true);
			resp.setMessage("Removido com sucesso!");
		}

		return resp;

	}

	public List<Lancamento> getAllLancamentos() {
		return this.lancamentoRepo.getAll();
	}
}
