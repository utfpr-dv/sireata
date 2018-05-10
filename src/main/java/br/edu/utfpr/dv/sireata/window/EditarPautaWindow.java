package br.edu.utfpr.dv.sireata.window;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Button.ClickEvent;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.ComentarioBO;
import br.edu.utfpr.dv.sireata.bo.PautaBO;
import br.edu.utfpr.dv.sireata.model.Comentario;
import br.edu.utfpr.dv.sireata.model.Pauta;

public class EditarPautaWindow extends EditarWindow {
	
	private final Pauta pauta;
	private final EditarAtaWindow parentWindow;
	
	private final TextField tfTitulo;
	private final TextArea taDescricao;
	private final Button btComentarios;
	private final Button btComentar;
	
	public EditarPautaWindow(Pauta pauta, boolean aceitarComentarios, boolean permiteSalvar, EditarAtaWindow parentWindow){
		super("Editar Pauta", null);
		
		if(pauta == null){
			this.pauta = new Pauta();
		}else{
			this.pauta = pauta;
		}
		
		this.parentWindow = parentWindow;
		
		this.tfTitulo = new TextField("Ponto de Pauta");
		this.tfTitulo.setWidth("800px");
		
		this.taDescricao = new TextArea("Discussão sobre o Ponto");
		this.taDescricao.setWidth("800px");
		this.taDescricao.setHeight("300px");
		
		this.btComentarios = new Button("Comentários", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	visualizarComentarios();
            }
        });
		this.btComentarios.setWidth("150px");
		
		this.btComentar = new Button("Incluir Comentário", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	incluirComentario();
            }
        });
		this.btComentar.setIcon(FontAwesome.PLUS);
		this.btComentar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		this.adicionarCampo(this.tfTitulo);
		this.adicionarCampo(this.taDescricao);
		
		if(this.pauta.getIdPauta() > 0){
			this.adicionarBotao(this.btComentarios);
			
			if(aceitarComentarios){
				this.adicionarBotao(this.btComentar);	
			}
		}
		
		this.btComentar.setWidth("200px");
		
		this.setBotaoSalvarVisivel(permiteSalvar);
		
		this.carregarPauta();
	}
	
	private void carregarPauta(){
		this.tfTitulo.setValue(this.pauta.getTitulo());
		this.taDescricao.setValue(this.pauta.getDescricao());
	}

	@Override
	public void salvar() {
		try{
			this.pauta.setTitulo(this.tfTitulo.getValue());
			this.pauta.setDescricao(this.taDescricao.getValue());
			
			PautaBO bo = new PautaBO();
			
			bo.validarDados(this.pauta);
			
			if(this.pauta.getAta().getIdAta() > 0){
				bo.salvar(this.pauta);
			}
			
			Notification.show("Salvar Pauta", "Pauta salva com sucesso.", Notification.Type.HUMANIZED_MESSAGE);
			
			this.parentWindow.atualizarPauta(this.pauta);
			
			this.close();
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Salvar Pauta", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
	private void visualizarComentarios(){
		UI.getCurrent().addWindow(new ListarComentariosWindow(this.pauta));
	}
	
	private void incluirComentario(){
		try {
			ComentarioBO bo = new ComentarioBO();
			Comentario comentario = bo.buscarPorUsuario(Session.getUsuario().getIdUsuario(), this.pauta.getIdPauta());
			
			if(comentario == null){
				comentario = new Comentario();
				comentario.setUsuario(Session.getUsuario());
				comentario.setPauta(this.pauta);
			}
			
			UI.getCurrent().addWindow(new EditarComentarioWindow(comentario));
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Incluir Comentário", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

}
