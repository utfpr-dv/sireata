package br.edu.utfpr.dv.sireata.window;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import br.edu.utfpr.dv.sireata.bo.CampusBO;
import br.edu.utfpr.dv.sireata.bo.OrgaoBO;
import br.edu.utfpr.dv.sireata.component.ComboCampus;
import br.edu.utfpr.dv.sireata.component.ComboCampus.TipoFiltro;
import br.edu.utfpr.dv.sireata.component.ComboDepartamento;
import br.edu.utfpr.dv.sireata.component.ComboUsuario;
import br.edu.utfpr.dv.sireata.model.Campus;
import br.edu.utfpr.dv.sireata.model.Orgao;
import br.edu.utfpr.dv.sireata.model.OrgaoMembro;
import br.edu.utfpr.dv.sireata.view.ListView;

public class EditarOrgaoWindow extends EditarWindow {
	
	private final Orgao orgao;
	
	private final TabSheet tab;
	private final ComboCampus cbCampus;
	private final ComboDepartamento cbDepartamento;
	private final TextField tfNome;
	private final TextField tfNomeCompleto;
	private final TextField tfDesignacaoPresidente;
	private final ComboUsuario cbPresidente;
	private final ComboUsuario cbSecretario;
	private final CheckBox cbAtivo;
	private final TextField tfLinkAtas;
	private final Button btAdicionarMembro;
	private final Button btEditarMembro;
	private final Button btRemoverMembro;
	private final VerticalLayout vlGrid;
	private Grid gridMembros;
	
	public EditarOrgaoWindow(Orgao orgao, ListView parentView){
		super("Editar Órgão", parentView);
		
		if(orgao == null){
			this.orgao = new Orgao();
		}else{
			this.orgao = orgao;
		}
		
		this.cbCampus = new ComboCampus(TipoFiltro.NENHUM);
		this.cbCampus.setWidth("400px");
		
		this.cbDepartamento = new ComboDepartamento(this.cbCampus.getCampus().getIdCampus(), TipoFiltro.NENHUM);
		this.cbDepartamento.setWidth("400px");
		
		this.cbCampus.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				cbDepartamento.setIdCampus(cbCampus.getCampus().getIdCampus());
			}
		});
		
		this.tfNome = new TextField("Nome");
		this.tfNome.setWidth("810px");
		
		this.tfNomeCompleto = new TextField("Nome Completo");
		this.tfNomeCompleto.setWidth("810px");
		
		this.tfDesignacaoPresidente = new TextField("Designação do Presidente");
		this.tfDesignacaoPresidente.setWidth("810px");
		
		this.cbPresidente = new ComboUsuario("Presidente");
		this.cbPresidente.setWidth("400px");
		
		this.cbSecretario = new ComboUsuario("Secretário");
		this.cbSecretario.setWidth("400px");
		
		this.cbAtivo = new CheckBox("Ativo");
		
		this.tfLinkAtas = new TextField("Link para acesso às atas publicadas");
		this.tfLinkAtas.setValue("http://coensapp.dv.utfpr.edu.br/sireata/#!ataspublicadas/" + String.valueOf(orgao.getIdOrgao()));
		this.tfLinkAtas.setReadOnly(true);
		this.tfLinkAtas.setWidth("810px");
		
		HorizontalLayout h1 = new HorizontalLayout(this.cbCampus, this.cbDepartamento);
		h1.setSpacing(true);
		
		HorizontalLayout h2 = new HorizontalLayout(this.cbPresidente, this.cbSecretario);
		h2.setSpacing(true);
		
		VerticalLayout tab1 = new VerticalLayout(h1, this.tfNome, this.tfNomeCompleto, this.tfDesignacaoPresidente, h2, this.cbAtivo);
		tab1.setSpacing(true);
		
		if(orgao.getIdOrgao() > 0){
			tab1.addComponent(this.tfLinkAtas);
		}
		
		this.tab = new TabSheet();
		this.tab.setWidth("820px");
		this.tab.addStyleName(ValoTheme.TABSHEET_FRAMED);
		this.tab.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
		this.tab.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		
		this.tab.addTab(tab1, "Órgão");
		
		this.vlGrid = new VerticalLayout();
		this.vlGrid.setSpacing(true);
		
		this.btAdicionarMembro = new Button("Adicionar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	adicionarMembro();
            }
        });
		this.btAdicionarMembro.setIcon(FontAwesome.PLUS);
		this.btAdicionarMembro.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		this.btAdicionarMembro.setWidth("150px");
		
		this.btEditarMembro = new Button("Editar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	editarMembro();
            }
        });
		this.btEditarMembro.setIcon(FontAwesome.EDIT);
		this.btEditarMembro.addStyleName(ValoTheme.BUTTON_PRIMARY);
		this.btEditarMembro.setWidth("150px");
		
		this.btRemoverMembro = new Button("Remover", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	removerMembro();
            }
        });
		this.btRemoverMembro.setIcon(FontAwesome.TRASH);
		this.btRemoverMembro.addStyleName(ValoTheme.BUTTON_DANGER);
		this.btRemoverMembro.setWidth("150px");
		
		HorizontalLayout h3 = new HorizontalLayout(this.btAdicionarMembro, this.btEditarMembro, this.btRemoverMembro);
		h3.setSpacing(true);
		
		VerticalLayout tab2 = new VerticalLayout(this.vlGrid, h3);
		tab2.setSpacing(true);
		
		this.tab.addTab(tab2, "Membros");
		
		this.adicionarCampo(this.tab);
		
		this.carregarOrgao();
	}
	
	private void carregarOrgao(){
		try{
			if((this.orgao.getDepartamento() != null) && (this.orgao.getDepartamento().getIdDepartamento() != 0)){
				CampusBO bo = new CampusBO();
				Campus campus = bo.buscarPorDepartamento(this.orgao.getDepartamento().getIdDepartamento());
				
				this.cbCampus.setCampus(campus);
				
				this.cbDepartamento.setIdCampus(campus.getIdCampus());
				
				this.cbDepartamento.setDepartamento(this.orgao.getDepartamento());
			}
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
		
		this.tfNome.setValue(this.orgao.getNome());
		this.tfNomeCompleto.setValue(this.orgao.getNomeCompleto());
		this.tfDesignacaoPresidente.setValue(this.orgao.getDesignacaoPresidente());
		this.cbPresidente.setUsuario(this.orgao.getPresidente());
		this.cbSecretario.setUsuario(this.orgao.getSecretario());
		this.cbAtivo.setValue(this.orgao.isAtivo());
		
		this.carregarMembros();
		
		if(this.orgao.getIdOrgao() > 0){
			this.cbCampus.setEnabled(false);
			this.cbDepartamento.setEnabled(false);
		}
	}
	
	private void carregarMembros(){
		this.gridMembros = new Grid();
		this.gridMembros.addColumn("Nome", String.class);
		this.gridMembros.addColumn("Designação", String.class);
		this.gridMembros.setWidth("810px");
		this.gridMembros.setHeight("350px");
		
		this.vlGrid.removeAllComponents();
		this.vlGrid.addComponent(this.gridMembros);
		
		for(OrgaoMembro u : this.orgao.getMembros()){
			this.gridMembros.addRow(u.getUsuario().getNome(), u.getDesignacao());
		}
	}

	@Override
	public void salvar() {
		try{
			OrgaoBO bo = new OrgaoBO();
			
			this.orgao.setDepartamento(this.cbDepartamento.getDepartamento());
			this.orgao.setNome(this.tfNome.getValue());
			this.orgao.setNomeCompleto(this.tfNomeCompleto.getValue());
			this.orgao.setDesignacaoPresidente(this.tfDesignacaoPresidente.getValue());
			this.orgao.setPresidente(this.cbPresidente.getUsuario());
			this.orgao.setSecretario(this.cbSecretario.getUsuario());
			this.orgao.setAtivo(this.cbAtivo.getValue());
			
			bo.salvar(this.orgao);
			
			Notification.show("Salvar Órgão", "Órgão salvo com sucesso.", Notification.Type.HUMANIZED_MESSAGE);
			
			this.atualizarGridPai();
			this.close();
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Salvar Órgão", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
	private void adicionarMembro(){
		UI.getCurrent().addWindow(new EditarMembroWindow(null, this));
	}
	
	public void adicionarMembro(OrgaoMembro membro){
		boolean encontrou = false;
		
		for(OrgaoMembro u : this.orgao.getMembros()){
			if(u.getUsuario().getIdUsuario() == membro.getUsuario().getIdUsuario()){
				encontrou = true;
				u.setDesignacao(membro.getDesignacao());
				break;
			}
		}
		
		if(!encontrou){
			this.orgao.getMembros().add(membro);
		}
		
		this.carregarMembros();
	}
	
	private void editarMembro(){
		int index = this.getIndexMembroSelecionado();
		
		if(index == -1){
			Notification.show("Editar Membro", "Selecione o membro para editar.", Notification.Type.WARNING_MESSAGE);
		}else{
			UI.getCurrent().addWindow(new EditarMembroWindow(this.orgao.getMembros().get(index), this));
		}
	}
	
	private void removerMembro(){
		int index = this.getIndexMembroSelecionado();
		
		if(index == -1){
			Notification.show("Selecionar Membro", "Selecione o membro para remover.", Notification.Type.WARNING_MESSAGE);
		}else{
			this.orgao.getMembros().remove(index);
			this.carregarMembros();
		}
	}
	
	private int getIndexMembroSelecionado(){
    	Object itemId = this.gridMembros.getSelectedRow();

    	if(itemId == null){
    		return -1;
    	}else{
    		return ((int)itemId) - 1;	
    	}
    }

}
