package model.dao;

import java.util.List;

import model.entities.Conta;

public interface ContaDao {

	void insert(Conta obj);
	void update(Conta obj);
	void deleteById(Integer id);
	Conta findById(Integer id);
	List<Conta> findAll();
}
