package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import model.Categoria;

public class CategoriaRepository extends Repository{
	
	@SuppressWarnings("unchecked")
	public List<Categoria> getAll(){
		List<Categoria> categorias =  new ArrayList<>();
		
		this.em.getTransaction().begin();
		Query query = this.em.createQuery("SELECT c from Categoria c");
		categorias.addAll(query.getResultList());
		this.em.getTransaction().commit();
		this.emf.close();
		return categorias;
	}
	
	public Categoria findById(Integer id) {
		try {
			this.em.getTransaction().begin();
			Categoria categoria =  this.em.find(Categoria.class, id);
			this.em.getTransaction().commit();
			return categoria;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Categoria save(Categoria categoria) {
		try {
			this.em.getTransaction().begin();
			this.em.merge(categoria);
			this.em.getTransaction().commit();
			this.emf.close();
			return categoria;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean remove(Categoria categoria) {
		try {
			this.em.getTransaction().begin();
			this.em.remove(categoria);
			this.em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
