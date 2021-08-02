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
import model.dao.RendaDao;
import model.entities.Renda;

public class RendaDaoJDBC implements RendaDao {

	private Connection conn;
	
	public RendaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Renda renda) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO renda "
					+ "(tipo) "
					+ "VALUE "
					+ "(?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, renda.getTipo());
			st.setInt(2, renda.getId());
			
			int rowsAffected = st.executeUpdate();
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					renda.setId(id);
				}
				DB.closeConnection();
			}
			else {
				throw new DbException("Erro Inesperado! Linhas não afetadas");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Renda renda) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE renda "
					+ "SET tipo = ? "
					+ "WHERE id = ?");
			
			st.setString(1, renda.getTipo());
			
			st.executeUpdate();
		}
		catch(SQLException e) {
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
			st = conn.prepareStatement("DELETE FROM cont WHERE id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}

	@Override
	public Renda findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM renda WHERE id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				Renda renda = instantiateRenda(rs);
				return renda;
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Renda> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM renda ORDER BY tipo");
			rs = st.executeQuery();
			
			List<Renda> list = new ArrayList<>();
			
			while(rs.next()) {
				Renda renda = instantiateRenda(rs);
				list.add(renda);
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		return null;
	}
	
	public Renda instantiateRenda(ResultSet rs) throws SQLException {
		Renda renda = new Renda();
		renda.setId(rs.getInt("id"));
		renda.setTipo(rs.getString("tipo"));
		return renda;
	}
	
}
