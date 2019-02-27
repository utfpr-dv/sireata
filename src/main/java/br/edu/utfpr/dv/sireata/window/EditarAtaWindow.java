package br.edu.utfpr.dv.sireata.window;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import br.edu.utfpr.dv.sireata.Session;
import br.edu.utfpr.dv.sireata.bo.AnexoBO;
import br.edu.utfpr.dv.sireata.bo.AtaBO;
import br.edu.utfpr.dv.sireata.bo.AtaParticipanteBO;
import br.edu.utfpr.dv.sireata.bo.CampusBO;
import br.edu.utfpr.dv.sireata.bo.DepartamentoBO;
import br.edu.utfpr.dv.sireata.bo.OrgaoBO;
import br.edu.utfpr.dv.sireata.bo.PautaBO;
import br.edu.utfpr.dv.sireata.component.ComboCampus;
import br.edu.utfpr.dv.sireata.component.ComboDepartamento;
import br.edu.utfpr.dv.sireata.component.ComboOrgao;
import br.edu.utfpr.dv.sireata.component.ComboUsuario;
import br.edu.utfpr.dv.sireata.component.ComboCampus.TipoFiltro;
import br.edu.utfpr.dv.sireata.model.Anexo;
import br.edu.utfpr.dv.sireata.model.Ata;
import br.edu.utfpr.dv.sireata.model.Campus;
import br.edu.utfpr.dv.sireata.model.Departamento;
import br.edu.utfpr.dv.sireata.model.Orgao;
import br.edu.utfpr.dv.sireata.model.OrgaoMembro;
import br.edu.utfpr.dv.sireata.model.Pauta;
import br.edu.utfpr.dv.sireata.model.Ata.TipoAta;
import br.edu.utfpr.dv.sireata.model.AtaParticipante;
import br.edu.utfpr.dv.sireata.model.AtaReport;
import br.edu.utfpr.dv.sireata.model.Usuario;
import br.edu.utfpr.dv.sireata.util.DateUtils;
import br.edu.utfpr.dv.sireata.view.ListView;

public class EditarAtaWindow extends EditarWindow {
	
	private final Ata ata;
	
	private final TabSheet tab;
	private final ComboCampus cbCampus;
	private final ComboDepartamento cbDepartamento;
	private final ComboOrgao cbOrgao;
	private final NativeSelect cbTipo;
	private final ComboUsuario cbPresidente;
	private final ComboUsuario cbSecretario;
	private final TextField tfNumero;
	private final DateField dfData;
	private final DateField dfDataLimiteComentarios;
	private final TextField tfLocal;
	private final TextField tfLocalCompleto;
	private final TextArea taConsideracoesIniciais;
	private final Button btAdicionarPauta;
	private final Button btEditarPauta;
	private final Button btRemoverPauta;
	private final Button btPautaAcima;
	private final Button btPautaAbaixo;
	private final VerticalLayout vlGridPauta;
	private Grid gridPauta;
	private final Button btAdicionarParticipante;
	private final Button btEditarParticipante;
	private final Button btRemoverParticipante;
	private final VerticalLayout vlGridParticipantes;
	private final Button btLiberarComentarios;
	private final Button btBloquearComentarios;
	private final Button btVisualizar;
	private final Button btPublicar;
	private Grid gridParticipantes;
	private final VerticalLayout vlGridAnexos;
	private Grid gridAnexos;
	private final Button btAdicionarAnexo;
	private final Button btVisualizarAnexo;
	private final Button btEditarAnexo;
	private final Button btRemoverAnexo;
	private final Button btMoverAnexoAcima;
	private final Button btMoverAnexoAbaixo;
	
	private int indexParticipante = -1;
	private int indexPauta = -1;
	private int indexAnexo = -1;
	
	public EditarAtaWindow(Ata ata, ListView parentView){
		super("Editar Ata", parentView);
		
		TipoFiltro tipoFiltro = TipoFiltro.NENHUM;
		
		if(ata == null){
			this.ata = new Ata();
		}else{
			this.ata = ata;
		}
		
		if(this.ata.getIdAta() == 0){
			tipoFiltro = TipoFiltro.CRIARATA;
		}
		
		this.dfData = new DateField("Data/Hora da Reunião");
		this.dfData.setDateFormat("dd/MM/yyyy HH:mm");
		this.dfData.setValue(this.ata.getData());
		this.dfData.setResolution(Resolution.MINUTE);
		this.dfData.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				carregarNumeroAta();
			}
		});
		
		this.cbCampus = new ComboCampus(tipoFiltro);
		this.cbCampus.setWidth("400px");
		
		this.cbDepartamento = new ComboDepartamento(this.cbCampus.getCampus().getIdCampus(), tipoFiltro);
		this.cbDepartamento.setWidth("400px");
		
		this.cbCampus.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				cbDepartamento.setIdCampus(cbCampus.getCampus().getIdCampus());
				carregarNumeroAta();
				carregarPresidenteSecretario();
				carregarMembrosOrgao();
			}
		});
		
		this.cbOrgao = new ComboOrgao(this.cbDepartamento.getDepartamento().getIdDepartamento(), tipoFiltro);
		this.cbOrgao.setWidth("400px");
		
		this.cbDepartamento.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				cbOrgao.setIdDepartamento(cbDepartamento.getDepartamento().getIdDepartamento());
				carregarNumeroAta();
				carregarPresidenteSecretario();
				carregarMembrosOrgao();
			}
		});
		
		this.cbOrgao.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				carregarNumeroAta();
				carregarPresidenteSecretario();
				carregarMembrosOrgao();
			}
		});
		
		this.cbTipo = new NativeSelect("Tipo de Reunião");
		this.cbTipo.setWidth("400px");
		this.cbTipo.addItem(TipoAta.ORDINARIA);
		this.cbTipo.addItem(TipoAta.EXTRAORDINARIA);
		this.cbTipo.setValue(TipoAta.ORDINARIA);
		this.cbTipo.setNullSelectionAllowed(false);
		this.cbTipo.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				carregarNumeroAta();
			}
		});
		
		this.cbPresidente = new ComboUsuario("Presidente");
		this.cbPresidente.setWidth("400px");
		
		this.cbSecretario = new ComboUsuario("Secretário");
		this.cbSecretario.setWidth("400px");

		this.tfNumero = new TextField("Número");
		this.tfNumero.setWidth("100px");
		
		this.dfDataLimiteComentarios = new DateField("Data Limite para Comentários");
		this.dfDataLimiteComentarios.setDateFormat("dd/MM/yyyy");
		
		this.tfLocal = new TextField("Local da Reunião");
		this.tfLocal.setWidth("810px");
		
		this.tfLocalCompleto = new TextField("Local Completo");
		this.tfLocalCompleto.setWidth("810px");
		
		this.taConsideracoesIniciais = new TextArea("Considerações Iniciais da Ata");
		this.taConsideracoesIniciais.setWidth("810px");
		this.taConsideracoesIniciais.setHeight("100px");
		
		HorizontalLayout h1 = new HorizontalLayout(this.cbCampus, this.cbDepartamento);
		h1.setSpacing(true);
		
		HorizontalLayout h2 = new HorizontalLayout(this.cbOrgao, this.cbTipo);
		h2.setSpacing(true);
		
		HorizontalLayout h3 = new HorizontalLayout(this.cbPresidente, this.cbSecretario);
		h3.setSpacing(true);
		
		HorizontalLayout h4 = new HorizontalLayout(this.dfData, this.tfNumero, this.dfDataLimiteComentarios);
		h4.setSpacing(true);
		
		VerticalLayout tab1 = new VerticalLayout(h1, h2, h3, h4, this.tfLocal, this.tfLocalCompleto);
		tab1.setSpacing(true);
		
		this.tab = new TabSheet();
		this.tab.setWidth("820px");
		this.tab.addStyleName(ValoTheme.TABSHEET_FRAMED);
		this.tab.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
		this.tab.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		
		this.tab.addTab(tab1, "Ata");
		
		this.vlGridPauta = new VerticalLayout();
		this.vlGridPauta.setSpacing(true);
		
		this.btAdicionarPauta = new Button("Adicionar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	adicionarPauta();
            }
        });
		this.btAdicionarPauta.setIcon(FontAwesome.PLUS);
		this.btAdicionarPauta.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		this.btAdicionarPauta.setWidth("150px");
		
		this.btEditarPauta = new Button("Editar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	editarPauta();
            }
        });
		this.btEditarPauta.setIcon(FontAwesome.EDIT);
		this.btEditarPauta.addStyleName(ValoTheme.BUTTON_PRIMARY);
		this.btEditarPauta.setWidth("150px");
		
		this.btRemoverPauta = new Button("Remover", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	removerPauta();
            }
        });
		this.btRemoverPauta.setIcon(FontAwesome.TRASH);
		this.btRemoverPauta.addStyleName(ValoTheme.BUTTON_DANGER);
		this.btRemoverPauta.setWidth("150px");
		
		this.btPautaAcima = new Button("Para Cima", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	moverPautaAcima();
            }
        });
		this.btPautaAcima.setIcon(FontAwesome.ARROW_UP);
		this.btPautaAcima.setWidth("150px");
		
		this.btPautaAbaixo = new Button("Para Baixo", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	moverPautaAbaixo();
            }
        });
		this.btPautaAbaixo.setIcon(FontAwesome.ARROW_DOWN);
		this.btPautaAbaixo.setWidth("150px");
		
		HorizontalLayout h6 = new HorizontalLayout(this.btAdicionarPauta, this.btEditarPauta, this.btRemoverPauta, this.btPautaAcima, this.btPautaAbaixo);
		h6.setSpacing(true);
		
		VerticalLayout tab2 = new VerticalLayout(this.taConsideracoesIniciais, this.vlGridPauta, h6);
		tab2.setSpacing(true);
		
		this.tab.addTab(tab2, "Pauta");
		
		this.vlGridParticipantes = new VerticalLayout();
		this.vlGridParticipantes.setSpacing(true);
		
		this.btAdicionarParticipante = new Button("Adicionar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	adicionarParticipante();
            }
        });
		this.btAdicionarParticipante.setIcon(FontAwesome.PLUS);
		this.btAdicionarParticipante.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		this.btAdicionarParticipante.setWidth("150px");
		
		this.btEditarParticipante = new Button("Editar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	editarParticipante();
            }
        });
		this.btEditarParticipante.setIcon(FontAwesome.EDIT);
		this.btEditarParticipante.addStyleName(ValoTheme.BUTTON_PRIMARY);
		this.btEditarParticipante.setWidth("150px");
		
		this.btRemoverParticipante = new Button("Remover", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	removerParticipante();
            }
        });
		this.btRemoverParticipante.setIcon(FontAwesome.TRASH);
		this.btRemoverParticipante.addStyleName(ValoTheme.BUTTON_DANGER);
		this.btRemoverParticipante.setWidth("150px");
		
		HorizontalLayout h5 = new HorizontalLayout(this.btAdicionarParticipante, this.btEditarParticipante, this.btRemoverParticipante);
		h5.setSpacing(true);
		
		VerticalLayout tab3 = new VerticalLayout(this.vlGridParticipantes, h5);
		tab3.setSpacing(true);
		
		this.tab.addTab(tab3, "Participantes");
		
		this.adicionarCampo(this.tab);
		
		this.vlGridAnexos = new VerticalLayout();
		this.vlGridAnexos.setSpacing(true);
		
		this.btVisualizarAnexo = new Button("Visualizar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	visualizarAnexo();
            }
        });
		this.btVisualizarAnexo.setIcon(FontAwesome.DOWNLOAD);
		this.btVisualizarAnexo.setWidth("125px");
		
		this.btAdicionarAnexo = new Button("Adicionar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	adicionarAnexo();
            }
        });
		this.btAdicionarAnexo.setIcon(FontAwesome.PLUS);
		this.btAdicionarAnexo.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		this.btAdicionarAnexo.setWidth("125px");
		
		this.btEditarAnexo = new Button("Editar", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	editarAnexo();
            }
        });
		this.btEditarAnexo.setIcon(FontAwesome.EDIT);
		this.btEditarAnexo.addStyleName(ValoTheme.BUTTON_PRIMARY);
		this.btEditarAnexo.setWidth("125px");
		
		this.btRemoverAnexo = new Button("Remover", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	removerAnexo();
            }
        });
		this.btRemoverAnexo.setIcon(FontAwesome.TRASH);
		this.btRemoverAnexo.addStyleName(ValoTheme.BUTTON_DANGER);
		this.btRemoverAnexo.setWidth("125px");
		
		this.btMoverAnexoAcima = new Button("Para Cima", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	moverAnexoAcima();
            }
        });
		this.btMoverAnexoAcima.setIcon(FontAwesome.ARROW_UP);
		this.btMoverAnexoAcima.setWidth("125px");
		
		this.btMoverAnexoAbaixo = new Button("Para Baixo", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	moverAnexoAbaixo();
            }
        });
		this.btMoverAnexoAbaixo.setIcon(FontAwesome.ARROW_DOWN);
		this.btMoverAnexoAbaixo.setWidth("125px");
		
		HorizontalLayout h7 = new HorizontalLayout(this.btVisualizarAnexo, this.btAdicionarAnexo, this.btEditarAnexo, this.btRemoverAnexo, this.btMoverAnexoAcima, this.btMoverAnexoAbaixo);
		h7.setSpacing(true);
		
		VerticalLayout tab4 = new VerticalLayout(this.vlGridAnexos, h7);
		tab4.setSpacing(true);
		
		this.tab.addTab(tab4, "Anexos");
		
		this.btLiberarComentarios = new Button("Liberar Comentários", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	liberarComentarios();
            }
        });
		this.btLiberarComentarios.setIcon(FontAwesome.UNLOCK);
		
		this.btBloquearComentarios = new Button("Bloquear Comentários", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	bloquearComentarios();
            }
        });
		this.btBloquearComentarios.setIcon(FontAwesome.LOCK);
		
		this.btPublicar = new Button("Publicar Ata", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	publicarAta();
            }
        });
		
		this.btVisualizar = new Button("Visualizar Ata", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	visualizarAta();
            }
        });
		this.btVisualizar.setIcon(FontAwesome.SEARCH);
		
		this.adicionarBotao(this.btLiberarComentarios);
		this.adicionarBotao(this.btBloquearComentarios);
		this.adicionarBotao(this.btVisualizar);
		this.adicionarBotao(this.btPublicar);
		
		this.btLiberarComentarios.setWidth("250px");
		this.btBloquearComentarios.setWidth("250px");
		
		this.carregarAta();
	}
	
	private void carregarAta(){
		try{
			if((this.ata.getOrgao() != null) && (this.ata.getOrgao().getIdOrgao() != 0)){
				DepartamentoBO dbo = new DepartamentoBO();
				Departamento departamento = dbo.buscarPorOrgao(this.ata.getOrgao().getIdOrgao());
				
				CampusBO cbo = new CampusBO();
				Campus campus = cbo.buscarPorDepartamento(departamento.getIdDepartamento());
				
				this.cbCampus.setCampus(campus);
				
				this.cbDepartamento.setIdCampus(campus.getIdCampus());
				
				this.cbDepartamento.setDepartamento(departamento);
				
				this.cbOrgao.setIdDepartamento(departamento.getIdDepartamento());
				
				this.cbOrgao.setOrgao(this.ata.getOrgao());
			}
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
		
		this.cbTipo.setValue(this.ata.getTipo());
		this.cbPresidente.setUsuario(this.ata.getPresidente());
		this.cbSecretario.setUsuario(this.ata.getSecretario());
		this.dfData.setValue(this.ata.getData());
		this.tfNumero.setValue(String.valueOf(this.ata.getNumero()));
		this.dfDataLimiteComentarios.setValue(this.ata.getDataLimiteComentarios());
		this.tfLocal.setValue(this.ata.getLocal());
		this.tfLocalCompleto.setValue(this.ata.getLocalCompleto());
		this.taConsideracoesIniciais.setValue(this.ata.getConsideracoesIniciais());
		
		this.ata.setParticipantes(null);
		this.ata.setPauta(null);
		this.ata.setAnexos(null);
		
		this.carregarParticipantes();
		this.carregaPauta();
		this.carregarAnexos();
		
		if(this.ata.getIdAta() == 0){
			carregarNumeroAta();
			carregarPresidenteSecretario();
			carregarMembrosOrgao();
			this.btLiberarComentarios.setVisible(false);
			this.btBloquearComentarios.setVisible(false);
			this.btPublicar.setVisible(false);
			this.btVisualizar.setVisible(false);
		}else{
			this.cbCampus.setEnabled(false);
			this.cbDepartamento.setEnabled(false);
			this.cbOrgao.setEnabled(false);
			
			this.btLiberarComentarios.setVisible(!this.ata.isAceitarComentarios());
			this.btBloquearComentarios.setVisible(this.ata.isAceitarComentarios());
			
			try{
				List<AtaReport> list = new ArrayList<AtaReport>();
				AtaBO bo = new AtaBO();
				
				if(!bo.isPresidenteOuSecretario(Session.getUsuario().getIdUsuario(), this.ata.getIdAta())){
					this.btLiberarComentarios.setVisible(false);
					this.btBloquearComentarios.setVisible(false);
					this.btPublicar.setVisible(false);
					this.btAdicionarPauta.setVisible(false);
					this.btRemoverPauta.setVisible(false);
					this.btPautaAcima.setVisible(false);
					this.btPautaAbaixo.setVisible(false);
					this.btAdicionarParticipante.setVisible(false);
					this.btEditarParticipante.setVisible(false);
					this.btRemoverParticipante.setVisible(false);
					this.setBotaoSalvarVisivel(false);
					this.btEditarPauta.setCaption("Visualizar Item");
					this.btAdicionarAnexo.setVisible(false);
					this.btEditarAnexo.setVisible(false);
					this.btRemoverAnexo.setVisible(false);
					this.btMoverAnexoAcima.setVisible(false);
					this.btMoverAnexoAbaixo.setVisible(false);
					this.btVisualizarAnexo.setCaption("Visualizar Anexo");
					this.btVisualizarAnexo.setWidth("150px");
				}
			}catch(Exception e){
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			}
		
			try {
				AtaBO bo = new AtaBO();
				
				if(bo.temComentarios(this.ata)){
					this.btRemoverParticipante.setEnabled(false);
					this.btRemoverPauta.setEnabled(false);
				}
			} catch (Exception e) {
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	private void visualizarAta() {
		try {
			AtaBO bo = new AtaBO();
			
			this.showReport(bo.gerarAta(this.ata));
		} catch(Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Visualizar Ata", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void salvar() {
		try{
			AtaBO bo = new AtaBO();
			
			this.ata.setOrgao(this.cbOrgao.getOrgao());
			this.ata.setPresidente(this.cbPresidente.getUsuario());
			this.ata.setSecretario(this.cbSecretario.getUsuario());
			this.ata.setTipo((TipoAta)this.cbTipo.getValue());
			this.ata.setData(this.dfData.getValue());
			this.ata.setNumero(Integer.parseInt(this.tfNumero.getValue()));
			this.ata.setDataLimiteComentarios(this.dfDataLimiteComentarios.getValue());
			this.ata.setLocal(this.tfLocal.getValue());
			this.ata.setLocalCompleto(this.tfLocalCompleto.getValue());
			this.ata.setConsideracoesIniciais(this.taConsideracoesIniciais.getValue());
			
			bo.salvar(this.ata);
			
			Notification.show("Salvar Ata", "Ata salva com sucesso.", Notification.Type.HUMANIZED_MESSAGE);
			
			this.atualizarGridPai();
			this.close();
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Salvar Ata", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
	private void carregarNumeroAta(){
		if(ata.getIdAta() == 0){
			try {
				AtaBO bo = new AtaBO();
				int numero = bo.buscarProximoNumeroAta(this.cbOrgao.getOrgao().getIdOrgao(), DateUtils.getYear(this.dfData.getValue()), (TipoAta)this.cbTipo.getValue());
				
				this.tfNumero.setValue(String.valueOf(numero));
			} catch (Exception e) {
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
				
				Notification.show("Número de Ata", e.getMessage(), Notification.Type.ERROR_MESSAGE);
			}	
		}
	}
	
	private void carregarMembrosOrgao(){
		this.ata.setParticipantes(new ArrayList<AtaParticipante>());
		
		try {
			OrgaoBO bo = new OrgaoBO();
			Orgao orgao = bo.buscarPorId(this.cbOrgao.getOrgao().getIdOrgao());
			
			if(orgao != null){
				for(OrgaoMembro membro : orgao.getMembros()){
					AtaParticipante participante = new AtaParticipante();
					
					participante.setParticipante(membro.getUsuario());
					participante.setDesignacao(membro.getDesignacao());
					participante.setMembro(true);
					
					this.ata.getParticipantes().add(participante);
				}
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Carregar Membros", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
		
		this.carregarParticipantes();
	}
	
	private void carregarParticipantes(){
		this.gridParticipantes = new Grid();
		this.gridParticipantes.addColumn("Participante", String.class);
		this.gridParticipantes.addColumn("Designação", String.class);
		this.gridParticipantes.addColumn("Membro", String.class);
		this.gridParticipantes.addColumn("Presente", String.class);
		this.gridParticipantes.getColumns().get(2).setWidth(100);
		this.gridParticipantes.getColumns().get(3).setWidth(100);
		this.gridParticipantes.setWidth("810px");
		this.gridParticipantes.setHeight("310px");
		
		this.vlGridParticipantes.removeAllComponents();
		this.vlGridParticipantes.addComponent(this.gridParticipantes);
		
		if(this.ata.getParticipantes() == null){
			if(this.ata.getIdAta() == 0){
				this.ata.setParticipantes(new ArrayList<AtaParticipante>());
			}else{
				try {
					AtaParticipanteBO bo = new AtaParticipanteBO();
					
					this.ata.setParticipantes(bo.listarPorAta(this.ata.getIdAta()));
				} catch (Exception e) {
					Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
					
					Notification.show("Participantes da Reunião", e.getMessage(), Notification.Type.ERROR_MESSAGE);
				}
			}
		}
		
		for(AtaParticipante p : this.ata.getParticipantes()){
			this.gridParticipantes.addRow(p.getParticipante().getNome(), p.getDesignacao(), (p.isMembro() ? "Sim" : "Não"), (p.isPresente() ? "Sim" : "Não"));
		}
	}
	
	private void carregarAnexos(){
		this.gridAnexos = new Grid();
		this.gridAnexos.addColumn("Ordem", String.class);
		this.gridAnexos.addColumn("Descrição", String.class);
		this.gridAnexos.getColumns().get(0).setWidth(200);
		this.gridAnexos.setWidth("810px");
		this.gridAnexos.setHeight("310px");
		
		this.vlGridAnexos.removeAllComponents();
		this.vlGridAnexos.addComponent(this.gridAnexos);
		
		if(this.ata.getAnexos() == null){
			if(this.ata.getIdAta() == 0){
				this.ata.setAnexos(new ArrayList<Anexo>());
			}else{
				try {
					AnexoBO bo = new AnexoBO();
					
					this.ata.setAnexos(bo.listarPorAta(this.ata.getIdAta()));
				} catch (Exception e) {
					Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
					
					Notification.show("Anexos", e.getMessage(), Notification.Type.ERROR_MESSAGE);
				}
			}
		}
		
		int i = 1;
		for(Anexo a : this.ata.getAnexos()){
			this.gridAnexos.addRow("Anexo " + String.valueOf(i), a.getDescricao());
			i++;
		}
	}
	
	private void carregarPresidenteSecretario(){
		try {
			OrgaoBO bo = new OrgaoBO();
			Usuario presidente = bo.buscarPresidente(this.cbOrgao.getOrgao().getIdOrgao());
			Usuario secretario = bo.buscarSecretario(this.cbOrgao.getOrgao().getIdOrgao());
			
			this.cbPresidente.setUsuario(presidente);
			this.cbSecretario.setUsuario(secretario);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Informações do Órgão", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
	private void carregaPauta(){
		this.gridPauta = new Grid();
		this.gridPauta.addColumn("Item", Integer.class);
		this.gridPauta.getColumns().get(0).setWidth(100);
		this.gridPauta.addColumn("Título", String.class);
		this.gridPauta.setWidth("810px");
		this.gridPauta.setHeight("175px");
		
		this.vlGridPauta.removeAllComponents();
		this.vlGridPauta.addComponent(this.gridPauta);
		
		if(this.ata.getPauta() == null){
			if(this.ata.getIdAta() == 0){
				this.ata.setPauta(new ArrayList<Pauta>());
			}else{
				try {
					PautaBO bo = new PautaBO();
					this.ata.setPauta(bo.listarPorAta(this.ata.getIdAta()));
				} catch (Exception e) {
					Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
					
					Notification.show("Pauta da Reunião", e.getMessage(), Notification.Type.ERROR_MESSAGE);
				}
			}
		}
		
		int ordem = 1;
		for(Pauta p : this.ata.getPauta()){
			p.setOrdem(ordem);
			
			this.gridPauta.addRow(p.getOrdem(), p.getTitulo());
			
			ordem++;
		}
	}
	
	private void adicionarParticipante(){
		AtaParticipante participante = new AtaParticipante();
		
		participante.setAta(this.ata);
		
		this.indexParticipante = -1;
		
		UI.getCurrent().addWindow(new EditarParticipanteWindow(participante, this));
	}
	
	private void editarParticipante(){
		this.indexParticipante = this.getIndexParticipanteSelecionado();
		
		if(this.indexParticipante == -1){
			Notification.show("Editar Participante", "Selecione o participante para editar.", Notification.Type.WARNING_MESSAGE);
		}else{
			UI.getCurrent().addWindow(new EditarParticipanteWindow(this.ata.getParticipantes().get(this.indexParticipante), this));
		}
	}
	
	private void removerParticipante(){
		int index = this.getIndexParticipanteSelecionado();
		
		if(index == -1){
    		Notification.show("Remover Participante", "Selecione o participante para remover.", Notification.Type.WARNING_MESSAGE);
    	}else{
    		ConfirmDialog.show(UI.getCurrent(), "Confirma a exclusão do registro?", new ConfirmDialog.Listener() {
                public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                    	try {
                    		AtaParticipante participante = ata.getParticipantes().get(index);
                    		
                			if(participante.getIdAtaParticipante() > 0){
	                			AtaParticipanteBO bo = new AtaParticipanteBO();
	                			
								bo.excluir(participante);
                			}
                        	
                        	ata.getParticipantes().remove(index);
                        	
                        	carregarParticipantes();
						} catch (Exception e) {
							Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
							
							Notification.show("Remover Participante", e.getMessage(), Notification.Type.ERROR_MESSAGE);
						}
                    }
                }
            });
    	}
	}
	
	private int getIndexParticipanteSelecionado(){
    	Object itemId = this.gridParticipantes.getSelectedRow();

    	if(itemId == null){
    		return -1;
    	}else{
    		return ((int)itemId) - 1;	
    	}
    }
	
	public void atualizarParticipante(AtaParticipante participante){
		if(this.indexParticipante == -1){
			boolean encontrou = false;
			
			for(AtaParticipante p : this.ata.getParticipantes()){
				if(p.getParticipante().getIdUsuario() == participante.getParticipante().getIdUsuario()){
					encontrou = true;
					break;
				}
			}
			
			if(!encontrou){
				this.ata.getParticipantes().add(participante);	
			}else{
				Notification.show("Adicionar Participante", "Usuário já consta na lista de participantes.", Notification.Type.WARNING_MESSAGE);
			}
		}else{
			this.ata.getParticipantes().set(this.indexParticipante, participante);
		}
		
		this.carregarParticipantes();
	}
	
	private void visualizarAnexo() {
		this.indexAnexo = this.getIndexAnexoSelecionado();
		
		if(this.indexAnexo == -1){
			Notification.show("Visualizar Anexo", "Selecione o anexo para visualizar.", Notification.Type.WARNING_MESSAGE);
		}else{
			this.showReport(this.ata.getAnexos().get(indexAnexo).getArquivo());
		}
	}
	
	private void adicionarAnexo(){
		Anexo anexo = new Anexo();
		
		anexo.setAta(this.ata);
		
		this.indexAnexo = -1;
		
		UI.getCurrent().addWindow(new EditarAnexoWindow(anexo, this));
	}
	
	private void editarAnexo(){
		this.indexAnexo = this.getIndexAnexoSelecionado();
		
		if(this.indexAnexo == -1){
			Notification.show("Editar Anexo", "Selecione o anexo para editar.", Notification.Type.WARNING_MESSAGE);
		}else{
			UI.getCurrent().addWindow(new EditarAnexoWindow(this.ata.getAnexos().get(this.indexAnexo), this));
		}
	}
	
	private void removerAnexo(){
		int index = this.getIndexAnexoSelecionado();
		
		if(index == -1){
    		Notification.show("Remover Anexo", "Selecione o anexo para remover.", Notification.Type.WARNING_MESSAGE);
    	}else{
    		ConfirmDialog.show(UI.getCurrent(), "Confirma a exclusão do registro?", new ConfirmDialog.Listener() {
                public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                    	try {
                    		Anexo anexo = ata.getAnexos().get(index);
                    		
                			if(anexo.getIdAnexo() > 0){
	                			AnexoBO bo = new AnexoBO();
	                			
								bo.excluir(anexo);
                			}
                        	
                        	ata.getAnexos().remove(index);
                        	
                        	carregarAnexos();
						} catch (Exception e) {
							Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
							
							Notification.show("Remover Anexo", e.getMessage(), Notification.Type.ERROR_MESSAGE);
						}
                    }
                }
            });
    	}
	}
	
	private void moverAnexoAcima() {
		int index = this.getIndexAnexoSelecionado();
		
		if(index == -1){
    		Notification.show("Mover Anexo", "Selecione o anexo para mover.", Notification.Type.WARNING_MESSAGE);
    	}else if(index > 0){
			Anexo a = this.ata.getAnexos().get(index);
			
			this.ata.getAnexos().set(index, this.ata.getAnexos().get(index - 1));
			this.ata.getAnexos().set(index - 1, a);
			
			this.carregarAnexos();
		}
	}
	
	private void moverAnexoAbaixo() {
		int index = this.getIndexAnexoSelecionado();
		
		if(index == -1){
    		Notification.show("Mover Anexo", "Selecione o anexo para mover.", Notification.Type.WARNING_MESSAGE);
    	}else if(index < this.ata.getAnexos().size() - 1){
    		Anexo a = this.ata.getAnexos().get(index);
			
			this.ata.getAnexos().set(index, this.ata.getAnexos().get(index + 1));
			this.ata.getAnexos().set(index + 1, a);
			
			this.carregarAnexos();
		}
	}
	
	private int getIndexAnexoSelecionado(){
    	Object itemId = this.gridAnexos.getSelectedRow();

    	if(itemId == null){
    		return -1;
    	}else{
    		return ((int)itemId) - 1;	
    	}
    }
	
	public void atualizarAnexo(Anexo anexo){
		if(this.indexAnexo == -1){
			this.ata.getAnexos().add(anexo);
		}else{
			this.ata.getAnexos().set(this.indexAnexo, anexo);
		}
		
		this.carregarAnexos();
	}
	
	private void adicionarPauta(){
		Pauta pauta = new Pauta();
		
		pauta.setAta(this.ata);
		pauta.setOrdem(this.ata.getPauta().size() + 1);
		
		this.indexPauta = -1;
		
		UI.getCurrent().addWindow(new EditarPautaWindow(pauta, false, true, this));
	}
	
	private void editarPauta(){
		this.indexPauta = this.getIndexPautaSelecionada();
		
		if(this.indexPauta == -1){
			Notification.show("Editar Pauta", "Selecione a pauta para editar.", Notification.Type.WARNING_MESSAGE);
		}else{
			UI.getCurrent().addWindow(new EditarPautaWindow(this.ata.getPauta().get(this.indexPauta), this.ata.isAceitarComentarios(), this.isBotaoSalvarVisivel(), this));
		}
	}
	
	private void removerPauta(){
		int index = this.getIndexPautaSelecionada();
		
		if(index == -1){
    		Notification.show("Remover Pauta", "Selecione a pauta para remover.", Notification.Type.WARNING_MESSAGE);
    	}else{
    		ConfirmDialog.show(UI.getCurrent(), "Confirma a exclusão do registro?", new ConfirmDialog.Listener() {
                public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                    	try {
                    		Pauta pauta = ata.getPauta().get(index);
                    		
                			if(pauta.getIdPauta() > 0){
	                			PautaBO bo = new PautaBO();
	                			
								bo.excluir(pauta);
                			}
                        	
                        	ata.getPauta().remove(index);
                        	
                        	carregaPauta();
						} catch (Exception e) {
							Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
							
							Notification.show("Remover Pauta", e.getMessage(), Notification.Type.ERROR_MESSAGE);
						}
                    }
                }
            });
    	}
	}
	
	private int getIndexPautaSelecionada(){
    	Object itemId = this.gridPauta.getSelectedRow();

    	if(itemId == null){
    		return -1;
    	}else{
    		return ((int)itemId) - 1;	
    	}
    }
	
	public void atualizarPauta(Pauta pauta){
		if(this.indexPauta == -1){
			this.ata.getPauta().add(pauta);
		}else{
			this.ata.getPauta().set(this.indexPauta, pauta);
		}
		
		this.carregaPauta();
	}
	
	private void moverPautaAcima(){
		int index = this.getIndexPautaSelecionada();
		
		if(index == -1){
    		Notification.show("Mover Pauta", "Selecione a pauta para mover.", Notification.Type.WARNING_MESSAGE);
    	}else if(index > 0){
			Pauta p = this.ata.getPauta().get(index);
			
			this.ata.getPauta().set(index, this.ata.getPauta().get(index - 1));
			this.ata.getPauta().set(index - 1, p);
			
			this.carregaPauta();
		}
	}
	
	private void moverPautaAbaixo(){
		int index = this.getIndexPautaSelecionada();
		
		if(index == -1){
    		Notification.show("Mover Pauta", "Selecione a pauta para mover.", Notification.Type.WARNING_MESSAGE);
    	}else if(index < this.ata.getPauta().size() - 1){
			Pauta p = this.ata.getPauta().get(index);
			
			this.ata.getPauta().set(index, this.ata.getPauta().get(index + 1));
			this.ata.getPauta().set(index + 1, p);
			
			this.carregaPauta();
		}
	}
	
	private void liberarComentarios(){
		try {
			AtaBO bo = new AtaBO();
			
			bo.liberarComentarios(this.ata);
			
			this.ata.setAceitarComentarios(true);
			
			this.btLiberarComentarios.setVisible(false);
			this.btBloquearComentarios.setVisible(true);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Liberar Comentários", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}		
	}
	
	private void bloquearComentarios(){
		try {
			AtaBO bo = new AtaBO();
			
			bo.bloquearComentarios(this.ata);
			
			this.ata.setAceitarComentarios(false);
			
			this.btLiberarComentarios.setVisible(true);
			this.btBloquearComentarios.setVisible(false);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Bloquear Comentários", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
	private void publicarAta(){
		try {
			ConfirmDialog.show(UI.getCurrent(), "Confirma a publicação da ata? Esta ação não poderá ser revertida.", new ConfirmDialog.Listener() {
                public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                    	try {
                    		AtaBO bo = new AtaBO();
                    		
                    		bo.publicar(ata);
                    		
                    		Notification.show("Publicar Ata", "Ata publicada com sucesso.", Notification.Type.WARNING_MESSAGE);
                    		
                    		atualizarGridPai();
                    		close();
						} catch (Exception e) {
							Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
							
							Notification.show("Publicar Ata", e.getMessage(), Notification.Type.ERROR_MESSAGE);
						}
                    }
                }
            });
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Publicar Ata", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
}
