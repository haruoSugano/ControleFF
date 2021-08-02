package model.dao;

import java.util.List;

import model.entities.Pagamento;

public interface PagamentoDao {

	void insert(Pagamento pagamento);
	void update(Pagamento pagamento);
	void deleteById(Integer id);
	Pagamento findById(Integer id);
	List<Pagamento> findAll();
}
