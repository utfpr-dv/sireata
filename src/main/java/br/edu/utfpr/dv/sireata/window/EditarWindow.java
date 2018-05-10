package br.edu.utfpr.dv.sireata.window;

import java.util.UUID;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.view.ListView;
import br.edu.utfpr.dv.sireata.view.PDFView;

import com.vaadin.ui.Button.ClickEvent;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public abstract class EditarWindow extends Window {

	private final ListView parentView;
	private final Button btSalvar;
	private final VerticalLayout vlCampos;
	private final HorizontalLayout hlBotoes;
	
	public EditarWindow(String titulo, ListView parentView){
		super(titulo);
		
		this.parentView = parentView;
		
		this.btSalvar = new Button("Salvar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	salvar();
            }
        });
		this.btSalvar.setIcon(FontAwesome.SAVE);
		this.btSalvar.addStyleName(ValoTheme.BUTTON_PRIMARY);
		this.btSalvar.setWidth("150px");
		
		this.vlCampos = new VerticalLayout();
		this.vlCampos.setSpacing(true);
		
		this.hlBotoes = new HorizontalLayout(btSalvar);
		this.hlBotoes.setSpacing(true);
		
		VerticalLayout vl = new VerticalLayout(this.vlCampos, this.hlBotoes);
		vl.setSpacing(true);
		vl.setMargin(true);
		
		this.setContent(vl);
		
		this.setModal(true);
        this.center();
        this.setResizable(false);
	}
	
	public void setBotaoSalvarHabilitado(boolean enabled){
		this.btSalvar.setEnabled(enabled);
	}
	
	public boolean isBotaoSalvarHabilitado(){
		return this.btSalvar.isEnabled();
	}
	
	public void setBotaoSalvarVisivel(boolean visivel){
		this.btSalvar.setVisible(visivel);
	}
	
	public boolean isBotaoSalvarVisivel(){
		return this.btSalvar.isVisible();
	}
	
	public void atualizarGridPai(){
		if(this.parentView != null){
			this.parentView.atualizarGrid();
		}
	}
	
	public void adicionarCampo(Component c){
		if(c instanceof HorizontalLayout){
			((HorizontalLayout)c).setSpacing(true);
		}else if(c instanceof VerticalLayout){
			((VerticalLayout)c).setSpacing(true);
		}
		
		this.vlCampos.addComponent(c);
	}
	
	public void adicionarBotao(Component c){
		c.setWidth("150px");
		this.hlBotoes.addComponent(c);
	}
	
	protected void showReport(byte[] pdfReport){
    	String id = UUID.randomUUID().toString();
    	
    	Session.putReport(pdfReport, id);
		
		getUI().getPage().open("#!" + PDFView.NAME + "/session/" + id, "_blank");
    }
	
	public abstract void salvar();
	
}
