package repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import config.Config;

public class Repository {
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory(Config.get("ambiente"));
	EntityManager em = emf.createEntityManager();
	
	public Repository() {
	}

}
