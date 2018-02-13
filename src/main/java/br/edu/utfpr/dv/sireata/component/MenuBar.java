package br.edu.utfpr.dv.sireata.component;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

import com.vaadin.ui.UI;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.UsuarioBO;
import br.edu.utfpr.dv.sireata.view.AtaView;
import br.edu.utfpr.dv.sireata.view.CampusView;
import br.edu.utfpr.dv.sireata.view.DepartamentoView;
import br.edu.utfpr.dv.sireata.view.LoginView;
import br.edu.utfpr.dv.sireata.view.OrgaoView;
import br.edu.utfpr.dv.sireata.view.UsuarioView;
import br.edu.utfpr.dv.sireata.window.EditarAtaWindow;
import br.edu.utfpr.dv.sireata.window.EditarSenhaWindow;
import br.edu.utfpr.dv.sireata.window.EditarUsuarioWindow;
import br.edu.utfpr.dv.sireata.window.SobreWindow;

public class MenuBar extends CustomComponent {
	
	private final com.vaadin.ui.MenuBar menu;
	
	public MenuBar(){
		this.menu = new com.vaadin.ui.MenuBar();
		boolean podeCriarAta = false;
		
		try {
			UsuarioBO bo = new UsuarioBO();
			podeCriarAta = bo.podeCriarAta(Session.getUsuario().getIdUsuario());
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
		
		HorizontalLayout layout = new HorizontalLayout(this.menu);
		
		MenuItem modules = this.menu.addItem("Atas", null);
		if(podeCriarAta){
			modules.addItem("Nova Ata", new Command(){
	    	    @Override
	    	    public void menuSelected(MenuItem selectedItem){
	    	    	UI.getCurrent().addWindow(new EditarAtaWindow(null, null));
	    	    }
	    	});
		}
		modules.addItem("Atas em Aberto", new Command(){
    	    @Override
    	    public void menuSelected(MenuItem selectedItem){
    	        UI.getCurrent().getNavigator().navigateTo(AtaView.NAME + "/0");
    	    }
    	});
		modules.addItem("Atas Publicadas", new Command(){
    	    @Override
    	    public void menuSelected(MenuItem selectedItem){
    	    	UI.getCurrent().getNavigator().navigateTo(AtaView.NAME + "/1");
    	    }
    	});
		
		if(Session.isAdministrador()){
			MenuItem administration = this.menu.addItem("Administração", null);
			administration.addItem("Câmpus", new Command(){
	    	    @Override
	    	    public void menuSelected(MenuItem selectedItem){
	    	        UI.getCurrent().getNavigator().navigateTo(CampusView.NAME);
	    	    }
	    	});
			administration.addItem("Departamentos", new Command(){
	    	    @Override
	    	    public void menuSelected(MenuItem selectedItem){
	    	        UI.getCurrent().getNavigator().navigateTo(DepartamentoView.NAME);
	    	    }
	    	});
			administration.addItem("Órgãos", new Command(){
	    	    @Override
	    	    public void menuSelected(MenuItem selectedItem){
	    	        UI.getCurrent().getNavigator().navigateTo(OrgaoView.NAME);
	    	    }
	    	});
			administration.addSeparator();
    		administration.addItem("Usuários", new Command(){
        	    @Override
        	    public void menuSelected(MenuItem selectedItem){
        	        UI.getCurrent().getNavigator().navigateTo(UsuarioView.NAME);
        	    }
        	});
		}
		
		MenuItem account = this.menu.addItem("Minha Conta", null);
    	account.addItem("Meus Dados", new Command(){
    	    @Override
    	    public void menuSelected(MenuItem selectedItem){
    	    	UI.getCurrent().addWindow(new EditarUsuarioWindow(Session.getUsuario(), null));
    	    }
    	});
		account.addSeparator();
    	if((Session.getUsuario() != null) && Session.getUsuario().isExterno()){
    		account.addItem("Alterar Senha", new Command(){
        	    @Override
        	    public void menuSelected(MenuItem selectedItem){
        	    	UI.getCurrent().addWindow(new EditarSenhaWindow());
        	    }
        	});
    		account.addSeparator();
    	}
    	account.addItem("Logoff", new Command(){
    	    @Override
    	    public void menuSelected(MenuItem selectedItem){
    	        logoff();
    	    }
    	});
    	
    	MenuItem help = this.menu.addItem("", new ThemeResource("images/help.png"), null);
    	/*help.addItem("Reportar Erro", new Command(){
    	    @Override
    	    public void menuSelected(MenuItem selectedItem){
    	    	//UI.getCurrent().getNavigator().navigateTo(BugReportView.NAME);
    	    }
    	});
    	help.addSeparator();*/
    	help.addItem("Sobre o Sistema", new Command(){
    	    @Override
    	    public void menuSelected(MenuItem selectedItem){
    	    	UI.getCurrent().addWindow(new SobreWindow());
    	    }
    	});
    	
    	this.setCompositionRoot(layout);
	}
	
	private void logoff(){
    	// "Logout" the user
        getSession().setAttribute("usuario", null);

        // Refresh this view, should redirect to login view
        getUI().getNavigator().navigateTo(LoginView.NAME);
    }

}
