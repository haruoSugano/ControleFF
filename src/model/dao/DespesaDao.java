package model.dao;

import java.util.List;

import model.entities.Conta;
import model.entities.Despesa;

public interface DespesaDao {

	void insert(Despesa despesa);
	void update(Despesa despesa);
	void deleteById(Integer id);
	Despesa findById(Integer id);
	List<Despesa> findAll();
	List<Despesa> findByConta(Conta conta);
}
