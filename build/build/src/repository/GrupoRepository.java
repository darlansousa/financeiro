package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import model.Grupo;

public class GrupoRepository extends Repository{
	
	
	@SuppressWarnings("unchecked")
	public List<Grupo> getAll(){
		List<Grupo> grupos =  new ArrayList<>();
		
		this.em.getTransaction().begin();
		Query query = this.em.createQuery("SELECT g from Grupo g");
		grupos.addAll(query.getResultList());
		this.em.getTransaction().commit();
		this.emf.close();
		return grupos;
	}
	
	
	public Grupo findById(Integer id) {
		try {
			this.em.getTransaction().begin();
			Grupo grupo =  this.em.find(Grupo.class, id);
			this.em.getTransaction().commit();
			return grupo;
		} catch (Exception e) {
			return null;
		}
	}
	

}
