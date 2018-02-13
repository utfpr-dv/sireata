package br.edu.utfpr.dv.sireata.window;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class SobreWindow extends Window {
	
	private final Button botaoFechar;
	private final VerticalLayout layoutCampos;
	private final HorizontalLayout layoutBotoes;
	
	public SobreWindow(){
		this.setCaption("Sobre ...");
		
		this.botaoFechar = new Button("Fechar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	close();
            }
        });
		this.botaoFechar.setWidth("150px");
		
		this.layoutCampos = new VerticalLayout();
		this.layoutCampos.setSpacing(true);
		
		Label label1 = new Label("SIREATA");
		label1.setStyleName("Title");
		this.layoutCampos.addComponent(label1);
		
		Label label2 = new Label("Sistema de Registro de Atas");
		label2.setStyleName("SubTitle");
		this.layoutCampos.addComponent(label2);
		
		this.layoutCampos.addComponent(new Label("Sistema desenvolvido pelo Curso de Engenharia de Software da UTFPR Câmpus Dois Vizinhos"));
		
		VerticalLayout layoutProfessors = new VerticalLayout(new Label("Professores responsáveis:"), new Label("Franciele Beal"), new Label("Newton Carlos Will"));
		
		this.layoutCampos.addComponent(new HorizontalLayout(layoutProfessors, new Image("", new ThemeResource("images/logo_ES.png"))));
		
		this.layoutBotoes = new HorizontalLayout(this.botaoFechar);
		this.layoutBotoes.setSpacing(true);
		this.layoutBotoes.setComponentAlignment(this.botaoFechar, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout vl = new VerticalLayout(this.layoutCampos, this.layoutBotoes);
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.setComponentAlignment(this.layoutBotoes, Alignment.BOTTOM_RIGHT);
		
		this.setContent(vl);
		this.setWidth("750px");
		
		this.setModal(true);
        this.center();
        this.setResizable(false);
	}
	
}
