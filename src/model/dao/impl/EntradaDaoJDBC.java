package model.dao.impl;

import java.sql.Connection;
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
import model.dao.EntradaDao;
import model.entities.Entrada;
import model.entities.Renda;

public class EntradaDaoJDBC implements EntradaDao{
	
	private Connection conn;
	
	public EntradaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Entrada entrada) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO entrada "
					+ "(tipoEntrada, dataEntrada, valor, acrescimo, fixo, usuarioId, rendaId)"
					+ "VALUES"
					+ "(?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, entrada.getTipoEntrada());
			st.setDate(2, new java.sql.Date(entrada.getDataEntrada().getTime()));
			st.setDouble(3, entrada.getValorBruto());
			st.setDouble(4, entrada.getAcrescimo());
			st.setBoolean(5, entrada.getFixo());
			st.setInt(6, entrada.getUsuario().getId());
			st.setInt(7, entrada.getRenda().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					entrada.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Erro inesperada! Linha não foi afetada!");
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
	public void update(Entrada entrada) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("Update entrada "
					+ "SET tipoEntrada = ?, dataEntrada = ?, valor = ?, acrescimo = ?, fixo = ?, UsuarioId = ?, RendaId = ? "
					+ "WHERE id = ?");
			
			st.setString(1, entrada.getTipoEntrada());
			st.setDate(2, new java.sql.Date(entrada.getDataEntrada().getTime()));
			st.setDouble(3, entrada.getValorBruto());
			st.setDouble(4, entrada.getAcrescimo());
			st.setBoolean(5, entrada.getFixo());
			st.setInt(6, entrada.getUsuario().getId());
			st.setInt(7, entrada.getRenda().getId());
			
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
			st = conn.prepareStatement(" DELETE FROM entrada WHERE id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}

	@Override
	public Entrada findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT entrada.*, renda.tipo as RendaTipo "
						+ "FROM entrada INNER JOIN renda "
						+ "ON entrada.rendaId = renda.id "
						+ "WHERE entrada.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Renda renda = instantiateRenda(rs);
				Entrada entrada = instantiateEntrada(rs, renda);
				return entrada;
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
	public List<Entrada> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT entrada.*, renda.tipo as RendaTipo "
					+ "FROM entrada INNER JOIN renda "
					+ "ON entrada.rendaId = renda.id "
					+ "ORDER BY tipo");
			
			rs = st.executeQuery();
			
			List<Entrada> list = new ArrayList<>();
			Map<Integer, Renda> map = new HashMap<>();
			
			while (rs.next()) {
				
				Renda rendaU = map.get(rs.getInt("rendaId"));
				
				if (rendaU == null) {
					rendaU = instantiateRenda(rs);
					map.put(rs.getInt("rendaId"), rendaU);
				}
				
				Entrada entrada = instantiateEntrada(rs, rendaU);
				list.add(entrada);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	@Override
	public List<Entrada> findByConta(Renda renda){
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT entrada.*,renda.tipo as RendaTipo "
					+ "FROM entrada INNER JOIN renda "
					+ "ON entrada.rendaId = renda.id "
					+ "WHERE rendaId = ? "
					+ "ORDER BY tipo");
			
			st.setInt(1, renda.getId());
			
			rs = st.executeQuery();
			
			List<Entrada> list = new ArrayList<>();
			Map<Integer, Renda> map = new HashMap<>();
			
			while(rs.next()) {
				
				Renda rendaU = map.get(rs.getInt("rendaId"));
				
				if(rendaU == null) {
					rendaU = instantiateRenda(rs);
					map.put(rs.getInt("rendaId"), rendaU);
				}
				
				Entrada entrada = instantiateEntrada(rs, rendaU);
				list.add(entrada);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	private Entrada instantiateEntrada(ResultSet rs, Renda renda) throws SQLException {
		Entrada entrada = new Entrada();
		entrada.setId(rs.getInt("id"));
		entrada.setDataEntrada(new java.sql.Date(rs.getTimestamp("dataEntrada").getTime()));
		entrada.setValorBruto(rs.getDouble("valor"));
		entrada.setAcrescimo(rs.getDouble("acrescimo"));
		entrada.setFixo(rs.getBoolean("fixo"));
		entrada.setRenda(renda);
		return entrada;
	}
	
	private Renda instantiateRenda(ResultSet rs) throws SQLException {
		Renda renda = new Renda();
		renda.setId(rs.getInt("id"));
		renda.setTipo(rs.getString("tipo"));
		return renda;
	}
}
