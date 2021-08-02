package model.dao;

import java.util.List;

import model.entities.Renda;

public interface RendaDao {

	void insert(Renda renda);
	void update(Renda renda);
	void deleteById(Integer id);
	Renda findById(Integer id);
	List<Renda> findAll();
}
