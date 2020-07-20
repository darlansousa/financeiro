package service;

import java.util.Date;
import java.util.List;

import model.Categoria;
import model.Orcamento;
import model.Rendimento;
import repository.CategoriaRepository;
import repository.OrcamentoRepository;
import repository.RendimentoRepository;
import util.Response;
import util.Uteis;

public class RendimentoService {

	RendimentoRepository repo;
	CategoriaRepository categoriaRepo;
	OrcamentoRepository orcamentoRepo;

	public RendimentoService() {
		this.repo = new RendimentoRepository();
		this.categoriaRepo = new CategoriaRepository();
		this.orcamentoRepo = new OrcamentoRepository();
	}

	public List<Rendimento> getAll() {
		return this.repo.getAll();
	}

	public Response save(String descricao, String valor, Date data, String recebido, Integer idCategoria,
			Integer idOrcamento) {
		Response resp = new Response(false, "Erro não conhecido.");

		if (descricao == null || descricao.isEmpty()) {
			resp.setMessage("Descrição inválida.");
			return resp;
		}
		if (data == null) {
			resp.setMessage("Data inicail inválida.");
			return resp;
		}

		if (valor == null || valor.isEmpty() || (!Uteis.isNumeric(valor))) {
			resp.setMessage("Valor inválido.");
			return resp;
		}
		
		if (recebido == null) {
			resp.setMessage("Campo Recebido inválido.");
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
			resp.setMessage("Orçamento inválido.");
			return resp;
		}

		Orcamento orcamento = this.orcamentoRepo.findById(idOrcamento);

		if (orcamento == null) {
			resp.setMessage("Orcamento não encontrado.");
			return resp;
		}

		if ((!(data.after(orcamento.getDataInicial()) & data.before(orcamento.getDataFinal())))
				& !(data.equals(orcamento.getDataInicial()) || data.equals(orcamento.getDataFinal()))) {
			resp.setMessage("Rendimento fora do período do orçamento.\nInserir uma data entre "
					+ orcamento.getDataInicial().toString() + "\n e " + orcamento.getDataFinal().toString() + ".");
			return resp;
		}

		if (recebido.equals("Sim")) {
			recebido = "S";
		} else {
			recebido = "N";
		}

		Rendimento rendimento = new Rendimento(descricao, new Double(valor), data, recebido.substring(0, 1), categoria,
				orcamento);
		Rendimento rendSaved = this.repo.save(rendimento);

		if (rendSaved != null) {
			resp.setSuccess(true);
			resp.setMessage("Salvo com sucesso!");
			return resp;
		}

		return resp;
	}

	public Response update(String id, String descricao, String valor,String valorAnterior,String statusAnterior, Date data, String recebido, Integer idCategoria,
			Integer idOrcamento) {
		Response resp = new Response(false, "Erro desconhecido.");

		if (descricao == null || descricao.isEmpty()) {
			resp.setMessage("Descrição inválida.");
			return resp;
		}
		if (data == null) {
			resp.setMessage("Data inicail inválida.");
			return resp;
		}

		if (valor == null || valor.isEmpty() || (!Uteis.isNumeric(valor))) {
			resp.setMessage("Valor inválido.");
			return resp;
		}
		
		if (recebido == null) {
			resp.setMessage("Campo recebido inválido.");
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

		if ((!(data.after(orcamento.getDataInicial()) & data.before(orcamento.getDataFinal())))
				& !(data.equals(orcamento.getDataInicial()) || data.equals(orcamento.getDataFinal()))) {
			resp.setMessage("Rendimento fora do período do orçamento.\nInserir uma data entre "
					+ orcamento.getDataInicial().toString() + "\n e " + orcamento.getDataFinal().toString() + ".");
			return resp;
		}

		if (recebido.equals("Sim")) {
			recebido = "S";
		} else {
			recebido = "N";
		}

		Rendimento rendimento = new Rendimento(new Integer(id), descricao, new Double(valor), data, recebido, categoria,
				orcamento);
		
		Rendimento rendSaved = this.repo.save(rendimento);

		
		
		if (rendSaved != null) {
			resp.setSuccess(true);
			resp.setMessage("Salvo com sucesso!");
			return resp;
		}

		return resp;
	}

	public Response removeById(Integer id) {
		Response resp = new Response(false, "Erro ao remover.");

		Rendimento rendimento = this.repo.findById(id);

		if (rendimento == null) {
			resp.setMessage("Não encontrado!");
			return resp;
		}

		if (this.repo.remove(rendimento)) {
			resp.setSuccess(true);
			resp.setMessage("Removido com sucesso!");
		}

		return resp;
	}

}
