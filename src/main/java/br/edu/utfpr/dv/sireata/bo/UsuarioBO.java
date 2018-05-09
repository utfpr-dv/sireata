package br.edu.utfpr.dv.sireata.bo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.CommunicationException;

import br.edu.utfpr.dv.sireata.dao.UsuarioDAO;
import br.edu.utfpr.dv.sireata.ldap.LdapConfig;
import br.edu.utfpr.dv.sireata.ldap.LdapUtils;
import br.edu.utfpr.dv.sireata.model.Usuario;
import br.edu.utfpr.dv.sireata.util.StringUtils;

public class UsuarioBO {
	
	public List<Usuario> listarTodos(boolean apenasAtivos) throws Exception{
		try {
			UsuarioDAO dao = new UsuarioDAO();
			
			return dao.listarTodos(apenasAtivos);
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Usuario> listar(String nome, boolean apenasAtivos, boolean apenasExternos) throws Exception {
		try{
			UsuarioDAO dao = new UsuarioDAO();
			
			return dao.listar(nome.trim(), apenasAtivos, apenasExternos);
		}catch(SQLException e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public int salvar(Usuario usuario) throws Exception{
		try {
			if(usuario.getLogin().isEmpty()){
				throw new Exception("Informe o login.");
			}
			if(usuario.getSenha().isEmpty()){
				throw new Exception("Informe a senha.");
			}
			if(usuario.getNome().isEmpty()){
				throw new Exception("Informe o nome.");
			}
			
			UsuarioDAO dao = new UsuarioDAO();
			
			return dao.salvar(usuario);
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	private boolean loginEAluno(String login){
		if(login.toLowerCase().startsWith("a")){
			login = login.substring(1);
			
			try{
				Integer.parseInt(login);
				
				return true;
			}catch(Exception e){
				return false;
			}
		}
		
		return false;
	}
	
	public Usuario buscarPorLogin(String login) throws Exception{
		try {
			UsuarioDAO dao = new UsuarioDAO();
			
			return dao.buscarPorLogin(login);
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public Usuario buscarPorId(int id) throws Exception{
		try {
			UsuarioDAO dao = new UsuarioDAO();
			
			return dao.buscarPorId(id);
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public String buscarEmail(int id) throws Exception{
		try {
			UsuarioDAO dao = new UsuarioDAO();
			
			return dao.buscarEmail(id);
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public Usuario alterarSenha(int idUsuario, String senhaAtual, String novaSenha) throws Exception{
		try {
			String hashAtual = StringUtils.generateSHA3Hash(senhaAtual);
			String novoHash = StringUtils.generateSHA3Hash(novaSenha);
			
			UsuarioDAO dao = new UsuarioDAO();
			Usuario usuario = dao.buscarPorId(idUsuario);
			
			if(usuario == null){
				throw new Exception("Usuário não encontrado.");
			}
			if(!usuario.isExterno()){
				throw new Exception("A alteração de senha é permitida apenas para usuários externos. Acadêmicos e Professores devem alterar a senha através do Sistema Acadêmico.");
			}
			if(!usuario.getSenha().equals(hashAtual)){
				throw new Exception("A senha atual não confere.");
			}
			if(senhaAtual.equals(novaSenha)){
				throw new Exception("A nova senha não deve ser igual à senha atual.");
			}
			
			usuario.setSenha(novoHash);
			dao.salvar(usuario);
			
			return usuario;
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public Usuario validarLogin(String login, String senha) throws Exception{
		if(login.trim().isEmpty() || senha.trim().isEmpty()) {
    		throw new Exception("Informe o usuário e a senha.");
    	}
		
		String hash = StringUtils.generateSHA3Hash(senha);
		
		if(login.contains("@")){
			login = login.substring(0, login.indexOf("@"));
		}
		
		Usuario usuario = this.buscarPorLogin(login);
		
		if(usuario == null){
			usuario = new Usuario();
			usuario.setExterno(false);
		}
		
		if(usuario.isExterno()){
			if(!usuario.getSenha().equals(hash)){
				throw new Exception("Usuário ou senha inválidos.");
			}
		}else{
			LdapUtils ldapUtils = new LdapUtils(LdapConfig.getInstance().getHost(), 
					LdapConfig.getInstance().getPort(), LdapConfig.getInstance().isUseSSL(), 
					LdapConfig.getInstance().isIgnoreCertificates(), LdapConfig.getInstance().getBasedn(), 
					LdapConfig.getInstance().getUidField());
			
			try{
				try {
					ldapUtils.authenticate(login, senha);
					
					Map<String, String> dataLdap = ldapUtils.getLdapProperties(login);
	
					//String cnpjCpf = dataLdap.get(LdapConfig.getInstance().getCpfField());
					//String matricula = dataLdap.get(LdapConfig.getInstance().getRegisterField());
					String nome = this.formatarNome(dataLdap.get(LdapConfig.getInstance().getNameField()));
					String email = dataLdap.get(LdapConfig.getInstance().getEmailField());
					
					usuario.setSenha(hash);
					
					if(usuario.getIdUsuario() == 0){
						usuario.setNome(nome);
						usuario.setEmail(email);
						usuario.setLogin(login);
						usuario.setExterno(false);
					}
				} catch (CommunicationException e) {
					if(usuario.getIdUsuario() == 0) {
						throw new Exception("Não foi possível conectar ao servicor LDAP.");
					} else {
						if(!usuario.getSenha().equals(hash)){
							throw new Exception("Usuário ou senha inválidos.");
						}
					}
				}
				
				this.salvar(usuario);
			}catch(Exception e){
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
				
				throw new Exception("Usuário ou senha inválidos." + System.lineSeparator() + "Detalhes: " + e.getMessage());
			}
		}
		
		return usuario;
	}
	
	private String formatarNome(String nome){
		String[] list = nome.trim().split(" ");
		List<String> list2 = new ArrayList<String>();
		
		for(String s : list){
			if(s.length() > 2){
				s = s.charAt(0) + s.substring(1).toLowerCase();	
			}else{
				s = s.toLowerCase();
			}
			
			list2.add(s);
		}
		
		return String.join(" ", list2);
	}
	
	public String[] buscarEmails(int[] ids) throws Exception{
		UsuarioDAO dao = new UsuarioDAO();
		
		return dao.buscarEmails(ids);
	}
	
	public boolean podeCriarAta(int idUsuario) throws Exception{
		try {
			UsuarioDAO dao = new UsuarioDAO();
			
			return dao.podeCriarAta(idUsuario);
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
}
