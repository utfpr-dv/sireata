package br.edu.utfpr.dv.sireata.component;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.UsuarioBO;
import br.edu.utfpr.dv.sireata.model.Usuario;
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

public class SideMenu extends CustomComponent {
	
	public static final String ID = "dashboard-menu";
    public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
    public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    
    private MenuItem settingsItem;
	private Accordion accordionMenu;
	
	public SideMenu() {
        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();
        
        setCompositionRoot(buildContent());
	}
	
	private Component buildContent() {
		final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(this.buildUserMenu());
        
        this.accordionMenu = new Accordion();
        
        this.accordionMenu.addTab(this.buildMenuAtas(), "Gestão de Atas");
        
        menuContent.addComponent(this.accordionMenu);
        
        return menuContent;
    }
	
	private Component buildUserMenu() {
        final com.vaadin.ui.MenuBar settings = new com.vaadin.ui.MenuBar();
        final Usuario usuario = Session.getUsuario();
        
        settings.addStyleName("user-menu");
        
        settingsItem = settings.addItem("", new ThemeResource("images/profile-pic-300px.jpg"), null);
        settingsItem.setText(usuario.getNome());
        
        settingsItem.addItem("Meus Dados", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
            	UI.getCurrent().addWindow(new EditarUsuarioWindow(usuario, null));
            }
        });
        if((Session.getUsuario() != null) && Session.getUsuario().isExterno()){
	        settingsItem.addItem("Alterar Senha", new Command() {
	            @Override
	            public void menuSelected(final MenuItem selectedItem) {
	            	UI.getCurrent().addWindow(new EditarSenhaWindow());
	            }
	        });
        }
        settingsItem.addSeparator();
        settingsItem.addItem("Logoff", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                logoff();
            }
        });
        
        return settings;
    }
	
	private Component buildMenuAtas(){
		boolean podeCriarAta = false;
		
		try {
			UsuarioBO bo = new UsuarioBO();
			podeCriarAta = bo.podeCriarAta(Session.getUsuario().getIdUsuario());
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
		
		VerticalLayout layout = new VerticalLayout();
		
		layout.addComponent(new MenuEntry("Atas", 0));
		if(podeCriarAta){
			layout.addComponent(new MenuEntry("Nova Ata", 1, new EditarAtaWindow(null, null)));
		}
		layout.addComponent(new MenuEntry("Atas em Aberto", 1, AtaView.NAME + "/0"));
		layout.addComponent(new MenuEntry("Atas Publicadas", 1, AtaView.NAME + "/1"));
		
		if(Session.isAdministrador()){
			layout.addComponent(new MenuEntry("Administração", 0));
			
			layout.addComponent(new MenuEntry("Câmpus", 1, CampusView.NAME));
			layout.addComponent(new MenuEntry("Departamentos", 1, DepartamentoView.NAME));
			layout.addComponent(new MenuEntry("Órgãos", 1, OrgaoView.NAME));
			layout.addComponent(new MenuEntry("Usuários", 1, UsuarioView.NAME));
		}
		
		layout.addComponent(new MenuEntry("Ajuda", 0));
		layout.addComponent(new MenuEntry("Sobre o Sistema", 1, new SobreWindow()));
		
		layout.setSizeFull();
		
		return layout;
	}

	private void logoff(){
    	// "Logout" the user
        getSession().setAttribute("usuario", null);

        // Refresh this view, should redirect to login view
        getUI().getNavigator().navigateTo(LoginView.NAME);
    }
	
}
