package service;

import java.util.List;

import model.Grupo;
import repository.GrupoRepository;

public class GrupoService {
	
	GrupoRepository repo;
	
	public GrupoService() {
		this.repo = new GrupoRepository();
	}
	
	public List<Grupo> getAll(){
		return this.repo.getAll();
	}
	
}
