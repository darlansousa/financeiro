package service;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import model.Lancamento;
import model.Orcamento;
import repository.OrcamentoRepository;
import util.Response;

public class OrcamentoService {

	OrcamentoRepository repo;
	ObjetivoService objetivoService;
	RendimentoService rendimentoService;
	GastoService gastoService;

	public OrcamentoService() {
		this.repo = new OrcamentoRepository();
		this.objetivoService = new ObjetivoService();
		this.rendimentoService = new RendimentoService();
		this.gastoService = new GastoService();
	}

	public List<Orcamento> getAll(Boolean clearCash) {
		return this.repo.getAll(clearCash);
	}

	public Response save(String descricao, Date ini, Date fim) {
		Response resp = new Response(false, "Erro não conhecido.");

		if (descricao == null || descricao.equals("")) {
			resp.setMessage("Descrição inválida.");
			return resp;
		}
		if (ini == null) {
			resp.setMessage("Data inicial inválida.");
			return resp;
		}
		if (fim == null) {
			resp.setMessage("Data final inválida.");
			return resp;
		}

		if (fim.before(ini)) {
			resp.setMessage("Período inválido.");
			return resp;
		}

		Orcamento orcamento = new Orcamento(descricao, ini, fim);
		Orcamento orcSaved = this.repo.save(orcamento);

		if (orcSaved != null) {
			resp.setSuccess(true);
			resp.setMessage("Salvo com sucesso!");
			return resp;
		}

		return resp;
	}

	public Response update(Integer id, String descricao, Date ini, Date fim) {
		Response resp = new Response(false, "Erro desconhecido.");

		if (id == null) {
			resp.setMessage("ID inválido.");
			return resp;
		}

		if (descricao == null || descricao.equals("")) {
			resp.setMessage("Descrição inválida.");
			return resp;
		}

		if (ini == null) {
			resp.setMessage("Data inicial inválida.");
			return resp;
		}
		if (fim == null) {
			resp.setMessage("Data final inválida.");
			return resp;
		}

		if (fim.before(ini)) {
			resp.setMessage("Período inválido.");
			return resp;
		}

		Orcamento orcamento = new Orcamento(id, descricao, ini, fim);
		Orcamento orcSaved = this.repo.save(orcamento);

		if (orcSaved != null) {
			resp.setSuccess(true);
			resp.setMessage("Salvo com sucesso!");
			return resp;
		}

		return resp;
	}

	public Response removeById(Integer id) {
		Response resp = new Response(false, "Erro ao remover.");

		Orcamento orcamento = this.repo.findById(id);

		if (orcamento == null) {
			resp.setMessage("Não encontrado!");
			return resp;
		}

		if (orcamento.getLancamentos() != null && !orcamento.getLancamentos().isEmpty()) {
			resp.setMessage("Não é possível remover, \nexistem lançamentos vinculados a este registro");
			return resp;
		}

		if (orcamento.getRendimentos() != null && !orcamento.getRendimentos().isEmpty()) {
			resp.setMessage("Não é possível remover, \nexistem rendimentos vinculados a este registro");
			return resp;
		}

		if (orcamento.getGastos() != null && !orcamento.getGastos().isEmpty()) {
			resp.setMessage("Não é possível remover, \nexistem Despesas vinculadas a este registro");
			return resp;
		}

		if (this.repo.remove(orcamento)) {
			resp.setSuccess(true);
			resp.setMessage("Removido com sucesso!");
		}

		return resp;
	}

	public Response copiar(Orcamento orcamento) {
		Response resp = new Response(false, "Problema crítico ao efetuar a operação!");

		if ((orcamento.getLancamentos() != null && !orcamento.getLancamentos().isEmpty()) || 
				(orcamento.getGastos() != null && !orcamento.getGastos().isEmpty()) || 
				(orcamento.getRendimentos() != null && !orcamento.getRendimentos().isEmpty())) {
			resp.setMessage("Orçamento já tem itens relacionados, não pode ter itens copiados");
			return resp;
		}

		try {
			Calendar cal = Calendar.getInstance();
			Orcamento lastOrcamento = this.getAll(false).stream().sorted((o1, o2) -> Integer.compare(o2.getId(), o1.getId()))
					.collect(Collectors.toList()).get(1);

			if (lastOrcamento.getLancamentos() != null && !lastOrcamento.getLancamentos().isEmpty())
				lastOrcamento.getLancamentos().stream().forEach(l -> {
					cal.setTime(l.getData());
					cal.add(Calendar.MONTH, 1);
					Lancamento lan = new Lancamento(l.getTipoInvestimento(), null, l.getValor(), cal.getTime(),
							l.getObjetivo(), l.getCategoria(), orcamento);
					this.objetivoService.saveLancamento(lan);
				});

			if (lastOrcamento.getRendimentos() != null && !lastOrcamento.getRendimentos().isEmpty())
				lastOrcamento.getRendimentos().stream().forEach(r -> {
					cal.setTime(r.getData());
					cal.add(Calendar.MONTH, 1);
					this.rendimentoService.save(r.getDescricao(), r.getValor().toString(), cal.getTime(), "Não",
							r.getCategoria().getId(), orcamento.getId());

				});

			if (lastOrcamento.getGastos() != null && !lastOrcamento.getGastos().isEmpty())
				lastOrcamento.getGastos().stream().forEach(g -> {
					cal.setTime(g.getData());
					cal.add(Calendar.MONTH, 1);

					this.gastoService.save(g.getDescricao(), g.getValor().toString(),
							cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "PENDENTE",
							g.getCartao(), g.getCategoria().getId(), orcamento.getId());

				});

			resp.setMessage("Itens copiados com sucesso!");
			resp.setSuccess(true);
			return resp;

		} catch (Exception e) {
			return resp;
		}

	}

}
