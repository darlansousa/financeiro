package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import model.Orcamento;

public class OrcamentoRepository extends Repository{
	
	@SuppressWarnings("unchecked")
	public List<Orcamento> getAll(){
		List<Orcamento> orcamentos =  new ArrayList<>();
		this.em.clear();
		this.em.getTransaction().begin();
		Query query = this.em.createQuery("SELECT o from Orcamento o");
		orcamentos.addAll(query.getResultList());
		this.em.getTransaction().commit();
	
		this.emf.close();

		return orcamentos;
	}
	
	public Orcamento findById(Integer id) {
		try {
			this.em.getTransaction().begin();
			Orcamento orcamento =  this.em.find(Orcamento.class, id);
			this.em.getTransaction().commit();
			return orcamento;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Orcamento save(Orcamento orcamento) {
		try {
			this.em.getTransaction().begin();
			this.em.merge(orcamento);
			this.em.getTransaction().commit();
			this.emf.close();
			return orcamento;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean remove(Orcamento orcamento) {
		try {
			this.em.getTransaction().begin();
			this.em.remove(orcamento);
			this.em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	

}
