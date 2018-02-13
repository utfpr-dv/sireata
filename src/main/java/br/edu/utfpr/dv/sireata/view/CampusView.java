package br.edu.utfpr.dv.sireata.view;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import br.edu.utfpr.dv.sireata.bo.CampusBO;
import br.edu.utfpr.dv.sireata.model.Campus;
import br.edu.utfpr.dv.sireata.window.EditarCampusWindow;

public class CampusView extends ListView {
	
	public static final String NAME = "campus";
	
	public CampusView(){
		this.setFiltrosVisiveis(false);
		this.setBotaoExcluirVisivel(false);
	}

	@Override
	protected void carregarGrid() {
		this.getGrid().addColumn("Nome", String.class);
		this.getGrid().addColumn("Ativo", String.class);
		
		this.getGrid().getColumns().get(1).setWidth(100);
		
		try{
			CampusBO bo = new CampusBO();
			List<Campus> list = bo.listarTodos(false);
			
			for(Campus c : list){
				Object itemId = this.getGrid().addRow(c.getNome(), (c.isAtivo() ? "Sim" : "Não"));
				this.adicionarGridId(itemId, c.getIdCampus());
			}
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Listar Câmpus", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void adicionar() {
		UI.getCurrent().addWindow(new EditarCampusWindow(null, this));
	}

	@Override
	public void editar(Object id) {
		try{
			CampusBO bo = new CampusBO();
			Campus campus = bo.buscarPorId((int)id);
			
			UI.getCurrent().addWindow(new EditarCampusWindow(campus, this));
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Editar Câmpus", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void excluir(Object id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void filtrar() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
