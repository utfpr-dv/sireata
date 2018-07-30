package br.edu.utfpr.dv.sireata.window;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.UsuarioBO;
import br.edu.utfpr.dv.sireata.model.Usuario;
import br.edu.utfpr.dv.sireata.util.StringUtils;
import br.edu.utfpr.dv.sireata.view.ListView;

public class EditarUsuarioWindow extends EditarWindow {
	
	private final Usuario usuario;
	
	private final TextField tfLogin;
	private final TextField tfNome;
	private final TextField tfEmail;
	private final CheckBox cbExterno;
	private final CheckBox cbAtivo;
	private final CheckBox cbAdministrador;

	public EditarUsuarioWindow(Usuario usuario, ListView parentView){
		super("Editar Usuário", parentView);
		
		if(usuario == null){
			this.usuario = new Usuario();
		}else{
			this.usuario = usuario;
		}
		
		this.tfLogin = new TextField("Login");
		this.tfLogin.setWidth("400px");
		this.tfLogin.setMaxLength(50);
		
		this.tfNome = new TextField("Nome");
		this.tfNome.setWidth("400px");
		this.tfNome.setMaxLength(100);
		
		this.tfEmail = new TextField("E-mail");
		this.tfEmail.setWidth("400px");
		this.tfEmail.setMaxLength(100);
		
		this.cbExterno = new CheckBox("Usuário externo");
		this.cbExterno.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				configurarUsuarioExterno(cbExterno.getValue());
			}
		});
		
		this.cbAtivo = new CheckBox("Ativo");
		
		this.cbAdministrador = new CheckBox("Administrador");
		
		if(Session.isAdministrador()){
			this.adicionarCampo(new HorizontalLayout(this.tfLogin, new VerticalLayout(this.cbAtivo, this.cbExterno, this.cbAdministrador)));	
		}else{
			this.tfNome.setEnabled(false);
		}
		this.adicionarCampo(new HorizontalLayout(this.tfNome, this.tfEmail));
		
		this.carregarUsuario();
		this.tfLogin.focus();
	}
	
	private void carregarUsuario(){
		this.tfNome.setValue(this.usuario.getNome());
		this.tfEmail.setValue(this.usuario.getEmail());
		
		if(Session.isAdministrador()){
			this.tfLogin.setValue(this.usuario.getLogin());
			this.cbExterno.setValue(this.usuario.isExterno());
			this.cbAtivo.setValue(this.usuario.isAtivo());
			this.cbAdministrador.setValue(this.usuario.isAdministrador());
		}
		
		this.configurarUsuarioExterno(this.usuario.isExterno());
	}
	
	private void configurarUsuarioExterno(boolean external){
		if(external){
			this.tfNome.setEnabled(true);
		}else{
			this.tfNome.setEnabled(false);
		}
	}
	
	@Override
	public void salvar() {
		try{
			UsuarioBO bo = new UsuarioBO();
			
			if(this.cbExterno.getValue()){
				this.usuario.setNome(this.tfNome.getValue());
			}
			
			this.usuario.setEmail(this.tfEmail.getValue());
			
			if(Session.isAdministrador()){
				this.usuario.setLogin(this.tfLogin.getValue());
				this.usuario.setExterno(this.cbExterno.getValue());
				this.usuario.setAtivo(this.cbAtivo.getValue());
				this.usuario.setAdministrador(this.cbAdministrador.getValue());
				
				if(this.usuario.getSenha().isEmpty()){
					this.usuario.setSenha(StringUtils.generateSHA3Hash(this.usuario.getLogin()));
				}
			}
			
			bo.salvar(usuario);
			
			Notification.show("Salvar Usuário", "Usuário salvo com sucesso.", Notification.Type.HUMANIZED_MESSAGE);
			
			this.atualizarGridPai();
			this.close();
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Salvar Usuário", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

}
