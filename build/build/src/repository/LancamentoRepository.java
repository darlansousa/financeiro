package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import model.Lancamento;

public class LancamentoRepository extends Repository{
	
	@SuppressWarnings("unchecked")
	public List<Lancamento> getAll() {
		List<Lancamento> lancamento =  new ArrayList<>();
		
		this.em.getTransaction().begin();
		Query query = this.em.createQuery("SELECT l from Lancamento l");
		lancamento.addAll(query.getResultList());
		this.em.getTransaction().commit();
		this.emf.close();
		return lancamento;
		
	}
	
	public Lancamento findById(Integer id) {
		try {
			this.em.getTransaction().begin();
			Lancamento lancamento =  this.em.find(Lancamento.class, id);
			this.em.getTransaction().commit();
			return lancamento;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Lancamento save(Lancamento lancamento) {
		try {
			this.em.getTransaction().begin();
			this.em.merge(lancamento);
			this.em.getTransaction().commit();
			this.emf.close();
			return lancamento;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean remove(Lancamento lancamento) {
		try {
			this.em.getTransaction().begin();
			this.em.remove(lancamento);
			this.em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
