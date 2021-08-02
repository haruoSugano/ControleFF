package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.ContaDao;
import model.entities.Conta;

public class ContaDaoJDBC implements ContaDao {

	private Connection conn;
	
	public ContaDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Conta findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM conta WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Conta conta = new Conta();
				conta.setId(rs.getInt("Id"));
				conta.setNome(rs.getString("Nome"));
				return conta;
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

	@Override
	public List<Conta> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM conta ORDER BY Nome");
			rs = st.executeQuery();

			List<Conta> list = new ArrayList<>();

			while (rs.next()) {
				Conta conta = new Conta();
				conta.setId(rs.getInt("Id"));
				conta.setNome(rs.getString("Nome"));
				list.add(conta);
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
	public void insert(Conta conta) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO conta " 
				+"(Nome) " 
				+"VALUES " 
				+"(?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, conta.getNome());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					conta.setId(id);
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
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
	public void update(Conta conta) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE conta " +
				"SET Nome = ? " +
				"WHERE Id = ?");

			st.setString(1, conta.getNome());
			st.setInt(2, conta.getId());

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
			st = conn.prepareStatement(
				"DELETE FROM conta WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
}
