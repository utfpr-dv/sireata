package br.edu.utfpr.dv.sireata.window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import br.edu.utfpr.dv.sireata.bo.AnexoBO;
import br.edu.utfpr.dv.sireata.model.Anexo;

public class EditarAnexoWindow extends EditarWindow {

	private final Anexo anexo;
	private final EditarAtaWindow parentWindow;
	
	private final TextField textDescricao;
	private final Upload uploadArquivo;
	
	public EditarAnexoWindow(Anexo anexo, EditarAtaWindow parentWindow) {
		super("Editar Anexo", null);
		
		if(anexo == null){
			this.anexo = new Anexo();
		}else{
			this.anexo = anexo;
		}
		
		this.parentWindow = parentWindow;
		
		this.textDescricao = new TextField("Descrição do Anexo");
		this.textDescricao.setWidth("300px");
		this.textDescricao.setMaxLength(50);
		
		DocumentUploader listener = new DocumentUploader();
		this.uploadArquivo = new Upload("(Formato PDF, Tam. Máx. 1 MB)", listener);
		this.uploadArquivo.addSucceededListener(listener);
		this.uploadArquivo.setButtonCaption("Enviar Arquivo");
		this.uploadArquivo.setImmediate(true);
		
		this.adicionarCampo(this.textDescricao);
		this.adicionarCampo(this.uploadArquivo);
		
		this.carregarAnexo();
	}
	
	private void carregarAnexo() {
		this.textDescricao.setValue(this.anexo.getDescricao());
	}
	
	@Override
	public void salvar() {
		try{
			this.anexo.setDescricao(this.textDescricao.getValue());
			
			AnexoBO bo = new AnexoBO();
			
			bo.validarDados(this.anexo);
			
			if(this.anexo.getAta().getIdAta() > 0){
				bo.salvar(this.anexo);
			}
			
			Notification.show("Salvar Anexo", "Anexo salvo com sucesso.", Notification.Type.HUMANIZED_MESSAGE);
			
			this.parentWindow.atualizarAnexo(this.anexo);
			
			this.close();
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			Notification.show("Salvar Anexo", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
	@SuppressWarnings("serial")
	class DocumentUploader implements Receiver, SucceededListener {
		private File tempFile;
		
		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			try {
				//imageFileUploaded.setVisible(false);
				
				if(!mimeType.equals("application/pdf")){
					throw new Exception("O arquivo precisa estar no formato PDF.");
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
	            
	            if(input.available() > (2048 * 1024)){
					throw new Exception("O arquivo precisa ter um tamanho máximo de 200 KB.");
	            }
	            
	            byte[] buffer = new byte[input.available()];
	            
	            input.read(buffer);
	            
	            anexo.setArquivo(buffer);
	            
	            //imageFileUploaded.setVisible(true);
	            
	            Notification.show("Carregamento do Arquivo", "O arquivo foi enviado com sucesso.\n\nClique em SALVAR para concluir a submissão.", Notification.Type.HUMANIZED_MESSAGE);
	        } catch (Exception e) {
	        	Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
	            
	            Notification.show("Carregamento do Arquivo", e.getMessage(), Notification.Type.ERROR_MESSAGE);
	        }
		}
	}

}
