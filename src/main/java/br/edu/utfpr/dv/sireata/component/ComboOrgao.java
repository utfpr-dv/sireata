package br.edu.utfpr.dv.sireata.component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.NativeSelect;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.OrgaoBO;
import br.edu.utfpr.dv.sireata.component.ComboCampus.TipoFiltro;
import br.edu.utfpr.dv.sireata.model.Orgao;

public class ComboOrgao extends NativeSelect {

	private List<Orgao> list;
	private int idDepartamento;
	private TipoFiltro tipoFiltro;
	
	public ComboOrgao(int idDepartamento, TipoFiltro tipoFiltro){
		super("Órgão");
		this.setNullSelectionAllowed(false);
		this.setWidth("400px");
		this.idDepartamento = idDepartamento;
		this.tipoFiltro = tipoFiltro;
		this.carregaCombo();
	}
	
	public int getIdDepartamento(){
		return this.idDepartamento;
	}
	
	public void setIdDepartamento(int idDepartamento){
		this.idDepartamento = idDepartamento;
		this.carregaCombo();
	}
	
	public TipoFiltro getTipoFiltro(){
		return this.tipoFiltro;
	}
	
	public void setTipoFiltro(TipoFiltro tipoFiltro){
		this.tipoFiltro = tipoFiltro;
		this.carregaCombo();
	}
	
	public Orgao getOrgao(){
		if(this.getValue() == null){
			return new Orgao();
		}else{
			return (Orgao)this.getValue();	
		}
	}
	
	public void setOrgao(Orgao c){
		if(c == null){
			this.setValue(null);
			return;
		}
		
		boolean find = false;
		
		for(Orgao orgao : this.list){
			if(c.getIdOrgao() == orgao.getIdOrgao()){
				this.setValue(orgao);
				find = true;
				break;
			}
		}
		
		if(!find){
			try {
				OrgaoBO bo = new OrgaoBO();
				Orgao orgao = bo.buscarPorId(c.getIdOrgao());
				
				if(orgao.getDepartamento().getIdDepartamento() == this.getIdDepartamento()){
					this.addItem(orgao);
					this.setValue(orgao);
				}
			} catch (Exception e) {
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	private void carregaCombo(){
		try {
			this.removeAllItems();
			
			if(this.getIdDepartamento() != 0){
				OrgaoBO bo = new OrgaoBO();
				
				if(this.getTipoFiltro() == TipoFiltro.CRIARATA){
					this.list = bo.listarParaCriacaoAta(this.getIdDepartamento(), Session.getUsuario().getIdUsuario());
				}else if(this.getTipoFiltro() == TipoFiltro.CONSULTARATA){
					this.list = bo.listarParaConsultaAtas(this.getIdDepartamento(), Session.getUsuario().getIdUsuario());
				}else{
					this.list = bo.listarPorDepartamento(this.getIdDepartamento());
				}
				
				this.addItems(this.list);
				
				if(this.list.size() > 0){
					this.setOrgao(this.list.get(0));
				}
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
}
