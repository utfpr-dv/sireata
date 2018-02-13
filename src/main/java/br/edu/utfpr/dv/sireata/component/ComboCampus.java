package br.edu.utfpr.dv.sireata.component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.NativeSelect;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.CampusBO;
import br.edu.utfpr.dv.sireata.model.Campus;

public class ComboCampus extends NativeSelect {
	
	public enum TipoFiltro {NENHUM, CRIARATA, CONSULTARATA}

	private List<Campus> list;
	private boolean filterOnlyActives;
	private TipoFiltro tipoFiltro;
	
	public ComboCampus(TipoFiltro tipoFiltro){
		super("Câmpus");
		this.setNullSelectionAllowed(false);
		this.setWidth("400px");
		this.filterOnlyActives = true;
		this.tipoFiltro = tipoFiltro;
		this.carregaCombo();
	}
	
	public boolean isFiltrarSomenteAtivos(){
		return filterOnlyActives;
	}
	
	public void setFiltrarSomenteAtivos(boolean filterOnlyActives){
		this.filterOnlyActives = filterOnlyActives;
		this.carregaCombo();
	}
	
	public TipoFiltro getTipoFiltro(){
		return this.tipoFiltro;
	}
	
	public void setTipoFiltro(TipoFiltro tipoFiltro){
		this.tipoFiltro = tipoFiltro;
		this.carregaCombo();
	}
	
	public Campus getCampus(){
		if(this.getValue() == null){
			return new Campus();
		}else{
			return (Campus)this.getValue();	
		}
	}
	
	public void setCampus(Campus c){
		if(c == null){
			this.setValue(null);
			return;
		}
		
		boolean find = false;
		
		for(Campus campus : this.list){
			if(c.getIdCampus() == campus.getIdCampus()){
				this.setValue(campus);
				find = true;
				break;
			}
		}
		
		if(!find){
			try {
				CampusBO bo = new CampusBO();
				Campus campus = bo.buscarPorId(c.getIdCampus());
				
				this.addItem(campus);
				this.setValue(campus);
			} catch (Exception e) {
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	private void carregaCombo(){
		try {
			CampusBO bo = new CampusBO();
			
			if(this.getTipoFiltro() == TipoFiltro.CRIARATA){
				this.list = bo.listarParaCriacaoAta(Session.getUsuario().getIdUsuario());
			}else if(this.getTipoFiltro() == TipoFiltro.CONSULTARATA){
				this.list = bo.listarParaConsultaAtas(Session.getUsuario().getIdUsuario());
			}else{
				this.list = bo.listarTodos(this.isFiltrarSomenteAtivos());
			}
			
			this.removeAllItems();
			this.addItems(this.list);
			
			if(this.list.size() > 0){
				this.setCampus(this.list.get(0));
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
}
