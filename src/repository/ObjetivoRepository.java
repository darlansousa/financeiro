package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import model.Objetivo;

public class ObjetivoRepository extends Repository{
	
	@SuppressWarnings("unchecked")
	public List<Objetivo> getAll() {
		List<Objetivo> objetivos =  new ArrayList<>();
		
		this.em.getTransaction().begin();
		Query query = this.em.createQuery("SELECT o from Objetivo o");
		objetivos.addAll(query.getResultList());
		this.em.getTransaction().commit();
		this.emf.close();
		return objetivos;
		
	}
	
	public Objetivo findById(Integer id) {
		try {
			this.em.getTransaction().begin();
			Objetivo objetivo =  this.em.find(Objetivo.class, id);
			this.em.getTransaction().commit();
			return objetivo;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Objetivo save(Objetivo objetivo) {
		try {
			this.em.getTransaction().begin();
			this.em.merge(objetivo);
			this.em.getTransaction().commit();
			this.emf.close();
			return objetivo;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean remove(Objetivo objetivo) {
		try {
			this.em.getTransaction().begin();
			this.em.remove(objetivo);
			this.em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
