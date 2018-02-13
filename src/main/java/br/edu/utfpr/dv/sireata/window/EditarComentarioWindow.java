package br.edu.utfpr.dv.sireata.window;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.AtaBO;
import br.edu.utfpr.dv.sireata.bo.ComentarioBO;
import br.edu.utfpr.dv.sireata.model.Ata;
import br.edu.utfpr.dv.sireata.model.Comentario;
import br.edu.utfpr.dv.sireata.model.Comentario.SituacaoComentario;

public class EditarComentarioWindow extends EditarWindow {
	
	private final Comentario comentario;
	
	private final NativeSelect cbSituacao;
	private final TextArea taComentarios;
	private final NativeSelect cbSituacaoComentarios;
	private final TextArea taMotivo;

	public EditarComentarioWindow(Comentario comentario){
		super("Editar Comentário", null);
		
		if(comentario == null){
			this.comentario = new Comentario();
		}else{
			this.comentario = comentario;
		}
		
		this.cbSituacao = new NativeSelect("Parecer");
		this.cbSituacao.setWidth("400px");
		this.cbSituacao.addItem(SituacaoComentario.NAOANALISADO);
		this.cbSituacao.addItem(SituacaoComentario.ACEITO);
		this.cbSituacao.addItem(SituacaoComentario.RECUSADO);
		this.cbSituacao.setValue(SituacaoComentario.NAOANALISADO);
		this.cbSituacao.setNullSelectionAllowed(false);
		
		this.taComentarios = new TextArea("Comentários sobre a Pauta");
		this.taComentarios.setWidth("800px");
		this.taComentarios.setHeight("150px");
		
		this.cbSituacaoComentarios = new NativeSelect("Parecer sobre os Comentários");
		this.cbSituacaoComentarios.setWidth("400px");
		this.cbSituacaoComentarios.addItem(SituacaoComentario.NAOANALISADO);
		this.cbSituacaoComentarios.addItem(SituacaoComentario.ACEITO);
		this.cbSituacaoComentarios.addItem(SituacaoComentario.RECUSADO);
		this.cbSituacaoComentarios.setValue(SituacaoComentario.NAOANALISADO);
		this.cbSituacaoComentarios.setNullSelectionAllowed(false);
		
		this.taMotivo = new TextArea("Motivo do Parecer");
		this.taMotivo.setWidth("800px");
		this.taMotivo.setHeight("150px");
		
		this.adicionarCampo(this.cbSituacao);
		this.adicionarCampo(this.taComentarios);
		this.adicionarCampo(this.cbSituacaoComentarios);
		this.adicionarCampo(this.taMotivo);
		
		this.carregarComentario();
	}
	
	private void carregarComentario(){
		this.cbSituacao.setValue(this.comentario.getSituacao());
		this.taComentarios.setValue(this.comentario.getComentarios());
		this.cbSituacaoComentarios.setValue(this.comentario.getSituacaoComentarios());
		this.taMotivo.setValue(this.comentario.getMotivo());
		
		if(Session.getUsuario().getIdUsuario() != this.comentario.getUsuario().getIdUsuario()){
			try{
				AtaBO abo = new AtaBO();
				Ata ata = abo.buscarPorPauta(this.comentario.getPauta().getIdPauta());
				
				if(!abo.isPresidenteOuSecretario(Session.getUsuario().getIdUsuario(), ata.getIdAta())){
					this.setBotaoSalvarVisivel(false);
				}
			}catch(Exception e){
				this.setBotaoSalvarVisivel(false);
				
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
				
				Notification.show("Carregar Comentários", e.getMessage(), Notification.Type.ERROR_MESSAGE);
			}
		}else{
			try{
				AtaBO bo = new AtaBO();
				Ata ata = bo.buscarPorPauta(this.comentario.getPauta().getIdPauta());
				
				if(!bo.isPresidenteOuSecretario(Session.getUsuario().getIdUsuario(), ata.getIdAta())){
					this.cbSituacaoComentarios.setEnabled(false);
					this.taMotivo.setReadOnly(true);
				}
			}catch(Exception e){
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
				
				Notification.show("Carregar Comentários", e.getMessage(), Notification.Type.ERROR_MESSAGE);
			}
		}
	}
	
	@Override
	public void salvar() {
		try{
			ComentarioBO bo = new ComentarioBO();
			AtaBO abo = new AtaBO();
			Ata ata = abo.buscarPorPauta(this.comentario.getPauta().getIdPauta());
			
			if(Session.getUsuario().getIdUsuario() == this.comentario.getUsuario().getIdUsuario()){
				this.comentario.setSituacao((SituacaoComentario) this.cbSituacao.getValue());
				this.comentario.setComentarios(this.taComentarios.getValue());
			}
			
			if(abo.isPresidenteOuSecretario(Session.getUsuario().getIdUsuario(), ata.getIdAta())){
				this.comentario.setSituacaoComentarios((SituacaoComentario) this.cbSituacaoComentarios.getValue());
				this.comentario.setMotivo(this.taMotivo.getValue());
			}
						
			bo.salvar(this.comentario);
			
			Notification.show("Salvar Comentário", "Comentárip salvo com sucesso.", Notification.Type.HUMANIZED_MESSAGE);
			
			this.close();
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Salvar Comentários", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

}
