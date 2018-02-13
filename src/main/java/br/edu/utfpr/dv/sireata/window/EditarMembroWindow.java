package br.edu.utfpr.dv.sireata.window;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

import br.edu.utfpr.dv.sireata.component.ComboUsuario;
import br.edu.utfpr.dv.sireata.model.OrgaoMembro;

public class EditarMembroWindow extends EditarWindow {
	
	private final OrgaoMembro membro;
	private final EditarOrgaoWindow parentWindow;
	
	private final ComboUsuario cbUsuario;
	private final TextField tfDesignacao;

	public EditarMembroWindow(OrgaoMembro membro, EditarOrgaoWindow parentWindow){
		super("Adicionar Membro", null);
		
		this.parentWindow = parentWindow;
		
		if(membro == null){
			this.membro = new OrgaoMembro();
		}else{
			this.membro = membro;
		}
		
		this.cbUsuario = new ComboUsuario("Membro");
		this.cbUsuario.setWidth("400px");
		
		this.tfDesignacao = new TextField("Designação");
		this.tfDesignacao.setWidth("400px");
		
		this.adicionarCampo(this.cbUsuario);
		this.adicionarCampo(this.tfDesignacao);
		
		this.carregaMembro();
	}
	
	private void carregaMembro(){
		this.cbUsuario.setUsuario(this.membro.getUsuario());
		this.tfDesignacao.setValue(this.membro.getDesignacao());
	}
	
	@Override
	public void salvar() {
		try{
			if((this.cbUsuario.getUsuario() == null) || (this.cbUsuario.getUsuario().getIdUsuario() == 0)){
				throw new Exception("Selecione o membro.");
			}
			
			this.membro.setUsuario(this.cbUsuario.getUsuario());
			this.membro.setDesignacao(this.tfDesignacao.getValue());
			
			this.parentWindow.adicionarMembro(this.membro);
			
			this.close();
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Adicionar Membro", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

}
