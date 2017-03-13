package br.edu.utfpr.dv.sireata.component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.NativeSelect;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.DepartamentoBO;
import br.edu.utfpr.dv.sireata.component.ComboCampus.TipoFiltro;
import br.edu.utfpr.dv.sireata.model.Departamento;

public class ComboDepartamento extends NativeSelect {

	private List<Departamento> list;
	private int idCampus;
	private TipoFiltro tipoFiltro;
	
	public ComboDepartamento(int idCampus, TipoFiltro tipoFiltro){
		super("Departamento");
		this.setNullSelectionAllowed(false);
		this.setWidth("400px");
		this.idCampus = idCampus;
		this.tipoFiltro = tipoFiltro;
		this.carregaCombo();
	}
	
	public int getIdCampus(){
		return this.idCampus;
	}
	
	public void setIdCampus(int idCampus){
		this.idCampus = idCampus;
		this.carregaCombo();
	}
	
	public TipoFiltro getTipoFiltro(){
		return this.tipoFiltro;
	}
	
	public void setTipoFiltro(TipoFiltro tipoFiltro){
		this.tipoFiltro = tipoFiltro;
		this.carregaCombo();
	}
	
	public Departamento getDepartamento(){
		if(this.getValue() == null){
			return new Departamento();
		}else{
			return (Departamento)this.getValue();	
		}
	}
	
	public void setDepartamento(Departamento c){
		if(c == null){
			this.setValue(null);
			return;
		}
		
		boolean find = false;
		
		for(Departamento department : this.list){
			if(c.getIdDepartamento() == department.getIdDepartamento()){
				this.setValue(department);
				find = true;
				break;
			}
		}
		
		if(!find){
			try {
				DepartamentoBO bo = new DepartamentoBO();
				Departamento department = bo.buscarPorId(c.getIdDepartamento());
				
				if(department.getCampus().getIdCampus() == this.getIdCampus()){
					this.addItem(department);
					this.setValue(department);
				}
			} catch (Exception e) {
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	private void carregaCombo(){
		try {
			this.removeAllItems();
			
			if(this.getIdCampus() != 0){
				DepartamentoBO bo = new DepartamentoBO();
				
				if(this.getTipoFiltro() == TipoFiltro.CRIARATA){
					this.list = bo.listarParaCriacaoAta(this.getIdCampus(), Session.getUsuario().getIdUsuario());
				}else if(this.getTipoFiltro() == TipoFiltro.CONSULTARATA){
					this.list = bo.listarParaConsultaAtas(this.getIdCampus(), Session.getUsuario().getIdUsuario());
				}else{
					this.list = bo.listarPorCampus(this.getIdCampus(), true);
				}
				
				this.addItems(this.list);
				
				if(this.list.size() > 0){
					this.setDepartamento(this.list.get(0));
				}
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
}
