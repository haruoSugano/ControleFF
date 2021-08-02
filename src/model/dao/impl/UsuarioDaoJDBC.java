package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.UsuarioDao;
import model.entities.Conta;
import model.entities.Usuario;

public class UsuarioDaoJDBC implements UsuarioDao {

	private Connection conn;
	
	public UsuarioDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Usuario usuario) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO usuario "
					+ "(Nome, Email, BirthDate, ContaId) "
					+ "VALUES "
					+ "(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, usuario.getNome());
			st.setString(2, usuario.getEmail());
			st.setDate(3, new java.sql.Date(usuario.getBirthDate().getTime()));
			st.setInt(4, usuario.getConta().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					usuario.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Erro inesperado! Linhas não afetada!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Usuario usuario) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE usuario "
					+ "SET Nome = ?, Email = ?, BirthDate = ?, ContaId = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, usuario.getNome());
			st.setString(2, usuario.getEmail());
			st.setDate(3, new java.sql.Date(usuario.getBirthDate().getTime()));
			st.setInt(5, usuario.getConta().getId());
			st.setInt(6, usuario.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM usuario WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Usuario findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT usuario.*,conta.Nome as DepNome "
					+ "FROM usuario INNER JOIN conta "
					+ "ON usuario.ContaId = conta.Id "
					+ "WHERE usuario.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Conta cont = instantiateConta(rs);
				Usuario usuario = instantiateUsuario(rs, cont);
				return usuario;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Usuario instantiateUsuario(ResultSet rs, Conta cont) throws SQLException {
		Usuario usuario = new Usuario();
		usuario.setId(rs.getInt("Id"));
		usuario.setNome(rs.getString("Nome"));
		usuario.setEmail(rs.getString("Email"));
		usuario.setBirthDate(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
		usuario.setConta(cont);
		return usuario;
	}

	private Conta instantiateConta(ResultSet rs) throws SQLException {
		Conta cont = new Conta();
		cont.setId(rs.getInt("ContaId"));
		cont.setNome(rs.getString("ContaNome"));
		return cont;
	}

	@Override
	public List<Usuario> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT usuario.*,conta.Nome as ContaNome "
					+ "FROM usuario INNER JOIN conta "
					+ "ON usuario.ContaId = conta.Id "
					+ "ORDER BY Nome");
			
			rs = st.executeQuery();
			
			List<Usuario> list = new ArrayList<>();
			Map<Integer, Conta> map = new HashMap<>();
			
			while (rs.next()) {
				
				Conta cont = map.get(rs.getInt("ContaId"));
				
				if (cont == null) {
					cont = instantiateConta(rs);
					map.put(rs.getInt("ContaId"), cont);
				}
				
				Usuario usuario = instantiateUsuario(rs, cont);
				list.add(usuario);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Usuario> findByConta(Conta conta) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT usuario.*,conta.Nome as ContaNome "
					+ "FROM usuario INNER JOIN conta "
					+ "ON usuario.ContaId = conta.Id "
					+ "WHERE ContaId = ? "
					+ "ORDER BY Nome");
			
			st.setInt(1, conta.getId());
			
			rs = st.executeQuery();
			
			List<Usuario> list = new ArrayList<>();
			Map<Integer, Conta> map = new HashMap<>();
			
			while (rs.next()) {
				
				Conta cont = map.get(rs.getInt("ContaId"));
				
				if (cont == null) {
					cont = instantiateConta(rs);
					map.put(rs.getInt("ContaId"), cont);
				}
				
				Usuario usuario = instantiateUsuario(rs, cont);
				list.add(usuario);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
