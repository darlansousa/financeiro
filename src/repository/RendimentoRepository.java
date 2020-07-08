package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import model.Rendimento;

public class RendimentoRepository extends Repository{
	
	@SuppressWarnings("unchecked")
	public List<Rendimento> getAll(){
		List<Rendimento> rendimentos =  new ArrayList<>();
		
		this.em.getTransaction().begin();
		Query query = this.em.createQuery("SELECT r from Rendimento r");
		rendimentos.addAll(query.getResultList());
		this.em.getTransaction().commit();
		this.emf.close();
		return rendimentos;
	}
	
	public Rendimento findById(Integer id) {
		try {
			this.em.getTransaction().begin();
			Rendimento rendimento =  this.em.find(Rendimento.class, id);
			this.em.getTransaction().commit();
			return rendimento;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Rendimento save(Rendimento rendimento) {
		try {
			this.em.getTransaction().begin();
			this.em.merge(rendimento);
			this.em.getTransaction().commit();
			this.emf.close();
			return rendimento;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean remove(Rendimento rendimento) {
		try {
			this.em.getTransaction().begin();
			this.em.remove(rendimento);
			this.em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
