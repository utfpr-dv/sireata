package br.edu.utfpr.dv.sireata.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sireata.model.Campus;

public class CampusDAO {
	
	public Campus buscarPorId(int id) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("SELECT * FROM campus WHERE idCampus = ?");
		
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			return this.carregarObjeto(rs);
		}else{
			return null;
		}
	}
	
	public Campus buscarPorDepartamento(int idDepartamento) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("SELECT idCampus FROM departamentos WHERE idDepartamento=?");
		
		stmt.setInt(1, idDepartamento);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			return this.buscarPorId(rs.getInt("idCampus"));
		}else{
			return null;
		}
	}
	
	public List<Campus> listarTodos(boolean apenasAtivos) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM campus " + (apenasAtivos ? " WHERE ativo=1" : "") + " ORDER BY nome");
		
		List<Campus> list = new ArrayList<Campus>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Campus> listarParaCriacaoAta(int idUsuario) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT campus.* FROM campus " +
				"INNER JOIN departamentos ON departamentos.idCampus=campus.idCampus " +
				"INNER JOIN orgaos ON orgaos.idDepartamento=departamentos.idDepartamento " +
				"WHERE campus.ativo=1 AND (orgaos.idPresidente=" + String.valueOf(idUsuario) + " OR orgaos.idSecretario=" + String.valueOf(idUsuario) + 
				") ORDER BY campus.nome");
		
		List<Campus> list = new ArrayList<Campus>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Campus> listarParaConsultaAtas(int idUsuario) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT campus.* FROM campus " +
				"INNER JOIN departamentos ON departamentos.idCampus=campus.idCampus " +
				"INNER JOIN orgaos ON orgaos.idDepartamento=departamentos.idDepartamento " +
				"INNER JOIN atas ON atas.idOrgao=orgaos.idOrgao " +
				"INNER JOIN ataParticipantes ON ataParticipantes.idAta=atas.idAta " +
				"WHERE atas.publicada=0 AND ataParticipantes.presente=1 AND ataParticipantes.idUsuario=" + String.valueOf(idUsuario) + 
				" ORDER BY campus.nome");
		
		List<Campus> list = new ArrayList<Campus>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public int salvar(Campus campus) throws SQLException{
		boolean insert = (campus.getIdCampus() == 0);
		PreparedStatement stmt;
		
		if(insert){
			stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("INSERT INTO campus(nome, endereco, logo, ativo, site) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		}else{
			stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("UPDATE campus SET nome=?, endereco=?, logo=?, ativo=?, site=? WHERE idCampus=?");
		}
		
		stmt.setString(1, campus.getNome());
		stmt.setString(2, campus.getEndereco());
		if(campus.getLogo() == null){
			stmt.setNull(3, Types.BLOB);
		}else{
			stmt.setBytes(3, campus.getLogo());	
		}
		stmt.setInt(4, campus.isAtivo() ? 1 : 0);
		stmt.setString(5, campus.getSite());
		
		if(!insert){
			stmt.setInt(6, campus.getIdCampus());
		}
		
		stmt.execute();
		
		if(insert){
			ResultSet rs = stmt.getGeneratedKeys();
			
			if(rs.next()){
				campus.setIdCampus(rs.getInt(1));
			}
		}
		
		return campus.getIdCampus();
	}
	
	private Campus carregarObjeto(ResultSet rs) throws SQLException{
		Campus campus = new Campus();
		
		campus.setIdCampus(rs.getInt("idCampus"));
		campus.setNome(rs.getString("nome"));
		campus.setEndereco(rs.getString("endereco"));
		campus.setLogo(rs.getBytes("logo"));
		campus.setAtivo(rs.getInt("ativo") == 1);
		campus.setSite(rs.getString("site"));
		
		return campus;
	}

}
