package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import model.ItemObjetivo;

public class ItemObjetivoRepository extends Repository{
	
	@SuppressWarnings("unchecked")
	public List<ItemObjetivo> getAll() {
		List<ItemObjetivo> item =  new ArrayList<>();
		
		this.em.getTransaction().begin();
		Query query = this.em.createQuery("SELECT io from ItemObjetivo io");
		item.addAll(query.getResultList());
		this.em.getTransaction().commit();
		this.emf.close();
		return item;
		
	}
	
	public ItemObjetivo findById(Integer id) {
		try {
			this.em.getTransaction().begin();
			ItemObjetivo item =  this.em.find(ItemObjetivo.class, id);
			this.em.getTransaction().commit();
			return item;
		} catch (Exception e) {
			return null;
		}
	}
	
	public ItemObjetivo save(ItemObjetivo item) {
		try {
			this.em.getTransaction().begin();
			this.em.merge(item);
			this.em.getTransaction().commit();
			this.emf.close();
			return item;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean remove(ItemObjetivo item) {
		try {
			this.em.getTransaction().begin();
			this.em.remove(item);
			this.em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
