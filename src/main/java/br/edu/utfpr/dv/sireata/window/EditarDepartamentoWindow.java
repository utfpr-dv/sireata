package br.edu.utfpr.dv.sireata.window;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import br.edu.utfpr.dv.sireata.bo.DepartamentoBO;
import br.edu.utfpr.dv.sireata.component.ComboCampus;
import br.edu.utfpr.dv.sireata.component.ComboCampus.TipoFiltro;
import br.edu.utfpr.dv.sireata.model.Departamento;
import br.edu.utfpr.dv.sireata.view.ListView;

public class EditarDepartamentoWindow extends EditarWindow {
	
	private final Departamento departamento;
	
	private final ComboCampus cbCampus;
	private final TextField tfNome;
	private final TextField tfNomeCompleto;
	private final CheckBox cbAtivo;
	private final Upload upLogo;
	private final Image imLogo;
	private final TextField tfSite;

	public EditarDepartamentoWindow(Departamento departamento, ListView parentView){
		super("Editar Departamento", parentView);
		
		if(departamento == null){
			this.departamento = new Departamento();
		}else{
			this.departamento = departamento;
		}
		
		this.cbCampus = new ComboCampus(TipoFiltro.NENHUM);
		this.cbCampus.setFiltrarSomenteAtivos(false);
		
		this.tfNome = new TextField("Nome");
		this.tfNome.setWidth("400px");
		this.tfNome.setMaxLength(100);
		
		this.tfNomeCompleto = new TextField("Nome Detalhado");
		this.tfNomeCompleto.setWidth("800px");
		this.tfNomeCompleto.setMaxLength(255);
		
		this.tfSite = new TextField("Site");
		this.tfSite.setWidth("800px");
		this.tfSite.setMaxLength(255);
		
		this.cbAtivo = new CheckBox("Ativo");
		
		DocumentUploader listener = new DocumentUploader();
		this.upLogo = new Upload("Enviar Logotipo", listener);
		this.upLogo.addSucceededListener(listener);
		this.upLogo.setButtonCaption("Enviar");
		
		this.imLogo = new Image();
		this.imLogo.setStyleName("ImageLogo");
		this.imLogo.setWidth("400px");
		this.imLogo.setHeight("200px");
		
		this.adicionarCampo(new HorizontalLayout(this.tfNome, this.cbCampus));
		this.adicionarCampo(this.tfNomeCompleto);
		this.adicionarCampo(this.tfSite);
		this.adicionarCampo(this.cbAtivo);
		this.adicionarCampo(new HorizontalLayout(this.upLogo, this.imLogo));
		
		this.carregarDepartamento();
		this.tfNome.focus();
	}
	
	private void carregarDepartamento(){
		this.cbCampus.setCampus(this.departamento.getCampus());
		this.tfNome.setValue(this.departamento.getNome());
		this.cbAtivo.setValue(this.departamento.isAtivo());
		this.tfSite.setValue(this.departamento.getSite());
		this.tfNomeCompleto.setValue(this.departamento.getNomeCompleto());
		
		this.carregarLogo();
		
		if(this.departamento.getIdDepartamento() > 0){
			this.cbCampus.setEnabled(false);
		}
	}
	
	private void carregarLogo(){
		if(this.departamento.getLogo() != null){
			StreamResource resource = new StreamResource(
	            new StreamResource.StreamSource() {
	                @Override
	                public InputStream getStream() {
	                    return new ByteArrayInputStream(departamento.getLogo());
	                }
	            }, "filename.png");
	
		    this.imLogo.setSource(resource);
		}
	}
	
	@Override
	public void salvar() {
		try{
			DepartamentoBO bo = new DepartamentoBO();
			
			this.departamento.setCampus(this.cbCampus.getCampus());
			this.departamento.setNome(this.tfNome.getValue());
			this.departamento.setAtivo(this.cbAtivo.getValue());
			this.departamento.setSite(this.tfSite.getValue());
			this.departamento.setNomeCompleto(this.tfNomeCompleto.getValue());
			
			bo.salvar(this.departamento);
			
			Notification.show("Salvar Departamento", "Departamento salvo com sucesso.", Notification.Type.HUMANIZED_MESSAGE);
			
			this.atualizarGridPai();
			this.close();
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Salvar Departamento", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
	@SuppressWarnings("serial")
	class DocumentUploader implements Receiver, SucceededListener {
		private File tempFile;
		
		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			try {
				if(!mimeType.equals("image/jpeg") && !mimeType.equals("image/png")){
					throw new Exception("O arquivo enviado é inválido. São aceitos apenas arquivos JPG e PNG.");
				}
				
	            tempFile = File.createTempFile(filename, "tmp");
	            tempFile.deleteOnExit();
	            return new FileOutputStream(tempFile);
	        } catch (Exception e) {
	        	Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
	            
	            Notification.show("Carregamento do Arquivo", e.getMessage(), Notification.Type.ERROR_MESSAGE);
	        }

	        return null;
		}
		
		@Override
		public void uploadSucceeded(SucceededEvent event) {
			try {
	            FileInputStream input = new FileInputStream(tempFile);
	            byte[] buffer = new byte[input.available()];
	            
	            input.read(buffer);
	            
	            departamento.setLogo(buffer);
	            
	            carregarLogo();
	        } catch (IOException e) {
	        	Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
	            
	            Notification.show("Carregamento do Arquivo", e.getMessage(), Notification.Type.ERROR_MESSAGE);
	        }
		}
	}

}
