package br.edu.utfpr.dv.sireata.view;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import br.edu.utfpr.dv.sireata.bo.OrgaoBO;
import br.edu.utfpr.dv.sireata.component.ComboCampus;
import br.edu.utfpr.dv.sireata.component.ComboCampus.TipoFiltro;
import br.edu.utfpr.dv.sireata.component.ComboDepartamento;
import br.edu.utfpr.dv.sireata.model.Orgao;
import br.edu.utfpr.dv.sireata.window.EditarOrgaoWindow;

public class OrgaoView extends ListView {
	
	public static final String NAME = "orgaos";
	
	private final ComboCampus cbCampus;
	private final ComboDepartamento cbDepartamento;

	public OrgaoView(){
		this.cbCampus = new ComboCampus(TipoFiltro.NENHUM);
		
		this.cbDepartamento = new ComboDepartamento(this.cbCampus.getCampus().getIdCampus(), TipoFiltro.NENHUM);
		
		this.cbCampus.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				cbDepartamento.setIdCampus(cbCampus.getCampus().getIdCampus());
			}
		});
		
		this.adicionarCampoFiltro(this.cbCampus);
		this.adicionarCampoFiltro(this.cbDepartamento);
		
		this.setBotaoExcluirVisivel(false);
	}
	
	@Override
	protected void carregarGrid() {
		this.getGrid().addColumn("Departamento", String.class);
		this.getGrid().addColumn("Nome", String.class);
		this.getGrid().addColumn("Presidente", String.class);
		this.getGrid().addColumn("Secretário", String.class);
		
		this.getGrid().getColumns().get(0).setWidth(300);
		
		try {
			OrgaoBO bo = new OrgaoBO();
	    	List<Orgao> list = bo.listar(this.cbCampus.getCampus().getIdCampus(), this.cbDepartamento.getDepartamento().getIdDepartamento());
	    	
	    	for(Orgao u : list){
				Object itemId = this.getGrid().addRow(u.getDepartamento().getNome(), u.getNome(), u.getPresidente().getNome(), u.getSecretario().getNome());
				this.adicionarGridId(itemId, u.getIdOrgao());
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Listar Usuários", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void adicionar() {
		Orgao orgao = new Orgao();
		
		orgao.setDepartamento(this.cbDepartamento.getDepartamento());
		
		UI.getCurrent().addWindow(new EditarOrgaoWindow(orgao, this));
	}

	@Override
	public void editar(Object id) {
		try{
			OrgaoBO bo = new OrgaoBO();
			Orgao orgao = bo.buscarPorId((int)id);
			
			UI.getCurrent().addWindow(new EditarOrgaoWindow(orgao, this));
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Editar Órgão", e.getMessage(), Notification.Type.ERROR_MESSAGE);
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
