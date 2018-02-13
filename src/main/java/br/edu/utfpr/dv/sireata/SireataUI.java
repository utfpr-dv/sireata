package br.edu.utfpr.dv.sireata;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;

import com.vaadin.ui.UI;

import br.edu.utfpr.dv.sireata.view.AtaPublicadaView;
import br.edu.utfpr.dv.sireata.view.AtaView;
import br.edu.utfpr.dv.sireata.view.CampusView;
import br.edu.utfpr.dv.sireata.view.DepartamentoView;
import br.edu.utfpr.dv.sireata.view.LoginView;
import br.edu.utfpr.dv.sireata.view.MainView;
import br.edu.utfpr.dv.sireata.view.OrgaoView;
import br.edu.utfpr.dv.sireata.view.PDFView;
import br.edu.utfpr.dv.sireata.view.UsuarioView;

@SuppressWarnings("serial")
@Theme("facebook")
public class SireataUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = SireataUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		//
        // Create a new instance of the navigator. The navigator will attach
        // itself automatically to this view.
        //
        new Navigator(this, this);
        getNavigator().addView("", MainView.class);
        getNavigator().addView(MainView.NAME, MainView.class);
        getNavigator().addView(LoginView.NAME, LoginView.class);
        getNavigator().addView(CampusView.NAME, CampusView.class);
        getNavigator().addView(DepartamentoView.NAME, DepartamentoView.class);
        getNavigator().addView(OrgaoView.NAME, OrgaoView.class);
        getNavigator().addView(UsuarioView.NAME, UsuarioView.class);
        getNavigator().addView(AtaView.NAME, AtaView.class);
        getNavigator().addView(AtaPublicadaView.NAME, AtaPublicadaView.class);
        getNavigator().addView(PDFView.NAME, PDFView.class);
        
        //
        // We use a view change handler to ensure the user is always redirected
        // to the login view if the user is not logged in.
        //
        getNavigator().addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {

                // Check if a user has logged in
                boolean isLoggedIn = Session.isAutenticado();
                boolean isLoginView = event.getNewView() instanceof LoginView;
                boolean isMainView = event.getNewView() instanceof MainView;
                boolean isAtaPublicadaView = event.getNewView() instanceof AtaPublicadaView;
                
                if (!isLoggedIn && !isLoginView && !isAtaPublicadaView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    getNavigator().navigateTo(LoginView.NAME);
                    return false;
                } else if (isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                    return true;
                } else if(!isMainView && !isLoginView) {
                	return true;
                }

                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
            	Page.getCurrent().setTitle("SIREATA - Sistema de Registro de Atas");
            }
        });
	}

}