package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import model.Gasto;

public class GastoRepository extends Repository{
	
	@SuppressWarnings("unchecked")
	public List<Gasto> getAll(){
		List<Gasto> gastos =  new ArrayList<>();
		
		this.em.getTransaction().begin();
		Query query = this.em.createQuery("SELECT g from Gasto g");
		gastos.addAll(query.getResultList());
		this.em.getTransaction().commit();
		this.emf.close();
		return gastos;
	}
	
	public Gasto findById(Integer id) {
		try {
			this.em.getTransaction().begin();
			Gasto gasto =  this.em.find(Gasto.class, id);
			this.em.getTransaction().commit();
			return gasto;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Gasto save(Gasto gasto) {
		try {
			this.em.getTransaction().begin();
			this.em.merge(gasto);
			this.em.getTransaction().commit();
			this.emf.close();
			return gasto;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean remove(Gasto gasto) {
		try {
			this.em.getTransaction().begin();
			this.em.remove(gasto);
			this.em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
