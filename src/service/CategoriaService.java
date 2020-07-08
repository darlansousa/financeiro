package service;

import java.util.List;

import model.Categoria;
import repository.CategoriaRepository;

public class CategoriaService {
	
	CategoriaRepository repo;
	
	public CategoriaService() {
		this.repo = new CategoriaRepository();
	}
	
	public List<Categoria> getAll(){
		return this.repo.getAll();
	}

}
