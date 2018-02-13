package br.edu.utfpr.dv.sireata.view;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import br.edu.utfpr.dv.sireata.bo.DepartamentoBO;
import br.edu.utfpr.dv.sireata.component.ComboCampus;
import br.edu.utfpr.dv.sireata.component.ComboCampus.TipoFiltro;
import br.edu.utfpr.dv.sireata.model.Departamento;
import br.edu.utfpr.dv.sireata.window.EditarDepartamentoWindow;

public class DepartamentoView extends ListView {

	public static final String NAME = "departamentos";
	
	private final ComboCampus cbCampus;
	
	public DepartamentoView(){
		this.cbCampus = new ComboCampus(TipoFiltro.NENHUM);
		
		this.adicionarCampoFiltro(this.cbCampus);
		
		this.setBotaoExcluirVisivel(false);
	}

	@Override
	protected void carregarGrid() {
		this.getGrid().addColumn("Nome", String.class);
		this.getGrid().addColumn("Ativo", String.class);
		
		this.getGrid().getColumns().get(1).setWidth(100);
		
		try{
			DepartamentoBO bo = new DepartamentoBO();
			List<Departamento> list = bo.listarPorCampus((this.cbCampus.getCampus() == null ? 0 : this.cbCampus.getCampus().getIdCampus()), false);
			
			for(Departamento d : list){
				Object itemId = this.getGrid().addRow(d.getNome(), (d.isAtivo() ? "Sim" : "Não"));
				this.adicionarGridId(itemId, d.getIdDepartamento());
			}
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Listar Departamentos", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void adicionar() {
		Departamento departamento = new Departamento();
		
		departamento.setCampus(this.cbCampus.getCampus());
		
		UI.getCurrent().addWindow(new EditarDepartamentoWindow(departamento, this));
	}

	@Override
	public void editar(Object id) {
		try{
			DepartamentoBO bo = new DepartamentoBO();
			Departamento departamento = bo.buscarPorId((int)id);
			
			UI.getCurrent().addWindow(new EditarDepartamentoWindow(departamento, this));
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Editar Departamento", e.getMessage(), Notification.Type.ERROR_MESSAGE);
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
