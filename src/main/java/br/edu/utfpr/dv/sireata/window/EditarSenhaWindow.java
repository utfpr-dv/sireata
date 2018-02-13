package br.edu.utfpr.dv.sireata.window;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.UsuarioBO;
import br.edu.utfpr.dv.sireata.model.Usuario;

public class EditarSenhaWindow extends EditarWindow {
	
	private final PasswordField tfSenhaAtual;
	private final PasswordField tfNovaSenha;
	private final PasswordField tfConfirmacaoSenha;
	
	public EditarSenhaWindow(){
		super("Alterar Senha", null);
		
		this.tfSenhaAtual = new PasswordField("Senha Atual");
		this.tfSenhaAtual.setWidth("400px");
		
		this.tfNovaSenha = new PasswordField("Nova Senha");
		this.tfNovaSenha.setWidth("400px");
		
		this.tfConfirmacaoSenha = new PasswordField("Confirmação de Senha");
		this.tfConfirmacaoSenha.setWidth("400px");
		
		this.adicionarCampo(this.tfSenhaAtual);
		this.adicionarCampo(this.tfNovaSenha);
		this.adicionarCampo(this.tfConfirmacaoSenha);
		
		this.tfSenhaAtual.focus();
	}
	
	@Override
	public void salvar() {
		try {
			if(!this.tfNovaSenha.getValue().equals(this.tfConfirmacaoSenha.getValue())){
				this.tfNovaSenha.focus();
				throw new Exception("As senhas não conferem.");
			}
			
			UsuarioBO bo = new UsuarioBO();
			Usuario usuario = bo.alterarSenha(Session.getUsuario().getIdUsuario(), this.tfSenhaAtual.getValue(), this.tfNovaSenha.getValue());
			
			Session.setUsuario(usuario);
			
			Notification.show("Alterar Senha", "Senha alterada com sucesso.", Notification.Type.HUMANIZED_MESSAGE);
			
			this.close();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Alterar Senha", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

}
