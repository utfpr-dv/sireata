package br.edu.utfpr.dv.sireata.bo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import br.edu.utfpr.dv.sireata.dao.AnexoDAO;
import br.edu.utfpr.dv.sireata.dao.AtaDAO;
import br.edu.utfpr.dv.sireata.dao.AtaParticipanteDAO;
import br.edu.utfpr.dv.sireata.dao.OrgaoDAO;
import br.edu.utfpr.dv.sireata.dao.PautaDAO;
import br.edu.utfpr.dv.sireata.model.Anexo;
import br.edu.utfpr.dv.sireata.model.Ata;
import br.edu.utfpr.dv.sireata.model.Pauta;
import br.edu.utfpr.dv.sireata.util.DateUtils;
import br.edu.utfpr.dv.sireata.util.ReportUtils;
import br.edu.utfpr.dv.sireata.util.StringUtils;
import br.edu.utfpr.dv.sireata.model.Ata.TipoAta;
import br.edu.utfpr.dv.sireata.model.AtaParticipante;
import br.edu.utfpr.dv.sireata.model.AtaReport;
import br.edu.utfpr.dv.sireata.model.Orgao;
import br.edu.utfpr.dv.sireata.model.ParticipanteReport;

public class AtaBO {
	
	public Ata buscarPorId(int id) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.buscarPorId(id);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public Ata buscarPorNumero(int idOrgao, TipoAta tipo, int numero, int ano) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.buscarPorNumero(idOrgao, tipo, numero, ano);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public Ata buscarPorPauta(int idPauta) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.buscarPorPauta(idPauta);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public int buscarProximoNumeroAta(int idOrgao, int ano, TipoAta tipo) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.buscarProximoNumeroAta(idOrgao, ano, tipo);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Ata> listarPublicadas() throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.listarPublicadas();
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Ata> listarPorOrgao(int idOrgao) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.listarPorOrgao(idOrgao);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Ata> listarPorDepartamento(int idDepartamento) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.listarPorDepartamento(idDepartamento);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Ata> listarPorCampus(int idCampus) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.listarPorCampus(idCampus);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Ata> listarNaoPublicadas(int idUsuario) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.listarNaoPublicadas(idUsuario);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Ata> listarPorOrgao(int idOrgao, int idUsuario) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.listarPorOrgao(idOrgao, idUsuario);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Ata> listarPorDepartamento(int idDepartamento, int idUsuario) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.listarPorDepartamento(idDepartamento, idUsuario);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Ata> listarPorCampus(int idCampus, int idUsuario) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.listarPorCampus(idCampus, idUsuario);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Ata> listar(int idCampus, int idDepartamento, int idOrgao, boolean publicada, int idUsuario) throws Exception{
		if(publicada){
			if(idOrgao >0 ){
				return this.listarPorOrgao(idOrgao);
			}else if(idDepartamento > 0){
				return this.listarPorDepartamento(idDepartamento);
			}else if(idCampus > 0){
				return this.listarPorCampus(idCampus);
			}else{
				return this.listarPublicadas();
			}
		}else{
			if(idOrgao >0 ){
				return this.listarPorOrgao(idOrgao, idUsuario);
			}else if(idDepartamento > 0){
				return this.listarPorDepartamento(idDepartamento, idUsuario);
			}else if(idCampus > 0){
				return this.listarPorCampus(idCampus, idUsuario);
			}else{
				return this.listarNaoPublicadas(idUsuario);
			}
		}
	}
	
	public int salvar(Ata ata) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			int id = dao.salvar(ata);
			
			if(ata.getPauta() != null){
				int i = 1;
				
				for(Pauta p : ata.getPauta()){
					PautaDAO pdao = new PautaDAO();
					
					p.getAta().setIdAta(id);
					p.setOrdem(i);
					pdao.salvar(p);
					i++;
				}
			}
			
			if(ata.getParticipantes() != null){
				for(AtaParticipante p : ata.getParticipantes()){
					AtaParticipanteDAO pdao = new AtaParticipanteDAO();
					
					p.getAta().setIdAta(id);
					pdao.salvar(p);
				}
			}
			
			if(ata.getAnexos() != null) {
				int i = 1;
				
				for(Anexo a : ata.getAnexos()) {
					AnexoDAO adao = new AnexoDAO();
					
					a.getAta().setIdAta(id);
					a.setOrdem(i);
					adao.salvar(a);
					i++;
				}
			}
			
			return id;
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean temComentarios(Ata ata) throws Exception{
		return this.temComentarios(ata.getIdAta());
	}
	
	public boolean temComentarios(int idAta) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.temComentarios(idAta);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean isPresidenteOuSecretario(int idUsuario, int idAta) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.isPresidenteOuSecretario(idUsuario, idAta);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean isPresidente(int idUsuario, int idAta) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.isPresidente(idUsuario, idAta);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean isPublicada(int idAta) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			return dao.isPublicada(idAta);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public void liberarComentarios(Ata ata) throws Exception{
		this.liberarComentarios(ata.getIdAta());
	}
	
	public void liberarComentarios(int idAta) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			dao.liberarComentarios(idAta);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public void bloquearComentarios(Ata ata) throws Exception{
		this.bloquearComentarios(ata.getIdAta());
	}
	
	public void bloquearComentarios(int idAta) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			
			dao.bloquearComentarios(idAta);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public AtaReport gerarAtaReport(Ata ata) throws Exception{
		return this.gerarAtaReport(ata.getIdAta());
	}
	
	public AtaReport gerarAtaReport(int idAta) throws Exception{
		try{
			PautaBO pbo = new PautaBO();
			AtaParticipanteBO apbo = new AtaParticipanteBO();
			OrgaoBO obo = new OrgaoBO();
			Ata ata = this.buscarPorId(idAta);
			Orgao orgao = obo.buscarPorId(ata.getOrgao().getIdOrgao());
			AtaReport report = new AtaReport();
			String texto;
			DecimalFormat df = new DecimalFormat("00");
			
			report.setNumero(df.format(ata.getNumero()) + "/" + String.valueOf(DateUtils.getYear(ata.getData())));
			report.setDataHora(DateUtils.format(ata.getData(), "dd/MM/yyyy") + " às " + DateUtils.format(ata.getData(), "HH") + " horas" + (DateUtils.getMinute(ata.getData()) > 0 ? " e " + DateUtils.format(ata.getData(), "mm") + " minutos" : "") + ".");
			report.setLocal(ata.getLocal());
			report.setPresidente(ata.getPresidente().getNome());
			report.setSecretario(ata.getSecretario().getNome());
			
			texto = this.getDataExtenso(ata.getData()) + ", no " + StringEscapeUtils.escapeHtml4(ata.getLocalCompleto()) + 
					" realizou-se a " + StringUtils.getExtensoOrdinal(ata.getNumero(), true) +
					" reunião " + (ata.getTipo() == TipoAta.ORDINARIA ? "ordinária" : "extraordinária") +
					" de " + String.valueOf(DateUtils.getYear(ata.getData())) + " do(a) " +
					StringEscapeUtils.escapeHtml4(orgao.getNomeCompleto()) + ", a qual foi conduzida pelo(a) " + 
					(ata.getPresidente().getIdUsuario() == orgao.getPresidente().getIdUsuario() ? StringEscapeUtils.escapeHtml4(orgao.getDesignacaoPresidente()) + " " : "professor(a) ") +
					StringEscapeUtils.escapeHtml4(ata.getPresidente().getNome()) + " e teve como pauta: <b>";
			
			ata.setPauta(pbo.listarPorAta(idAta));
			
			for(int i = 1; i <= ata.getPauta().size(); i++){
				texto += "(" + String.valueOf(i) + ") " + StringEscapeUtils.escapeHtml4(ata.getPauta().get(i - 1).getTitulo()) + (i == ata.getPauta().size() ? "." : "; ");
			}
			
			texto += "</b> " + ata.getConsideracoesIniciais() + " ";
			
			for(int i = 1; i <= ata.getPauta().size(); i++){
				texto += "<b>(" + String.valueOf(i) + ") " + StringEscapeUtils.escapeHtml4(ata.getPauta().get(i - 1).getTitulo()) + 
						"</b>, " + StringEscapeUtils.escapeHtml4(ata.getPauta().get(i - 1).getDescricao()) + " ";
			}
			
			texto += " Nada mais havendo a tratar, deu-se por encerrada a reunião, da qual eu, " +
					StringEscapeUtils.escapeHtml4(ata.getSecretario().getNome()) + ", lavrei a presente ata que, após aprovada, vai assinada por mim e pelos demais presentes.";
			
			report.setTexto(texto);
			
			ata.setParticipantes(apbo.listarPorAta(idAta));
			
			for(AtaParticipante participante : ata.getParticipantes()){
				OrgaoDAO odao = new OrgaoDAO();
				ParticipanteReport pr = new ParticipanteReport();
				
				pr.setNome(participante.getParticipante().getNome() + (participante.getDesignacao().isEmpty() ? "" : " (" + participante.getDesignacao() + ")"));
				pr.setPresente(participante.isPresente());
				if(!participante.isPresente()){
					pr.setMotivo(participante.getMotivo());					
				}
				
				if(participante.isMembro()){
					report.getParticipantesMembros().add(pr);
				}else{
					report.getDemaisParticipantes().add(pr);
				}
			}
			
			int i = 1;
			for(ParticipanteReport pr : report.getParticipantesMembros()){
				pr.setOrdem(i++);
			}
			for(ParticipanteReport pr : report.getDemaisParticipantes()){
				pr.setOrdem(i++);
			}
			
			return report;
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public byte[] gerarAta(Ata ata) throws Exception{
		return this.gerarAta(ata.getIdAta());
	}
	
	public byte[] gerarAta(int idAta) throws Exception{
		try{
			AtaReport report = this.gerarAtaReport(idAta);
			
			List<AtaReport> list = new ArrayList<AtaReport>();
			list.add(report);
			
			ByteArrayOutputStream pdf = new ReportUtils().createPdfStream(list, "Ata");
			
			AnexoBO bo = new AnexoBO();
			List<Anexo> anexos = bo.listarPorAta(idAta);
			
			if(anexos.size() > 0) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				PDFMergerUtility pdfMerge = new PDFMergerUtility();
				
				pdfMerge.setDestinationStream(output);
				
				pdfMerge.addSource(new ByteArrayInputStream(pdf.toByteArray()));
				for(Anexo a : anexos){
					pdfMerge.addSource(new ByteArrayInputStream(a.getArquivo()));
				}
				
				pdfMerge.mergeDocuments(null);
				
				return output.toByteArray();	
			} else {
				return pdf.toByteArray();				
			}
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public void publicar(Ata ata) throws Exception{
		this.publicar(ata.getIdAta());
	}
	
	public void publicar(int idAta) throws Exception{
		try{
			AtaDAO dao = new AtaDAO();
			byte[] pdf = this.gerarAta(idAta);
			
			dao.publicar(idAta, pdf);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean excluir(int idAta, int idUsuario) throws Exception{
		try{
			if(!this.isPresidente(idUsuario, idAta)){
				throw new Exception("Apenas o presidente da reunião pode excluir a ata.");
			}
			if(this.isPublicada(idAta)){
				throw new Exception("A ata já foi publicada e não pode ser excluída.");
			}
			
			AtaDAO dao = new AtaDAO();
			
			return dao.excluir(idAta);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	private String getDataExtenso(Date data){
		int dia = DateUtils.getDayOfMonth(data);
		int mes = DateUtils.getMonth(data);
		int ano = DateUtils.getYear(data);
		int hora = DateUtils.getHour(data);
		int minuto = DateUtils.getMinute(data);
		String resultado = "Ao";
		String[] meses = {"janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};
		
		if(dia > 1){
			resultado += "s ";
		}else{
			resultado += " ";	
		}
		
		//Data
		resultado += (dia == 1 ? "primeiro" : StringUtils.getExtenso(dia)) + " dia" + (dia > 1 ? "s" : "") + " do mês de " + meses[mes] + " de " + StringUtils.getExtenso(ano) + ", ";
		//Hora
		resultado += "à" + (hora > 1 ? "s" : "") + " " + StringUtils.getExtenso(hora) + " hora" + (hora > 1 ? "s" : "");
		if(minuto > 0){
			resultado += " e " + StringUtils.getExtenso(minuto) + " minuto" + (minuto > 1 ? "s" : ""); 
		}
		
		return resultado;
	}

}
