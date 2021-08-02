package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.EntradaDao;
import model.entities.Entrada;

public class EntradaService {

	private EntradaDao dao = DaoFactory.createEntradaDao();
	
	public List<Entrada> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Entrada entrada) {
		if(entrada.getId() == null) {
			dao.insert(entrada);
		}
		else {
			dao.update(entrada);
		}
	}
	
	public void remove(Entrada entrada) {
		dao.deleteById(entrada.getId());
	}
}
