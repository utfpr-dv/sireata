package br.edu.utfpr.dv.sireata.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sireata.model.Comentario;
import br.edu.utfpr.dv.sireata.model.Comentario.SituacaoComentario;

public class ComentarioDAO {
	
	public Comentario buscarPorId(int id) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("SELECT * FROM comentarios WHERE idComentario = ?");
		
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			return this.carregarObjeto(rs);
		}else{
			return null;
		}
	}
	
	public Comentario buscarPorUsuario(int idUsuario, int idPauta) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT comentarios.*, usuarios.nome AS nomeUsuario FROM comentarios " +
				"INNER JOIN usuarios ON usuarios.idUsuario=comentarios.idUsuario " +
				"WHERE comentarios.idPauta=" + String.valueOf(idPauta) + " AND comentarios.idUsuario=" + String.valueOf(idUsuario));
		
		if(rs.next()){
			return this.carregarObjeto(rs);
		}else{
			return null;
		}
	}
	
	public List<Comentario> listarPorPauta(int idPauta) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT comentarios.*, usuarios.nome AS nomeUsuario FROM comentarios " +
				"INNER JOIN usuarios ON usuarios.idUsuario=comentarios.idUsuario " +
				"WHERE comentarios.idPauta=" + String.valueOf(idPauta) + " ORDER BY usuarios.nome");
		
		List<Comentario> list = new ArrayList<Comentario>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public int salvar(Comentario comentario) throws SQLException{
		boolean insert = (comentario.getIdComentario() == 0);
		PreparedStatement stmt;
		
		if(insert){
			stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("INSERT INTO comentarios(idPauta, idUsuario, situacao, comentarios, situacaoComentarios, motivo) VALUES(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		}else{
			stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("UPDATE comentarios SET idPauta=?, idUsuario=?, situacao=?, comentarios=?, situacaoComentarios=?, motivo=? WHERE idComentario=?");
		}
		
		stmt.setInt(1, comentario.getPauta().getIdPauta());
		stmt.setInt(2, comentario.getUsuario().getIdUsuario());
		stmt.setInt(3, comentario.getSituacao().getValue());
		stmt.setString(4, comentario.getComentarios());
		stmt.setInt(5, comentario.getSituacaoComentarios().getValue());
		stmt.setString(6, comentario.getMotivo());
		
		if(!insert){
			stmt.setInt(7, comentario.getIdComentario());
		}
		
		stmt.execute();
		
		if(insert){
			ResultSet rs = stmt.getGeneratedKeys();
			
			if(rs.next()){
				comentario.setIdComentario(rs.getInt(1));
			}
		}
		
		return comentario.getIdComentario();
	}
	
	private Comentario carregarObjeto(ResultSet rs) throws SQLException{
		Comentario comentario = new Comentario();
		
		comentario.setIdComentario(rs.getInt("idComentario"));
		comentario.getPauta().setIdPauta(rs.getInt("idPauta"));
		comentario.getUsuario().setIdUsuario(rs.getInt("idUsuario"));
		comentario.getUsuario().setNome(rs.getString("nomeUsuario"));
		comentario.setSituacao(SituacaoComentario.valueOf(rs.getInt("situacao")));
		comentario.setComentarios(rs.getString("comentarios"));
		comentario.setSituacaoComentarios(SituacaoComentario.valueOf(rs.getInt("situacaoComentarios")));
		comentario.setMotivo(rs.getString("motivo"));
		
		return comentario;
	}

}
