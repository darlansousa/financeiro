package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import model.InvestimentoFixo;

public class InvestimentoFixoRepository extends Repository{
	
	@SuppressWarnings("unchecked")
	public List<InvestimentoFixo> getAll(){
		List<InvestimentoFixo> investimentos =  new ArrayList<>();
		
		this.em.getTransaction().begin();
		Query query = this.em.createQuery("SELECT if from InvestimentoFixo if");
		investimentos.addAll(query.getResultList());
		this.em.getTransaction().commit();
		this.emf.close();
		return investimentos;
	}
	
	public InvestimentoFixo findById(Integer id) {
		try {
			this.em.getTransaction().begin();
			InvestimentoFixo investimentoFixo =  this.em.find(InvestimentoFixo.class, id);
			this.em.getTransaction().commit();
			return investimentoFixo;
		} catch (Exception e) {
			return null;
		}
	}
	
	public InvestimentoFixo save(InvestimentoFixo investimentoFixo) {
		try {
			this.em.getTransaction().begin();
			this.em.merge(investimentoFixo);
			this.em.getTransaction().commit();
			this.emf.close();
			return investimentoFixo;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean remove(InvestimentoFixo investimentoFixo) {
		try {
			this.em.getTransaction().begin();
			this.em.remove(investimentoFixo);
			this.em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
