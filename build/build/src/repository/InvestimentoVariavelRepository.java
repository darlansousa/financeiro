package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import model.InvestimentoVariavel;

public class InvestimentoVariavelRepository extends Repository{
	
	@SuppressWarnings("unchecked")
	public List<InvestimentoVariavel> getAll(){
		List<InvestimentoVariavel> investimentos =  new ArrayList<>();
		
		this.em.getTransaction().begin();
		Query query = this.em.createQuery("SELECT iv from InvestimentoVariavel iv");
		investimentos.addAll(query.getResultList());
		this.em.getTransaction().commit();
		this.emf.close();
		return investimentos;
	}
	
	public InvestimentoVariavel findById(Integer id) {
		try {
			this.em.getTransaction().begin();
			InvestimentoVariavel investimentoVariavel =  this.em.find(InvestimentoVariavel.class, id);
			this.em.getTransaction().commit();
			return investimentoVariavel;
		} catch (Exception e) {
			return null;
		}
	}
	
	public InvestimentoVariavel save(InvestimentoVariavel investimentoVariavel) {
		try {
			this.em.getTransaction().begin();
			this.em.merge(investimentoVariavel);
			this.em.getTransaction().commit();
			this.emf.close();
			return investimentoVariavel;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean remove(InvestimentoVariavel investimentoVariavel) {
		try {
			this.em.getTransaction().begin();
			this.em.remove(investimentoVariavel);
			this.em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
