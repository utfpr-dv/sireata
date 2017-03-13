package br.edu.utfpr.dv.sireata.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.UsuarioBO;
import br.edu.utfpr.dv.sireata.model.Usuario;

import com.vaadin.ui.Button.ClickEvent;

public class LoginView extends CustomComponent implements View {
	
	public static final String NAME = "login";
	
	private final Label label;
	private final Label info;
	private final Label infoAluno;
	private final Label infoServidor;
    private final TextField user;
    private final PasswordField password;
    private final Button loginButton;
    
    public LoginView(){
    	this.setCaption("SIREATA - Sistema de Registro de Atas");
    	this.setResponsive(true);
    	
    	this.label = new Label("SIREATA - Sistema de Registro de Atas");
    	this.label.setStyleName("Title");
    	
    	this.info = new Label("Para acessar o SIREATA, efetue o login.");
    	
    	this.infoAluno = new Label("- Se você for aluno, no campo usuário coloque a letra \"a\" e o número do seu R.A. (por exemplo: a1234567 ou a1423599) e no campo senha insira a mesma senha do Sistema Acadêmico.");
    	
    	this.infoServidor = new Label("- Se você for servidor, no campo usuário coloque o seu nome de usuário utilizado para acessar os sistemas da UTFPR, e no campo senha informe a sua senha utilizada para acessar os sistemas da UTFPR.");
    	
    	VerticalLayout layoutInfo = new VerticalLayout(this.label, this.info, this.infoAluno, this.infoServidor);
    	layoutInfo.setSizeFull();
    	layoutInfo.setWidth("800px");
    	layoutInfo.setSpacing(true);
    	
    	this.user = new TextField("Usuário");
    	this.user.setWidth("300px");
    	this.user.setInputPrompt("Informe seu nome de usuário");
    	this.user.setInvalidAllowed(false);

    	this.password = new PasswordField("Senha");
    	this.password.setWidth("300px");
    	this.password.setInputPrompt("Informe sua senha");
    	this.password.setValue("");
    	this.password.setNullRepresentation("");
        
    	this.loginButton = new Button("Login", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	login();
            }
        });
    	this.loginButton.setClickShortcut(KeyCode.ENTER);
        
        VerticalLayout layoutLogin = new VerticalLayout(this.user, this.password, this.loginButton);
        layoutLogin.setSizeUndefined();
        layoutLogin.setResponsive(true);
        layoutLogin.setSpacing(true);
                        
        VerticalLayout fields = new VerticalLayout(layoutInfo, layoutLogin);
        fields.setComponentAlignment(layoutInfo, Alignment.MIDDLE_CENTER);
        fields.setComponentAlignment(layoutLogin, Alignment.MIDDLE_CENTER);
        fields.setSizeFull();
        fields.setResponsive(true);
        fields.setSpacing(true);

        setCompositionRoot(fields);
    }
	
    @Override
    public void enter(ViewChangeEvent event) {
        user.focus();
    }
    
    private void login(){
    	String username = this.user.getValue();
        String password = this.password.getValue();
        
        try{
        	UsuarioBO bo = new UsuarioBO();
        	Usuario usuario = bo.validarLogin(username, password);
        	
        	Session.setUsuario(usuario);
            getUI().getNavigator().navigateTo(MainView.NAME);
        }catch(Exception e){
        	Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
        	
        	this.password.setValue("");
            this.user.setValue("");
            this.user.focus();
            
            Notification.show("Login", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }
    
}