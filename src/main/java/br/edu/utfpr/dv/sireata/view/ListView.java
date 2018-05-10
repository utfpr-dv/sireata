package br.edu.utfpr.dv.sireata.view;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SingleSelectionModel;

public abstract class ListView extends BasicView {

    private Grid grid;
    private final Button btAdicionar;
    private final Button btEditar;
    private final Button btExcluir;
    private final VerticalLayout vlBotoes;
    private final HorizontalLayout hlGrid;
    private final HorizontalLayout hlCampos;
    private final VerticalLayout vlFiltros;
    private final Button btFiltrar;
    private final List<GridItem> gridItens;
    private final Panel panelFiltros;
    
    public ListView(){
    	this.gridItens = new ArrayList<GridItem>();
    	
    	this.grid = new Grid();
    	this.grid.setSelectionMode(SelectionMode.SINGLE);
		((SingleSelectionModel)this.grid.getSelectionModel()).setDeselectAllowed(false);
		this.grid.setSizeFull();
		this.grid.addItemClickListener(new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				if(event.isDoubleClick() && isBotaoEditarVisivel()){
					editar();
				}
			}
		});
		
		this.btAdicionar = new Button("Adicionar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	adicionar();
            }
        });
		this.btAdicionar.setIcon(FontAwesome.PLUS);
		this.btAdicionar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		this.btAdicionar.setWidth("150px");
		
		this.btEditar = new Button("Editar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	editar();
            }
        });
		this.btEditar.setIcon(FontAwesome.EDIT);
		this.btEditar.addStyleName(ValoTheme.BUTTON_PRIMARY);
		this.btEditar.setWidth("150px");
		
		this.btExcluir = new Button("Excluir", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	excluir();
            }
        });
		this.btExcluir.setIcon(FontAwesome.TRASH);
		this.btExcluir.addStyleName(ValoTheme.BUTTON_DANGER);
		this.btExcluir.setWidth("150px");
		
		this.vlBotoes = new VerticalLayout(btAdicionar, btEditar, btExcluir);
		this.vlBotoes.setSpacing(true);
		this.vlBotoes.setMargin(true);
		this.vlBotoes.setSizeFull();
		
		this.btFiltrar = new Button("Filtrar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	try{
            		filtrar();
            		
            		atualizarGrid();
            	}catch(Exception e){
            		Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            		
            		Notification.show("Filtrar", e.getMessage(), Notification.Type.ERROR_MESSAGE);
            	}
            }
        });
		this.btFiltrar.setIcon(FontAwesome.FILTER);
		this.btFiltrar.setWidth("150px");
		
		this.hlCampos = new HorizontalLayout();
		this.hlCampos.setSpacing(true);
		
		this.vlFiltros = new VerticalLayout(this.hlCampos, this.btFiltrar);
		this.vlFiltros.setSpacing(true);
		this.vlFiltros.setMargin(true);
		
		this.panelFiltros = new Panel("Filtros");
		this.panelFiltros.setContent(this.vlFiltros);
		
		this.hlGrid = new HorizontalLayout();
		this.hlGrid.setSizeFull();
		//this.hlGrid.setSpacing(true);
		
		this.setCaption("SIREATA - Sistema de Registro de Atas");
    	this.setSizeFull();
		VerticalLayout vl = new VerticalLayout(this.panelFiltros, this.hlGrid);
		vl.setSizeFull();
		vl.setExpandRatio(this.hlGrid, 1);
		//vl.setSpacing(true);
		this.setContent(vl);
    }
    
    protected abstract void carregarGrid();
    
    public void atualizarGrid(){
    	Panel panelButtons = new Panel("Ações");
    	panelButtons.setContent(this.vlBotoes);
    	panelButtons.setWidth("170px");
    	
    	this.grid = new Grid();
    	this.grid.setSizeFull();
    	this.carregarGrid();
    	this.hlGrid.removeAllComponents();
    	this.hlGrid.addComponent(this.grid);
    	this.hlGrid.addComponent(panelButtons);
    	this.hlGrid.setComponentAlignment(panelButtons, Alignment.TOP_RIGHT);
    	this.hlGrid.setExpandRatio(this.grid, 1);
    }
    
    public void adicionarCampoFiltro(Component c){
    	if(c instanceof HorizontalLayout){
			((HorizontalLayout)c).setSpacing(true);
		}else if(c instanceof VerticalLayout){
			((VerticalLayout)c).setSpacing(true);
		}
    	
    	hlCampos.addComponent(c);
    }
    
    public void adicionarBotao(Button button){
    	button.setWidth("150px");
    	vlBotoes.addComponent(button);
    }
    
    public Grid getGrid(){
    	return this.grid;
    }
    
    public void setFiltrosVisiveis(boolean visible){
    	this.panelFiltros.setVisible(visible);
    }
    
    public boolean isFiltrosVisiveis(){
    	return this.panelFiltros.isVisible();
    }
    
    public void setBotaoAdicionarVisivel(boolean visible){
    	this.btAdicionar.setVisible(visible);
    }
    
    public void setBotaoEditarVisivel(boolean visible){
    	this.btEditar.setVisible(visible);
    }
    
    public void setBotaoExcluirVisivel(boolean visible){
    	this.btExcluir.setVisible(visible);
    }
    
    public boolean isBotaoAdicionarVisivel(){
    	return btAdicionar.isVisible();
    }
    
    public boolean isBotaoEditarVisivel(){
    	return btEditar.isVisible();
    }
    
    public boolean isBotaoExcluirVisivel(){
    	return btExcluir.isVisible();
    }
    
    public void setTextoBotaoAdicionar(String caption){
    	this.btAdicionar.setCaption(caption);
    }
    
    public void setTextoBotaoEditar(String caption){
    	this.btEditar.setCaption(caption);
    }
    
    public void setTextoBotaoExcluir(String caption){
    	this.btExcluir.setCaption(caption);
    }
    
    public String getTextoBotaoAdicionar(){
    	return this.btAdicionar.getCaption();
    }
    
    public String getTextoBotaoEditar(){
    	return this.btEditar.getCaption();
    }
    
    public String getTextoBotaoExcluir(){
    	return this.btExcluir.getCaption();
    }
    
    public void adicionarGridId(Object itemId, Object value){
    	this.gridItens.add(new GridItem(itemId, value));
    }
    
    public Object getIdSelecionado(){
    	Object itemId = grid.getSelectedRow();
    	Object value = null;
    	
    	for(GridItem i : this.gridItens){
    		if(i.itemId == itemId){
    			value = i.value;
    		}
    	}
    	
    	return value;
    }
    
    private void editar(){
    	Object value = this.getIdSelecionado();
    	
    	if(value == null){
    		Notification.show("Selecionar Registro", "Selecione o registro para edição.", Notification.Type.WARNING_MESSAGE);
    	}else{
    		this.editar(value);	
    	}
    }
    
    private void excluir(){
		Object value = this.getIdSelecionado();
    	
    	if(value == null){
    		Notification.show("Selecionar Registro", "Selecione o registro para exclusão.", Notification.Type.WARNING_MESSAGE);
    	}else{
    		ConfirmDialog.show(UI.getCurrent(), "Confirma a exclusão do registro?", new ConfirmDialog.Listener() {
                public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                    	excluir(value);
                    }
                }
            });
    	}
    }
    
    public abstract void adicionar();
    
    public abstract void editar(Object id);
    
    public abstract void excluir(Object id);
    
    public abstract void filtrar() throws Exception;

	@Override
	public void enter(ViewChangeEvent event){
		this.atualizarGrid();
	}
	
	private class GridItem{
		public Object itemId;
		public Object value;
		
		public GridItem(Object itemId, Object value){
			this.itemId = itemId;
			this.value = value;
		}
	}
	
}
