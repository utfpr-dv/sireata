package br.edu.utfpr.dv.sireata.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sireata.model.AtaParticipante;

public class AtaParticipanteDAO {
	
	public AtaParticipante buscarPorId(int id) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("SELECT ataparticipantes.*, usuarios.nome AS nomeParticipante FROM ataparticipantes " +
				"INNER JOIN usuarios ON usuarios.idUsuario=ataparticipantes.idUsuario " +
				"WHERE idAtaParticipante = ?");
		
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			return this.carregarObjeto(rs);
		}else{
			return null;
		}
	}
	
	public List<AtaParticipante> listarPorAta(int idAta) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT ataparticipantes.*, usuarios.nome AS nomeParticipante FROM ataparticipantes " +
				"INNER JOIN usuarios ON usuarios.idUsuario=ataparticipantes.idUsuario " + 
				"WHERE idAta=" + String.valueOf(idAta) + " ORDER BY usuarios.nome");
		
		List<AtaParticipante> list = new ArrayList<AtaParticipante>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public int salvar(AtaParticipante participante) throws SQLException{
		boolean insert = (participante.getIdAtaParticipante() == 0);
		PreparedStatement stmt;
		
		if(insert){
			stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("INSERT INTO ataparticipantes(idAta, idUsuario, presente, motivo, designacao) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		}else{
			stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("UPDATE ataparticipantes SET idAta=?, idUsuario=?, presente=?, motivo=?, designacao=? WHERE idAtaParticipante=?");
		}
		
		stmt.setInt(1, participante.getAta().getIdAta());
		stmt.setInt(2, participante.getParticipante().getIdUsuario());
		stmt.setInt(3, (participante.isPresente() ? 1 : 0));
		stmt.setString(4, participante.getMotivo());
		stmt.setString(5, participante.getDesignacao());
		
		if(!insert){
			stmt.setInt(6, participante.getIdAtaParticipante());
		}
		
		stmt.execute();
		
		if(insert){
			ResultSet rs = stmt.getGeneratedKeys();
			
			if(rs.next()){
				participante.setIdAtaParticipante(rs.getInt(1));
			}
		}
		
		return participante.getIdAtaParticipante();
	}
	
	public void excluir(int id) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		stmt.execute("DELETE FROM ataparticipantes WHERE idAtaParticipante=" + String.valueOf(id));
	}
	
	private AtaParticipante carregarObjeto(ResultSet rs) throws SQLException{
		AtaParticipante participante = new AtaParticipante();
		
		participante.setIdAtaParticipante(rs.getInt("idAtaParticipante"));
		participante.getAta().setIdAta(rs.getInt("idAta"));
		participante.getParticipante().setIdUsuario(rs.getInt("idUsuario"));
		participante.getParticipante().setNome(rs.getString("nomeParticipante"));
		participante.setPresente(rs.getInt("presente") == 1);
		participante.setMotivo(rs.getString("motivo"));
		participante.setDesignacao(rs.getString("designacao"));
		
		return participante;
	}

}
