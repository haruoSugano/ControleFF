package model.dao;

import db.DB;
import model.dao.impl.ContaDaoJDBC;
import model.dao.impl.DespesaDaoJDBC;
import model.dao.impl.EntradaDaoJDBC;
import model.dao.impl.UsuarioDaoJDBC;

public class DaoFactory {

	public static UsuarioDao createUsuarioDao() {
		return new UsuarioDaoJDBC(DB.getConnection());
	}
	
	public static ContaDao createContaDao() {
		return new ContaDaoJDBC(DB.getConnection());
	}
	
	public static EntradaDao createEntradaDao() {
		return new EntradaDaoJDBC(DB.getConnection());
	}
	
	
	
	public static DespesaDao createDespesaDao() {
		return new DespesaDaoJDBC(DB.getConnection());
	}
}
