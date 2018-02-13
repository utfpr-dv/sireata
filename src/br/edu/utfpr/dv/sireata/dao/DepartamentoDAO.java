package br.edu.utfpr.dv.sireata.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sireata.model.Departamento;

public class DepartamentoDAO {

	public Departamento buscarPorId(int id) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.prepareStatement(
				"SELECT departamentos.*, campus.nome AS nomeCampus " +
				"FROM departamentos INNER JOIN campus ON campus.idCampus=departamentos.idCampus " +
				"WHERE idDepartamento = ?");
		
			stmt.setInt(1, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				return this.carregarObjeto(rs);
			}else{
				return null;
			}
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public Departamento buscarPorOrgao(int idOrgao) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.prepareStatement(
				"SELECT departamentos.*, campus.nome AS nomeCampus " +
				"FROM departamentos INNER JOIN campus ON campus.idCampus=departamentos.idCampus " +
				"INNER JOIN orgaos ON orgaos.idDepartamento=departamentos.idDepartamento " +
				"WHERE orgaos.idOrgao = ?");
		
			stmt.setInt(1, idOrgao);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				return this.carregarObjeto(rs);
			}else{
				return null;
			}
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public List<Departamento> listarTodos(boolean apenasAtivos) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT DISTINCT departamentos.*, campus.nome AS nomeCampus " +
				"FROM departamentos INNER JOIN campus ON campus.idCampus=departamentos.idCampus " + 
				(apenasAtivos ? " WHERE departamentos.ativo=1" : "") + " ORDER BY departamentos.nome");
		
			List<Departamento> list = new ArrayList<Departamento>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public List<Departamento> listarPorCampus(int idCampus, boolean apenasAtivos) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT DISTINCT departamentos.*, campus.nome AS nomeCampus " +
				"FROM departamentos INNER JOIN campus ON campus.idCampus=departamentos.idCampus " +
				"WHERE departamentos.idCampus=" + String.valueOf(idCampus) + (apenasAtivos ? " AND departamentos.ativo=1" : "") + " ORDER BY departamentos.nome");
		
			List<Departamento> list = new ArrayList<Departamento>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public List<Departamento> listarParaCriacaoAta(int idCampus, int idUsuario) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT DISTINCT departamentos.*, campus.nome AS nomeCampus FROM departamentos " +
				"INNER JOIN campus ON campus.idCampus=departamentos.idCampus " +
				"INNER JOIN orgaos ON orgaos.idDepartamento=departamentos.idDepartamento " +
				"WHERE departamentos.ativo=1 AND departamentos.idCampus=" + String.valueOf(idCampus) + " AND (orgaos.idPresidente=" + String.valueOf(idUsuario) + " OR orgaos.idSecretario=" + String.valueOf(idUsuario) + 
				") ORDER BY departamentos.nome");
		
			List<Departamento> list = new ArrayList<Departamento>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public List<Departamento> listarParaConsultaAtas(int idCampus, int idUsuario) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT DISTINCT departamentos.*, campus.nome AS nomeCampus FROM departamentos " +
				"INNER JOIN campus ON campus.idCampus=departamentos.idCampus " +
				"INNER JOIN orgaos ON orgaos.idDepartamento=departamentos.idDepartamento " +
				"INNER JOIN atas ON atas.idOrgao=orgaos.idOrgao " +
				"INNER JOIN ataParticipantes ON ataParticipantes.idAta=atas.idAta " +
				"WHERE atas.publicada=0 AND ataParticipantes.presente=1 AND departamentos.idCampus=" + String.valueOf(idCampus) + " AND ataParticipantes.idUsuario=" + String.valueOf(idUsuario) + 
				" ORDER BY departamentos.nome");
		
			List<Departamento> list = new ArrayList<Departamento>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public int salvar(Departamento departamento) throws SQLException{
		boolean insert = (departamento.getIdDepartamento() == 0);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
		
			if(insert){
				stmt = conn.prepareStatement("INSERT INTO departamentos(idCampus, nome, logo, ativo, site, nomeCompleto) VALUES(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			}else{
				stmt = conn.prepareStatement("UPDATE departamentos SET idCampus=?, nome=?, logo=?, ativo=?, site=?, nomeCompleto=? WHERE idDepartamento=?");
			}
			
			stmt.setInt(1, departamento.getCampus().getIdCampus());
			stmt.setString(2, departamento.getNome());
			if(departamento.getLogo() == null){
				stmt.setNull(3, Types.BINARY);
			}else{
				stmt.setBytes(3, departamento.getLogo());	
			}
			stmt.setInt(4, departamento.isAtivo() ? 1 : 0);
			stmt.setString(5, departamento.getSite());
			stmt.setString(6, departamento.getNomeCompleto());
			
			if(!insert){
				stmt.setInt(7, departamento.getIdDepartamento());
			}
			
			stmt.execute();
			
			if(insert){
				rs = stmt.getGeneratedKeys();
				
				if(rs.next()){
					departamento.setIdDepartamento(rs.getInt(1));
				}
			}
			
			return departamento.getIdDepartamento();
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	private Departamento carregarObjeto(ResultSet rs) throws SQLException{
		Departamento departamento = new Departamento();
		
		departamento.setIdDepartamento(rs.getInt("idDepartamento"));
		departamento.getCampus().setIdCampus(rs.getInt("idCampus"));
		departamento.setNome(rs.getString("nome"));
		departamento.setNomeCompleto(rs.getString("nomeCompleto"));
		departamento.setLogo(rs.getBytes("logo"));
		departamento.setAtivo(rs.getInt("ativo") == 1);
		departamento.setSite(rs.getString("site"));
		departamento.getCampus().setNome(rs.getString("nomeCampus"));
		
		return departamento;
	}
	
}
