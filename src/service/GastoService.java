package service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import model.Categoria;
import model.Gasto;
import model.Orcamento;
import repository.CategoriaRepository;
import repository.GastoRepository;
import repository.OrcamentoRepository;
import util.Response;
import util.Uteis;

public class GastoService {

	GastoRepository repo;
	CategoriaRepository categoriaRepo;
	OrcamentoRepository orcamentoRepo;

	public GastoService() {
		this.repo = new GastoRepository();
		this.categoriaRepo = new CategoriaRepository();
		this.orcamentoRepo = new OrcamentoRepository();
	}

	public List<Gasto> getAll() {
		return this.repo.getAll();
	}

	public Response save(String descricao, String valor,  LocalDate data, String status, String cartao,
			Integer idCategoria, Integer idOrcamento) {
		Response resp = new Response(false, "Erro não conhecido.");
		ZoneId defaultZoneId = ZoneId.systemDefault();

		if (descricao == null || descricao.isEmpty()) {
			resp.setMessage("Descrição inválida.");
			return resp;
		}

		if (valor == null || descricao.isEmpty() || (!Uteis.isNumeric(valor))) {
			resp.setMessage("Valor inválido.");
			return resp;
		}

		if (data == null) {
			resp.setMessage("Data inválida.");
			return resp;
		}

		Date dataGasto = Date.from(data.atStartOfDay(defaultZoneId).toInstant());

		if (status == null || status.isEmpty()) {
			resp.setMessage("Status invalido.");
			return resp;
		}

		if (!(status.equals("CONFIRMADO") || status.equals("BAIXADO") || status.equals("PENDENTE"))) {
			resp.setMessage("Status incorreto.");
			return resp;
		}

		if (idCategoria == null) {
			resp.setMessage("Categoria inválida.");
			return resp;
		}

		Categoria categoria = this.categoriaRepo.findById(idCategoria);

		if (categoria == null) {
			resp.setMessage("Categoria não encontrada.");
			return resp;
		}

		if (idOrcamento == null) {
			resp.setMessage("Orcamento inválido.");
			return resp;
		}

		Orcamento orcamento = this.orcamentoRepo.findById(idOrcamento);

		if (orcamento == null) {
			resp.setMessage("Orcamento não encontrado.");
			return resp;
		}

		if ((!(dataGasto.after(orcamento.getDataInicial()) & dataGasto.before(orcamento.getDataFinal())))
				& !(dataGasto.equals(orcamento.getDataInicial()) || dataGasto.equals(orcamento.getDataFinal()))) {
			resp.setMessage("Rendimento fora do período do orçamento.\nInserir uma data entre "
					+ orcamento.getDataInicial().toString() + "\n e " + orcamento.getDataFinal().toString() + ".");
			return resp;
		}

		Gasto gasto = new Gasto(descricao, new Double(valor), dataGasto, status, cartao, categoria, orcamento);
		Gasto gastoSaved = this.save(gasto);

		if (gastoSaved != null) {
			resp.setSuccess(true);
			resp.setMessage("Salvo com sucesso!");
			return resp;
		}

		return resp;

	}

	public Response update(Integer id, String descricao, String valor, LocalDate data, String status, String cartao,
			Integer idCategoria, Integer idOrcamento) {
		Response resp = new Response(false, "Erro não conhecido.");
		ZoneId defaultZoneId = ZoneId.systemDefault();

		if (id == null) {
			resp.setMessage("ID inválido.");
			return resp;
		}

		if (descricao == null || descricao.isEmpty()) {
			resp.setMessage("Descrição inválida.");
			return resp;
		}

		if (valor == null || valor.isEmpty() || (!Uteis.isNumeric(valor))) {
			resp.setMessage("Valor inválido.");
			return resp;
		}

		if (data == null) {
			resp.setMessage("Data inválida.");
			return resp;
		}

		Date dataGasto = Date.from(data.atStartOfDay(defaultZoneId).toInstant());

		if (status == null || status.isEmpty()) {
			resp.setMessage("Status invalido.");
			return resp;
		}

		if (!(status.equals("CONFIRMADO") || status.equals("BAIXADO") || status.equals("PENDENTE"))) {
			resp.setMessage("Status incorreto.");
			return resp;
		}

		if (idCategoria == null) {
			resp.setMessage("Categoria inválida.");
			return resp;
		}

		Categoria categoria = this.categoriaRepo.findById(idCategoria);

		if (categoria == null) {
			resp.setMessage("Categoria não encontrada.");
			return resp;
		}

		if (idOrcamento == null) {
			resp.setMessage("Orcamento inválido.");
			return resp;
		}

		Orcamento orcamento = this.orcamentoRepo.findById(idOrcamento);

		if (orcamento == null) {
			resp.setMessage("Orcamento não encontrado.");
			return resp;
		}

		if ((!(dataGasto.after(orcamento.getDataInicial()) & dataGasto.before(orcamento.getDataFinal())))
				& !(dataGasto.equals(orcamento.getDataInicial()) || dataGasto.equals(orcamento.getDataFinal()))) {
			resp.setMessage("Rendimento fora do período do orçamento.\nInserir uma data entre "
					+ orcamento.getDataInicial().toString() + "\n e " + orcamento.getDataFinal().toString() + ".");
			return resp;
		}

		Gasto gasto = new Gasto(id, descricao, new Double(valor), dataGasto, status, cartao, categoria, orcamento);
		Gasto gastoSaved = this.save(gasto);

		if (gastoSaved != null) {
			resp.setSuccess(true);
			resp.setMessage("Salvo com sucesso!");
			return resp;
		}

		return resp;

	}

	public Gasto save(Gasto gasto) {
		Gasto gastoSaved = this.repo.save(gasto);
		return gastoSaved;
	}

	public Response removeById(Integer id) {
		Response resp = new Response(false, "Erro ao remover");

		Gasto gasto = this.repo.findById(id);

		if (gasto == null) {
			resp.setMessage("Não encontrado!");
			return resp;
		}

		if (this.repo.remove(gasto)) {
			resp.setSuccess(true);
			resp.setMessage("Removido com sucesso!");
		}

		return resp;
	}

}
