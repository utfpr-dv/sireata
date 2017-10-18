package br.edu.utfpr.dv.sireata.view;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class MainView extends BasicView {
	
	public static final String NAME = "home";
	
    private final Label label;
    
    public MainView(){
    	this.setCaption("SIREATA - Sistema de Registro de Atas");
    	
    	this.label = new Label("SIREATA - Sistema de Registro de Atas");
    	this.label.setStyleName("Title");
    	
    	VerticalLayout layoutMain = new VerticalLayout(this.label);
    	layoutMain.setSpacing(true);
    	layoutMain.setSizeFull();
    	
    	this.setSizeFull();
    	this.setContent(layoutMain);
    }

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
