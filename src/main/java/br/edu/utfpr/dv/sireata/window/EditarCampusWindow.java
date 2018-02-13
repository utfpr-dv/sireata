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

import br.edu.utfpr.dv.sireata.bo.CampusBO;
import br.edu.utfpr.dv.sireata.model.Campus;
import br.edu.utfpr.dv.sireata.view.ListView;

public class EditarCampusWindow extends EditarWindow {
	
	private final Campus campus;
	
	private final TextField tfNome;
	private final TextField tfEndereco;
	private final CheckBox cbAtivo;
	private final Upload upLogo;
	private final Image imLogo;
	private final TextField tfSite;
	
	public EditarCampusWindow(Campus campus, ListView parentView){
		super("Editar Câmpus", parentView);
		
		if(campus == null){
			this.campus = new Campus();
		}else{
			this.campus = campus;
		}
		
		this.tfNome = new TextField("Câmpus");
		this.tfNome.setWidth("400px");
		this.tfNome.setMaxLength(100);
		
		this.tfEndereco = new TextField("Endereço");
		this.tfEndereco.setWidth("800px");
		this.tfEndereco.setMaxLength(255);
		
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
		
		this.adicionarCampo(new HorizontalLayout(this.tfNome, this.cbAtivo));
		this.adicionarCampo(this.tfEndereco);
		this.adicionarCampo(this.tfSite);
		this.adicionarCampo(new HorizontalLayout(this.upLogo, this.imLogo));
		
		this.carregarCampus();
		this.tfNome.focus();
	}
	
	private void carregarCampus(){
		this.tfNome.setValue(this.campus.getNome());
		this.tfEndereco.setValue(this.campus.getEndereco());
		this.cbAtivo.setValue(this.campus.isAtivo());
		this.tfSite.setValue(this.campus.getSite());
		
		this.carregarLogo();
	}
	
	private void carregarLogo(){
		if(this.campus.getLogo() != null){
			StreamResource resource = new StreamResource(
	            new StreamResource.StreamSource() {
	                @Override
	                public InputStream getStream() {
	                    return new ByteArrayInputStream(campus.getLogo());
	                }
	            }, "filename.png");
	
		    this.imLogo.setSource(resource);
		}
	}

	@Override
	public void salvar() {
		try{
			CampusBO bo = new CampusBO();
			
			this.campus.setNome(this.tfNome.getValue());
			this.campus.setEndereco(this.tfEndereco.getValue());
			this.campus.setAtivo(this.cbAtivo.getValue());
			this.campus.setSite(this.tfSite.getValue());
			
			bo.salvar(this.campus);
			
			Notification.show("Salvar Câmpus", "Câmpus salvo com sucesso.", Notification.Type.HUMANIZED_MESSAGE);
			
			this.atualizarGridPai();
			this.close();
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Salvar Câmpus", e.getMessage(), Notification.Type.ERROR_MESSAGE);
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
	            
	            campus.setLogo(buffer);
	            
	            carregarLogo();
	        } catch (IOException e) {
	        	Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
	            
	            Notification.show("Carregamento do Arquivo", e.getMessage(), Notification.Type.ERROR_MESSAGE);
	        }
		}

	}

}
