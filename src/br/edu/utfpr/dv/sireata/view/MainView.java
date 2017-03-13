package br.edu.utfpr.dv.sireata.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import br.edu.utfpr.dv.sireata.component.MenuBar;

public class MainView extends CustomComponent implements View {
	
	public static final String NAME = "home";
	
	private final MenuBar menu;
    private final Label label;
    
    public MainView(){
    	this.setCaption("SIREATA - Sistema de Registro de Atas");
    	
    	this.menu = new MenuBar();
    	
    	this.label = new Label("SIREATA - Sistema de Registro de Atas");
    	this.label.setStyleName("Title");
    	
    	VerticalLayout layoutMain = new VerticalLayout(this.menu, this.label);
    	layoutMain.setSpacing(true);
    	layoutMain.setSizeFull();
    	
    	this.setSizeFull();
    	this.setCompositionRoot(layoutMain);
    }

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
