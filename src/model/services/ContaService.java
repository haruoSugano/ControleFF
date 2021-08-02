package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ContaDao;
import model.entities.Conta;

public class ContaService {
	
	private ContaDao dao = DaoFactory.createContaDao();
	
	public List<Conta> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Conta conta) {
		if(conta.getId() == null) {
			dao.insert(conta);
		}
		else {
			dao.update(conta);
		}
	}
	
	public void remover(Conta conta) {
		dao.deleteById(conta.getId());
	}

}
