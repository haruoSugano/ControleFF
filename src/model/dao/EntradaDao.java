package model.dao;

import java.util.List;

import model.entities.Conta;
import model.entities.Entrada;
import model.entities.Renda;

public interface EntradaDao {
	
	void insert(Entrada entrada);
	void update(Entrada entrada);
	void deleteById(Integer id);
	Entrada findById(Integer id);
	List<Entrada> findAll();
	List<Entrada> findByConta(Renda renda);
}
