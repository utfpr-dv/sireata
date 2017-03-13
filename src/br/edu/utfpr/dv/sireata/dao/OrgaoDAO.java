package br.edu.utfpr.dv.sireata.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sireata.model.Orgao;
import br.edu.utfpr.dv.sireata.model.OrgaoMembro;
import br.edu.utfpr.dv.sireata.model.Usuario;

public class OrgaoDAO {
	
	public Orgao buscarPorId(int id) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement(
				"SELECT orgaos.*, p.nome AS presidente, s.nome AS secretario, departamentos.nome AS departamento FROM orgaos " +
				"INNER JOIN departamentos ON departamentos.iddepartamento=orgaos.iddepartamento " +
				"INNER JOIN usuarios p ON p.idusuario=orgaos.idpresidente " +
				"INNER JOIN usuarios s ON s.idusuario=orgaos.idsecretario " +
				"WHERE orgaos.idOrgao = ?");
		
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			return this.carregarObjeto(rs);
		}else{
			return null;
		}
	}
	
	public List<Orgao> listarTodos(boolean apenasAtivos) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT orgaos.*, p.nome AS presidente, s.nome AS secretario, departamentos.nome AS departamento FROM orgaos " +
				"INNER JOIN departamentos ON departamentos.iddepartamento=orgaos.iddepartamento " +
				"INNER JOIN usuarios p ON p.idusuario=orgaos.idpresidente " +
				"INNER JOIN usuarios s ON s.idusuario=orgaos.idsecretario " +
				(apenasAtivos ? " WHERE orgaos.ativo=1" : "") + " ORDER BY orgaos.nome");
		
		List<Orgao> list = new ArrayList<Orgao>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Orgao> listarPorDepartamento(int idDepartamento) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement(
				"SELECT orgaos.*, p.nome AS presidente, s.nome AS secretario, departamentos.nome AS departamento FROM orgaos " +
				"INNER JOIN departamentos ON departamentos.iddepartamento=orgaos.iddepartamento " +
				"INNER JOIN usuarios p ON p.idusuario=orgaos.idpresidente " +
				"INNER JOIN usuarios s ON s.idusuario=orgaos.idsecretario " +
				"WHERE orgaos.idDepartamento = ? ORDER BY orgaos.nome");
		
		stmt.setInt(1, idDepartamento);
		
		ResultSet rs = stmt.executeQuery();
		
		List<Orgao> list = new ArrayList<Orgao>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Orgao> listarPorCampus(int idCampus) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement(
				"SELECT orgaos.*, p.nome AS presidente, s.nome AS secretario, departamentos.nome AS departamento FROM orgaos " +
				"INNER JOIN departamentos ON departamentos.iddepartamento=orgaos.iddepartamento " +
				"INNER JOIN usuarios p ON p.idusuario=orgaos.idpresidente " +
				"INNER JOIN usuarios s ON s.idusuario=orgaos.idsecretario " +
				"WHERE departamentos.idCampus = ? ORDER BY departamentos.nome, orgaos.nome");
		
		stmt.setInt(1, idCampus);
		
		ResultSet rs = stmt.executeQuery();
		
		List<Orgao> list = new ArrayList<Orgao>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Orgao> listarParaCriacaoAta(int idDepartamento, int idUsuario) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT orgaos.*, p.nome AS presidente, s.nome AS secretario, departamentos.nome AS departamento FROM orgaos " +
				"INNER JOIN departamentos ON departamentos.iddepartamento=orgaos.iddepartamento " +
				"INNER JOIN usuarios p ON p.idusuario=orgaos.idpresidente " +
				"INNER JOIN usuarios s ON s.idusuario=orgaos.idsecretario " +
				"WHERE orgaos.ativo=1 AND orgaos.idDepartamento=" + String.valueOf(idDepartamento) + " AND (orgaos.idPresidente=" + String.valueOf(idUsuario) + " OR orgaos.idSecretario=" + String.valueOf(idUsuario) + 
				") ORDER BY orgaos.nome");
		
		List<Orgao> list = new ArrayList<Orgao>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Orgao> listarParaConsultaAtas(int idDepartamento, int idUsuario) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT orgaos.*, p.nome AS presidente, s.nome AS secretario, departamentos.nome AS departamento FROM orgaos " +
				"INNER JOIN departamentos ON departamentos.iddepartamento=orgaos.iddepartamento " +
				"INNER JOIN atas ON atas.idOrgao=orgaos.idOrgao " +
				"INNER JOIN ataParticipantes ON ataParticipantes.idAta=atas.idAta " +
				"INNER JOIN usuarios p ON p.idusuario=orgaos.idpresidente " +
				"INNER JOIN usuarios s ON s.idusuario=orgaos.idsecretario " +
				"WHERE atas.publicada=0 AND ataParticipantes.presente=1 AND orgaos.idDepartamento=" + String.valueOf(idDepartamento) + " AND ataParticipantes.idUsuario=" + String.valueOf(idUsuario) + 
				" ORDER BY orgaos.nome");
		
		List<Orgao> list = new ArrayList<Orgao>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public Usuario buscarPresidente(int idOrgao) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement(
				"SELECT idPresidente FROM orgaos WHERE idOrgao = ?");
		
		stmt.setInt(1, idOrgao);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			UsuarioDAO dao = new UsuarioDAO();
			
			return dao.buscarPorId(rs.getInt("idPresidente"));
		}else{
			return null;
		}
	}
	
	public Usuario buscarSecretario(int idOrgao) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement(
				"SELECT idSecretario FROM orgaos WHERE idOrgao = ?");
		
		stmt.setInt(1, idOrgao);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			UsuarioDAO dao = new UsuarioDAO();
			
			return dao.buscarPorId(rs.getInt("idSecretario"));
		}else{
			return null;
		}
	}
	
	public boolean isMembro(int idOrgao, int idUsuario) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement(
				"SELECT * FROM membros WHERE idOrgao = ? AND idUsuario=?");
		
		stmt.setInt(1, idOrgao);
		stmt.setInt(2, idUsuario);
		
		ResultSet rs = stmt.executeQuery();
		
		return rs.next();
	}
	
	public int salvar(Orgao orgao) throws SQLException{
		boolean insert = (orgao.getIdOrgao() == 0);
		
		ConnectionDAO.getInstance().getConnection().setAutoCommit(false);
		
		try{
			PreparedStatement stmt;
			
			if(insert){
				stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("INSERT INTO orgaos(idDepartamento, idPresidente, idSecretario, nome, nomeCompleto, designacaoPresidente, ativo) VALUES(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			}else{
				stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("UPDATE orgaos SET idDepartamento=?, idPresidente=?, idSecretario=?, nome=?, nomeCompleto=?, designacaoPresidente=?, ativo=? WHERE idOrgao=?");
			}
			
			stmt.setInt(1, orgao.getDepartamento().getIdDepartamento());
			stmt.setInt(2, orgao.getPresidente().getIdUsuario());
			stmt.setInt(3, orgao.getSecretario().getIdUsuario());
			stmt.setString(4, orgao.getNome());
			stmt.setString(5, orgao.getNomeCompleto());
			stmt.setString(6, orgao.getDesignacaoPresidente());
			stmt.setInt(7, (orgao.isAtivo() ? 1 : 0));
			
			if(!insert){
				stmt.setInt(8, orgao.getIdOrgao());
			}
			
			stmt.execute();
			
			if(insert){
				ResultSet rs = stmt.getGeneratedKeys();
				
				if(rs.next()){
					orgao.setIdOrgao(rs.getInt(1));
				}
			}
			
			stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("DELETE FROM membros WHERE idOrgao=" + String.valueOf(orgao.getIdOrgao()));
			stmt.execute();
			
			for(OrgaoMembro u : orgao.getMembros()){
				stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("INSERT INTO membros(idOrgao, idUsuario, designacao) VALUES(?, ?, ?)");
				
				stmt.setInt(1, orgao.getIdOrgao());
				stmt.setInt(2, u.getUsuario().getIdUsuario());
				stmt.setString(3, u.getDesignacao());
				
				stmt.execute();
			}
			
			ConnectionDAO.getInstance().getConnection().commit();
			
			return orgao.getIdOrgao();
		}catch(SQLException e){
			ConnectionDAO.getInstance().getConnection().rollback();
			
			throw e;
		}finally{
			ConnectionDAO.getInstance().getConnection().setAutoCommit(true);
		}
	}
	
	private Orgao carregarObjeto(ResultSet rs) throws SQLException{
		Orgao orgao = new Orgao();
		
		orgao.setIdOrgao(rs.getInt("idOrgao"));
		orgao.getDepartamento().setIdDepartamento(rs.getInt("idDepartamento"));
		orgao.getDepartamento().setNome(rs.getString("departamento"));
		orgao.getPresidente().setIdUsuario(rs.getInt("idPresidente"));
		orgao.getPresidente().setNome(rs.getString("presidente"));
		orgao.getSecretario().setIdUsuario(rs.getInt("idSecretario"));
		orgao.getSecretario().setNome(rs.getString("secretario"));
		orgao.setNome(rs.getString("nome"));
		orgao.setNomeCompleto(rs.getString("nomeCompleto"));
		orgao.setDesignacaoPresidente(rs.getString("designacaoPresidente"));
		orgao.setAtivo(rs.getInt("ativo") == 1);
		
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs2 = stmt.executeQuery("SELECT membros.*, usuarios.nome FROM membros " +
				"INNER JOIN usuarios ON usuarios.idUsuario=membros.idUsuario " +
				"WHERE idOrgao=" + String.valueOf(orgao.getIdOrgao()) + " ORDER BY usuarios.nome");
		while(rs2.next()){
			OrgaoMembro membro = new OrgaoMembro();
			
			membro.getUsuario().setIdUsuario(rs2.getInt("idUsuario"));
			membro.getUsuario().setNome(rs2.getString("nome"));
			membro.setDesignacao(rs2.getString("designacao"));
			
			orgao.getMembros().add(membro);
		}
		
		return orgao;
	}

}
