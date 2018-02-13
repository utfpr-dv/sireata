package br.edu.utfpr.dv.sireata.component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.ComboBox;

import br.edu.utfpr.dv.sireata.bo.UsuarioBO;
import br.edu.utfpr.dv.sireata.model.Usuario;

public class ComboUsuario extends ComboBox {
	
private List<Usuario> list;
	
	public ComboUsuario(String caption){
		super(caption);
		this.setInvalidAllowed(false);
		this.setNullSelectionAllowed(false);
		this.setWidth("400px");
		this.carregaCombo();
	}
	
	public Usuario getUsuario(){
		if(this.getValue() == null){
			return new Usuario();
		}else{
			return (Usuario)this.getValue();	
		}
	}
	
	public void setUsuario(Usuario c){
		if(c == null){
			this.setValue(null);
			return;
		}
		
		boolean find = false;
		
		for(Usuario user : this.list){
			if(c.getIdUsuario() == user.getIdUsuario()){
				this.setValue(user);
				find = true;
				break;
			}
		}
		
		if(!find){
			try {
				UsuarioBO bo = new UsuarioBO();
				Usuario user = bo.buscarPorId(c.getIdUsuario());
				
				this.addItem(user);
				this.setValue(user);
			} catch (Exception e) {
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	private void carregaCombo(){
		try {
			UsuarioBO bo = new UsuarioBO();
			this.list = bo.listarTodos(true);
			
			this.removeAllItems();
			this.addItems(this.list);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
}
