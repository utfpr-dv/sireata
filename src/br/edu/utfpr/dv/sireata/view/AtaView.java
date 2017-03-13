package br.edu.utfpr.dv.sireata.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.renderers.DateRenderer;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.AtaBO;
import br.edu.utfpr.dv.sireata.component.ComboCampus;
import br.edu.utfpr.dv.sireata.component.ComboCampus.TipoFiltro;
import br.edu.utfpr.dv.sireata.component.ComboDepartamento;
import br.edu.utfpr.dv.sireata.component.ComboOrgao;
import br.edu.utfpr.dv.sireata.model.Ata;
import br.edu.utfpr.dv.sireata.model.AtaReport;
import br.edu.utfpr.dv.sireata.util.DateUtils;
import br.edu.utfpr.dv.sireata.util.ExtensionUtils;
import br.edu.utfpr.dv.sireata.util.ReportUtils;
import br.edu.utfpr.dv.sireata.window.EditarAtaWindow;

public class AtaView extends ListView {
	
	public static final String NAME = "atas";
	
	private final ComboCampus cbCampus;
	private final ComboDepartamento cbDepartamento;
	private final ComboOrgao cbOrgao;
	private final Button btVisualizar;
	private final Button btPrevia;
	private final Button btPublicar;
	
	private Button.ClickListener listenerClickVisualizar;
	private Button.ClickListener listenerClickPrevia;
	
	private int tipo;
	
	public AtaView(){
		this.cbCampus = new ComboCampus(TipoFiltro.NENHUM);
		
		this.cbDepartamento = new ComboDepartamento(this.cbCampus.getCampus().getIdCampus(), TipoFiltro.NENHUM);
		
		this.cbCampus.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				cbDepartamento.setIdCampus(cbCampus.getCampus().getIdCampus());
			}
		});
		
		this.cbOrgao = new ComboOrgao(this.cbDepartamento.getDepartamento().getIdDepartamento(), TipoFiltro.NENHUM);
		
		this.cbDepartamento.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				cbOrgao.setIdDepartamento(cbDepartamento.getDepartamento().getIdDepartamento());
			}
		});
		
		this.btVisualizar = new Button("Visualizar");
		
		this.btPrevia = new Button("Prévia");
		
		this.btPublicar = new Button("Publicar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	publicarAta();
            }
        });
		
		this.adicionarCampoFiltro(this.cbCampus);
		this.adicionarCampoFiltro(this.cbDepartamento);
		this.adicionarCampoFiltro(this.cbOrgao);
		
		this.adicionarBotao(this.btVisualizar);
		this.adicionarBotao(this.btPrevia);
		this.adicionarBotao(this.btPublicar);
		
		this.setBotaoExcluirVisivel(false);
		this.setBotaoAdicionarVisivel(false);
	}

	@Override
	protected void carregarGrid() {
		this.getGrid().addColumn("Órgão", String.class);
		this.getGrid().addColumn("Tipo", String.class);
		this.getGrid().addColumn("Número", String.class);
		this.getGrid().addColumn("Data", Date.class).setRenderer(new DateRenderer(new SimpleDateFormat("dd/MM/yyyy")));
		this.getGrid().addColumn("Presidente", String.class);
		this.getGrid().addColumn("Secretário", String.class);
		this.getGrid().getColumns().get(1).setWidth(150);
		this.getGrid().getColumns().get(2).setWidth(100);
		this.getGrid().getColumns().get(3).setWidth(130);
		this.getGrid().addSelectionListener(new SelectionListener() {
			@Override
			public void select(SelectionEvent event) {
				prepareDownload();
			}
		});
		
		try {
			AtaBO bo = new AtaBO();
	    	List<Ata> list = bo.listar(this.cbCampus.getCampus().getIdCampus(), this.cbDepartamento.getDepartamento().getIdDepartamento(), this.cbOrgao.getOrgao().getIdOrgao(), (this.tipo == 1), Session.getUsuario().getIdUsuario());
	    	
	    	for(Ata u : list){
				Object itemId = this.getGrid().addRow(u.getOrgao().getNome(), u.getTipo().toString(), String.valueOf(u.getNumero()) + "/" + String.valueOf(DateUtils.getYear(u.getData())), u.getData(), u.getPresidente().getNome(), u.getSecretario().getNome());
				this.adicionarGridId(itemId, u.getIdAta());
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Listar Usuários", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void adicionar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editar(Object id) {
		try{
			AtaBO bo = new AtaBO();
			Ata ata = bo.buscarPorId((int)id);
			
			UI.getCurrent().addWindow(new EditarAtaWindow(ata, this));
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Editar Ata", e.getMessage(), Notification.Type.ERROR_MESSAGE);
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
	
	public int getTipo(){
		return this.tipo;
	}
	
	public void setTipo(int tipo){
		this.tipo = tipo;
	}
	
	private void publicarAta(){
		try {
			Object idAta = this.getIdSelecionado();
			
			if(idAta == null){
	    		Notification.show("Publicar Ata", "Selecione a ata para publicar.", Notification.Type.WARNING_MESSAGE);
	    	}else{
	    		AtaBO bo = new AtaBO();
	    		
	    		if(bo.isPresidenteOuSecretario(Session.getUsuario().getIdUsuario(), (int)idAta)){
	    			ConfirmDialog.show(UI.getCurrent(), "Confirma a publicação da ata? Esta ação não poderá ser revertida.", new ConfirmDialog.Listener() {
		                public void onClose(ConfirmDialog dialog) {
		                    if (dialog.isConfirmed()) {
		                    	try {
		                    		bo.publicar((int)idAta);
		                    		
		                    		Notification.show("Publicar Ata", "Ata publicada com sucesso.", Notification.Type.WARNING_MESSAGE);
		                    		
		                    		atualizarGrid();
								} catch (Exception e) {
									Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
									
									Notification.show("Publicar Ata", e.getMessage(), Notification.Type.ERROR_MESSAGE);
								}
		                    }
		                }
		            });
	    		}else{
	    			Notification.show("Publicar Ata", "Apenas o presidente ou secretário da reunião pode publicar a ata.", Notification.Type.WARNING_MESSAGE);
	    		}
	    	}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Publicar Ata", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void enter(ViewChangeEvent event){
		super.enter(event);
		
		if(event.getParameters() != null){
			this.setTipo(Integer.parseInt(event.getParameters()));
		}else{
			this.setTipo(1);
		}
		
		if(this.getTipo() == 0){
			this.cbCampus.setTipoFiltro(TipoFiltro.CONSULTARATA);
			this.cbDepartamento.setTipoFiltro(TipoFiltro.CONSULTARATA);
			this.cbOrgao.setTipoFiltro(TipoFiltro.CONSULTARATA);
			this.btVisualizar.setVisible(false);
		}else{
			this.setBotaoEditarVisivel(false);
			this.btPrevia.setVisible(false);
			this.btPublicar.setVisible(false);
		}
		
		this.atualizarGrid();
	}
	
	private void prepareDownload(){
    	Object value = this.getIdSelecionado();
    	
    	this.btVisualizar.removeClickListener(this.listenerClickVisualizar);
    	new ExtensionUtils().removeAllExtensions(this.btVisualizar);
    	
    	this.btPrevia.removeClickListener(this.listenerClickPrevia);
    	new ExtensionUtils().removeAllExtensions(this.btPrevia);
    	
    	if(value != null){
    		try {
    			AtaBO bo = new AtaBO();
            	Ata ata = bo.buscarPorId((int)value);
            	
            	new ExtensionUtils().extendToDownload(ata.getNome() + ".pdf", ata.getDocumento(), this.btVisualizar);
        	} catch (Exception e) {
        		this.listenerClickVisualizar = new Button.ClickListener() {
		            @Override
		            public void buttonClick(ClickEvent event) {
		            	Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		            	
		            	Notification.show("Visualizar Ata", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		            }
		        };
		        
        		this.btVisualizar.addClickListener(this.listenerClickVisualizar);
			}
    		
    		try {
    			List<AtaReport> list = new ArrayList<AtaReport>();
				AtaBO bo = new AtaBO();
				AtaReport report = bo.gerarAtaReport((int)value);
				
				list.add(report);
				
				new ReportUtils().prepareForPdfReport("Ata", "Ata", list, this.btPrevia);
        	} catch (Exception e) {
        		this.listenerClickPrevia = new Button.ClickListener() {
		            @Override
		            public void buttonClick(ClickEvent event) {
		            	Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		            	
		            	Notification.show("Prévia da Ata", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		            }
		        };
		        
        		this.btPrevia.addClickListener(this.listenerClickPrevia);
			}
    	}else{
    		this.listenerClickVisualizar = new Button.ClickListener() {
	            @Override
	            public void buttonClick(ClickEvent event) {
	            	Notification.show("Visualizar Ata", "Selecione a ata para visualizar.", Notification.Type.WARNING_MESSAGE);
	            }
	        };
	        
    		this.btVisualizar.addClickListener(this.listenerClickVisualizar);
    		
    		this.listenerClickPrevia = new Button.ClickListener() {
	            @Override
	            public void buttonClick(ClickEvent event) {
	            	Notification.show("Prévia da Ata", "Selecione a ata para visualizar a prévia do documento.", Notification.Type.WARNING_MESSAGE);
	            }
	        };
	        
	        this.btPrevia.addClickListener(this.listenerClickPrevia);
    	}
    }

}
