package br.edu.utfpr.dv.sireata.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sireata.model.Usuario;

public class UsuarioDAO {
	
	public Usuario buscarPorLogin(String login) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("SELECT * FROM usuarios WHERE login = ?");
		
		stmt.setString(1, login);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			return this.carregarObjeto(rs);
		}else{
			return null;
		}
	}
	
	public Usuario buscarPorId(int id) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("SELECT * FROM usuarios WHERE idUsuario = ?");
		
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			return this.carregarObjeto(rs);
		}else{
			return null;
		}
	}
	
	public String buscarEmail(int id) throws SQLException{
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("SELECT email FROM usuarios WHERE idUsuario = ?");
		
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			return rs.getString("email");
		}else{
			return "";
		}
	}
	
	public List<Usuario> listarTodos(boolean apenasAtivos) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios WHERE login <> 'admin' " + (apenasAtivos ? " AND ativo = 1 " : "") + " ORDER BY nome");
		List<Usuario> list = new ArrayList<Usuario>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));			
		}
		
		return list;
	}
	
	public List<Usuario> listar(String nome, boolean apenasAtivos, boolean apenasExternos) throws SQLException {
		String sql = "SELECT * FROM usuarios WHERE login <> 'admin' " + 
				(!nome.isEmpty() ? " AND nome LIKE ? " : "") +
				(apenasAtivos ? " AND ativo = 1 " : "") +
				(apenasExternos ? " AND externo = 1 " : "") +
				"ORDER BY nome";
		PreparedStatement stmt = ConnectionDAO.getInstance().getConnection().prepareStatement(sql);
		
		if(!nome.isEmpty()){
			stmt.setString(1, "%" + nome + "%");
		}
		
		ResultSet rs = stmt.executeQuery();
		List<Usuario> list = new ArrayList<Usuario>();
		
		while(rs.next()){
			list.add(this.carregarObjeto(rs));
		}
		
		return list;
	}
	
	public int salvar(Usuario usuario) throws SQLException{
		boolean insert = (usuario.getIdUsuario() == 0);
		PreparedStatement stmt;
		
		if(insert){
			stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("INSERT INTO usuarios(nome, login, senha, email, externo, ativo, administrador) VALUES(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		}else{
			stmt = ConnectionDAO.getInstance().getConnection().prepareStatement("UPDATE usuarios SET nome=?, login=?, senha=?, email=?, externo=?, ativo=?, administrador=? WHERE idUsuario=?");
		}
		
		stmt.setString(1, usuario.getNome());
		stmt.setString(2, usuario.getLogin());
		stmt.setString(3, usuario.getSenha());
		stmt.setString(4, usuario.getEmail());
		stmt.setInt(5, usuario.isExterno() ? 1 : 0);
		stmt.setInt(6, usuario.isAtivo() ? 1 : 0);
		stmt.setInt(7, usuario.isAdministrador() ? 1 : 0);
		
		if(!insert){
			stmt.setInt(8, usuario.getIdUsuario());
		}
		
		stmt.execute();
		
		if(insert){
			ResultSet rs = stmt.getGeneratedKeys();
			
			if(rs.next()){
				usuario.setIdUsuario(rs.getInt(1));
			}
		}
		
		return usuario.getIdUsuario();
	}
	
	private Usuario carregarObjeto(ResultSet rs) throws SQLException{
		Usuario usuario = new Usuario();
		
		usuario.setIdUsuario(rs.getInt("idUsuario"));
		usuario.setNome(rs.getString("nome"));
		usuario.setLogin(rs.getString("login"));
		usuario.setSenha(rs.getString("senha"));
		usuario.setEmail(rs.getString("email"));
		usuario.setExterno(rs.getInt("externo") == 1);
		usuario.setAtivo(rs.getInt("ativo") == 1);
		usuario.setAdministrador(rs.getInt("administrador") == 1);
		
		return usuario;
	}
	
	public String[] buscarEmails(int[] ids) throws SQLException{
		String sql = "";
		
		for(int id : ids){
			if(sql == "")
				sql = String.valueOf(id);
			else
				sql = sql + ", " + String.valueOf(id);
		}
		
		if(sql != ""){
			List<String> emails = new ArrayList<String>();
			Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT email FROM usuarios WHERE idUsuario IN (" + sql + ")");
			
			while(rs.next()){
				emails.add(rs.getString("email"));
			}
			
			return (String[])emails.toArray();
		}else
			return null;
	}
	
	public boolean podeCriarAta(int idUsuario) throws SQLException{
		Statement stmt = ConnectionDAO.getInstance().getConnection().createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(orgaos.idOrgao) AS qtde FROM orgaos " +
				"WHERE idPresidente=" + String.valueOf(idUsuario) + " OR idSecretario=" + String.valueOf(idUsuario));
		
		if(rs.next()){
			return (rs.getInt("qtde") > 0);
		}else{
			return false;
		}
	}

}
