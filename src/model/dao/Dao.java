package model.dao;

import java.util.List;

public interface Dao<G> {

	G insert(G obj);
	
	void update(G obj);
	
	void deleteById(Integer id);
	
	G findById(Integer id);
	
	List<G> findAll();
}
