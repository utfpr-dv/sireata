package br.edu.utfpr.dv.sireata.window;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import br.edu.utfpr.dv.sireata.bo.ComentarioBO;
import br.edu.utfpr.dv.sireata.model.Comentario;
import br.edu.utfpr.dv.sireata.model.Comentario.SituacaoComentario;
import br.edu.utfpr.dv.sireata.model.Pauta;

public class ListarComentariosWindow extends Window {
	
	private final Pauta pauta;
	private List<Comentario> comentarios;
	
	private final Grid gridComentarios;
	private final Button btVisualizar;

	public ListarComentariosWindow(Pauta pauta){
		this.setCaption("Comentários");
		
		this.pauta = pauta;
		
		this.gridComentarios = new Grid();
		this.gridComentarios.addColumn("Participante", String.class);
		this.gridComentarios.addColumn("Aceito", String.class);
		this.gridComentarios.getColumns().get(1).setWidth(100);
		this.gridComentarios.setHeight("300px");
		
		this.btVisualizar = new Button("Visualizar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	visualizarComentario();
            }
        });
		this.btVisualizar.setIcon(FontAwesome.SEARCH);
		this.btVisualizar.addStyleName(ValoTheme.BUTTON_PRIMARY);
		this.btVisualizar.setWidth("150px");
		
		HorizontalLayout hl = new HorizontalLayout(this.gridComentarios, this.btVisualizar);
		hl.setSpacing(true);
		hl.setMargin(true);
		
		this.setContent(hl);
		
		this.carregarComentarios();
		
		this.setModal(true);
        this.center();
        this.setResizable(false);
	}
	
	private void carregarComentarios(){
		try {
			ComentarioBO bo = new ComentarioBO();
			
			this.comentarios = bo.listarPorPauta(this.pauta.getIdPauta());
			
			for(Comentario c : this.comentarios){
				this.gridComentarios.addRow(c.getUsuario().getNome(), (c.getSituacao() == SituacaoComentario.ACEITO ? "Sim" : (c.getSituacao() == SituacaoComentario.RECUSADO ? "Não" : "-")));
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Comentários", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
	private void visualizarComentario(){
		int index = this.getIndexComentarioSelecionado();
		
		if(index == -1){
			Notification.show("Visualizar Comentário", "Selecione o comentário para visualizar.", Notification.Type.WARNING_MESSAGE);
		}else{
			UI.getCurrent().addWindow(new EditarComentarioWindow(this.comentarios.get(index)));
		}
	}
	
	private int getIndexComentarioSelecionado(){
    	Object itemId = this.gridComentarios.getSelectedRow();

    	if(itemId == null){
    		return -1;
    	}else{
    		return ((int)itemId) - 1;	
    	}
    }
	
}
