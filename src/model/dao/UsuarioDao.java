package model.dao;

import java.util.List;

import model.entities.Conta;
import model.entities.Usuario;

public interface UsuarioDao {

	void insert(Usuario usuario);
	void update(Usuario usuario);
	void deleteById(Integer id);
	Usuario findById(Integer id);
	List<Usuario> findAll();
	List<Usuario> findByConta(Conta conta);
}
