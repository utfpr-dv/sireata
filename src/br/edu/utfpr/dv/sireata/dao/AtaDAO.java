package br.edu.utfpr.dv.sireata.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sireata.model.Ata;
import br.edu.utfpr.dv.sireata.model.Ata.TipoAta;

public class AtaDAO {
	
	public Ata buscarPorId(int id) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement(
					"SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
					"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
					"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
					"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
					"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
					"WHERE idAta = ?");
		
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			return this.carregarObjeto(rs);
		}else{
			return null;
		}
	}
	
	public Ata buscarPorPauta(int idPauta) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement(
				"SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"INNER JOIN pautas ON pautas.idAta=atas.idAta " +
				"WHERE pautas.idPauta = ?");
	
	stmt.setInt(1, idPauta);
	
	ResultSet rs = stmt.executeQuery();
	
	if(rs.next()){
		return this.carregarObjeto(rs);
	}else{
		return null;
	}
	}
	
	public int buscarProximoNumeroAta(int idOrgao, int ano, TipoAta tipo) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement(
				"SELECT MAX(numero) AS numero FROM atas WHERE idOrgao = ? AND YEAR(data) = ? AND tipo = ?");
		
		stmt.setInt(1, idOrgao);
		stmt.setInt(2, ano);
		stmt.setInt(3, tipo.getValue());
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			return rs.getInt("numero") + 1;
		}else{
			return 1;
		}
	}
	
	public List<Ata> listar(int idUsuario, int idCampus, int idDepartamento, int idOrgao, boolean publicadas) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN ataparticipantes ON ataparticipantes.idAta=atas.idAta " +
				"INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE ataparticipantes.idUsuario = " + String.valueOf(idUsuario) +
				" AND atas.publicada = " + (publicadas ? "1 " : "0 ") +
				(idCampus > 0 ? " AND departamentos.idCampus = " + String.valueOf(idCampus) : "") +
				(idDepartamento > 0 ? " AND departamentos.idDepartamento = " + String.valueOf(idDepartamento) : "") +
				(idOrgao > 0 ? " AND atas.idOrgao = " + String.valueOf(idOrgao) : "") +
				"ORDER BY atas.data DESC");
		
		List<Ata> list = new ArrayList<Ata>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Ata> listarPublicadas() throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE atas.publicada=1 ORDER BY atas.data DESC");
		
		List<Ata> list = new ArrayList<Ata>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Ata> listarPorOrgao(int idOrgao) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE atas.publicada=1 AND atas.idOrgao=" + String.valueOf(idOrgao) + " ORDER BY atas.data DESC");
		
		List<Ata> list = new ArrayList<Ata>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Ata> listarPorDepartamento(int idDepartamento) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE atas.publicada=1 AND Orgaos.idDepartamento=" + String.valueOf(idDepartamento) + " ORDER BY atas.data DESC");
		
		List<Ata> list = new ArrayList<Ata>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Ata> listarPorCampus(int idCampus) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"WHERE atas.publicada=1 AND departamentos.idCampus=" + String.valueOf(idCampus) + " ORDER BY atas.data DESC");
		
		List<Ata> list = new ArrayList<Ata>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Ata> listarNaoPublicadas(int idUsuario) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"INNER JOIN ataparticipantes ON ataparticipantes.idAta=atas.idAta " +
				"WHERE atas.publicada=0 AND ataparticipantes.idUsuario=" + String.valueOf(idUsuario) +" ORDER BY atas.data DESC");
		
		List<Ata> list = new ArrayList<Ata>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Ata> listarPorOrgao(int idOrgao, int idUsuario) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"INNER JOIN ataparticipantes ON ataparticipantes.idAta=atas.idAta " +
				"WHERE atas.publicada=0 AND ataparticipantes.idUsuario=" + String.valueOf(idUsuario) + " AND atas.idOrgao=" + String.valueOf(idOrgao) + " ORDER BY atas.data DESC");
		
		List<Ata> list = new ArrayList<Ata>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Ata> listarPorDepartamento(int idDepartamento, int idUsuario) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"INNER JOIN ataparticipantes ON ataparticipantes.idAta=atas.idAta " +
				"WHERE atas.publicada=0 AND ataparticipantes.idUsuario=" + String.valueOf(idUsuario) + " AND Orgaos.idDepartamento=" + String.valueOf(idDepartamento) + " ORDER BY atas.data DESC");
		
		List<Ata> list = new ArrayList<Ata>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public List<Ata> listarPorCampus(int idCampus, int idUsuario) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
				"FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
				"INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
				"INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
				"INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
				"INNER JOIN ataparticipantes ON ataparticipantes.idAta=atas.idAta " +
				"WHERE atas.publicada=0 AND ataparticipantes.idUsuario=" + String.valueOf(idUsuario) + " AND departamentos.idCampus=" + String.valueOf(idCampus) + " ORDER BY atas.data DESC");
		
		List<Ata> list = new ArrayList<Ata>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public int salvar(Ata ata) throws SQLException{
		boolean insert = (ata.getIdAta() == 0);
		PreparedStatement stmt;
		
		if(insert){
			stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("INSERT INTO atas(idOrgao, idPresidente, idSecretario, tipo, numero, data, local, localCompleto, dataLimiteComentarios, consideracoesIniciais, audio, documento, publicada, dataPublicacao, aceitarComentarios) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, 0, NULL, 0)", Statement.RETURN_GENERATED_KEYS);
		}else{
			stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("UPDATE atas SET idOrgao=?, idPresidente=?, idSecretario=?, tipo=?, numero=?, data=?, local=?, localCompleto=?, dataLimiteComentarios=?, consideracoesIniciais=?, audio=? WHERE idAta=?");
		}
		
		stmt.setInt(1, ata.getOrgao().getIdOrgao());
		stmt.setInt(2, ata.getPresidente().getIdUsuario());
		stmt.setInt(3, ata.getSecretario().getIdUsuario());
		stmt.setInt(4, ata.getTipo().getValue());
		stmt.setInt(5, ata.getNumero());
		stmt.setTimestamp(6, new java.sql.Timestamp(ata.getData().getTime()));
		stmt.setString(7, ata.getLocal());
		stmt.setString(8, ata.getLocalCompleto());
		stmt.setDate(9, new java.sql.Date(ata.getDataLimiteComentarios().getTime()));
		stmt.setString(10, ata.getConsideracoesIniciais());
		if(ata.getAudio() == null){
			stmt.setNull(11, Types.BLOB);
		}else{
			stmt.setBytes(11, ata.getAudio());	
		}
		
		if(!insert){
			stmt.setInt(12, ata.getIdAta());
		}
		
		stmt.execute();
		
		if(insert){
			ResultSet rs = stmt.getGeneratedKeys();
			
			if(rs.next()){
				ata.setIdAta(rs.getInt(1));
			}
		}
		
		return ata.getIdAta();
	}
	
	public void publicar(int idAta, byte[] documento) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("UPDATE atas SET documento=?, publicada=1, aceitarComentarios=0 WHERE publicada=0 AND idAta=?");
		
		stmt.setBytes(1, documento);
		stmt.setInt(2, idAta);
		
		stmt.execute();
	}
	
	public void liberarComentarios(int idAta) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		stmt.execute("UPDATE atas SET aceitarComentarios=1 WHERE publicada=0 AND idAta=" + String.valueOf(idAta));
	}
	
	public void bloquearComentarios(int idAta) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		stmt.execute("UPDATE atas SET aceitarComentarios=0 WHERE idAta=" + String.valueOf(idAta));
	}
	
	private Ata carregarObjeto(ResultSet rs) throws SQLException{
		Ata ata = new Ata();
		
		ata.setIdAta(rs.getInt("idAta"));
		ata.getOrgao().setIdOrgao(rs.getInt("idOrgao"));
		ata.getOrgao().setNome(rs.getString("orgao"));
		ata.getPresidente().setIdUsuario(rs.getInt("idPresidente"));
		ata.getPresidente().setNome(rs.getString("presidente"));
		ata.getSecretario().setIdUsuario(rs.getInt("idSecretario"));
		ata.getSecretario().setNome(rs.getString("secretario"));
		ata.setTipo(TipoAta.valueOf(rs.getInt("tipo")));
		ata.setNumero(rs.getInt("numero"));
		ata.setData(rs.getTimestamp("data"));
		ata.setLocal(rs.getString("local"));
		ata.setLocalCompleto(rs.getString("localCompleto"));
		ata.setDataLimiteComentarios(rs.getDate("dataLimiteComentarios"));
		ata.setConsideracoesIniciais(rs.getString("consideracoesIniciais"));
		ata.setAudio(rs.getBytes("audio"));
		ata.setPublicada(rs.getInt("publicada") == 1);
		ata.setAceitarComentarios(rs.getInt("aceitarComentarios") == 1);
		ata.setDataPublicacao(rs.getDate("dataPublicacao"));
		ata.setDocumento(rs.getBytes("documento"));
		
		return ata;
	}
	
	public boolean temComentarios(int idAta) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT COUNT(comentarios.idComentario) AS qtde FROM comentarios " +
				"INNER JOIN pautas ON pautas.idPauta=comentarios.idPauta " + 
				"WHERE pautas.idAta=" + String.valueOf(idAta));
		
		if(rs.next()){
			return (rs.getInt("qtde") > 0);
		}else{
			return false;
		}
	}
	
	public boolean isPresidenteOuSecretario(int idUsuario, int idAta) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT atas.idAta FROM atas " +
				"WHERE idAta=" + String.valueOf(idAta) + " AND (idPresidente=" + String.valueOf(idUsuario) + " OR idSecretario=" + String.valueOf(idUsuario) + ")");
		
		return rs.next();
	}

}
