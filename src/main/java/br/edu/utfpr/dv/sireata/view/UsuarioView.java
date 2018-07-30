package br.edu.utfpr.dv.sireata.view;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import br.edu.utfpr.dv.sireata.bo.UsuarioBO;
import br.edu.utfpr.dv.sireata.model.Usuario;
import br.edu.utfpr.dv.sireata.window.EditarUsuarioWindow;

public class UsuarioView extends ListView {
	
	public static final String NAME = "usuarios";
	
	private final TextField tfNome;
	private final CheckBox cbAtivo;
	private final CheckBox cbExterno;
	
	public UsuarioView(){
		this.tfNome = new TextField("Nome:");
		this.tfNome.setWidth("400px");
		
		this.cbAtivo = new CheckBox("Somente usuários ativos");
		this.cbAtivo.setValue(true);
		
		this.cbExterno = new CheckBox("Somente usuários externos");
		
		VerticalLayout vl = new VerticalLayout(this.cbAtivo, this.cbExterno);
		vl.setSpacing(true);
		
		this.adicionarCampoFiltro(new HorizontalLayout(this.tfNome, vl));
		
		this.setBotaoExcluirVisivel(false);
	}
	
	protected void carregarGrid(){
		this.getGrid().addColumn("Login", String.class);
		this.getGrid().addColumn("Nome", String.class);
		this.getGrid().addColumn("Email", String.class);
		
		this.getGrid().getColumns().get(0).setWidth(300);
		
		try {
			UsuarioBO bo = new UsuarioBO();
	    	List<Usuario> list = bo.listar(this.tfNome.getValue(), this.cbAtivo.getValue(), this.cbExterno.getValue());
	    	
	    	for(Usuario u : list){
				Object itemId = this.getGrid().addRow(u.getLogin(), u.getNome(), u.getEmail());
				this.adicionarGridId(itemId, u.getIdUsuario());
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Listar Usuários", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
    }

	@Override
	public void adicionar() {
		UI.getCurrent().addWindow(new EditarUsuarioWindow(null, this));
	}

	@Override
	public void editar(Object id) {
		try {
			UsuarioBO bo = new UsuarioBO();
			Usuario usuario = bo.buscarPorId((int)id);
			
			UI.getCurrent().addWindow(new EditarUsuarioWindow(usuario, this));
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Editar Usuário", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void excluir(Object id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void filtrar() {
		// TODO Auto-generated method stub
		
	}

}
