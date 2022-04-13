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
			
			String dataExtenso = this.getDataExtenso(ata.getData());
			String local = StringEscapeUtils.escapeHtml4(ata.getLocalCompleto());
			String ordinalExtenso = StringUtils.getExtensoOrdinal(ata.getNumero(), true);
			String reuniaoTipo = (ata.getTipo() == TipoAta.ORDINARIA ? "ordinária" : "extraordinária");
			String ano = String.valueOf(DateUtils.getYear(ata.getData()));
			String orgaoNomeCompleto = StringEscapeUtils.escapeHtml4(orgao.getNomeCompleto());
			String ataSecretarioNome = StringEscapeUtils.escapeHtml4(ata.getSecretario().getNome());
			String designacao;
			String designadoNome = StringEscapeUtils.escapeHtml4(ata.getPresidente().getNome());
			String boldOpen = "<b>";
			String boldClose = "</b>";

			report.setNumero(df.format(ata.getNumero()) + "/" + String.valueOf(DateUtils.getYear(ata.getData())));
			report.setDataHora(DateUtils.format(ata.getData(), "dd/MM/yyyy") + " às " + DateUtils.format(ata.getData(), "HH") + " horas" + (DateUtils.getMinute(ata.getData()) > 0 ? " e " + DateUtils.format(ata.getData(), "mm") + " minutos" : "") + ".");
			report.setLocal(ata.getLocal());
			report.setPresidente(ata.getPresidente().getNome());
			report.setSecretario(ata.getSecretario().getNome());

			if (ata.getPresidente().getIdUsuario() == orgao.getPresidente().getIdUsuario()) {
				designacao = StringEscapeUtils.escapeHtml4(orgao.getDesignacaoPresidente()) + " ";
			}else {
				designacao = "professor(a) ";
			}

			texto = dataExtenso + ", no " + local + " realizou-se a " + ordinalExtenso +
					" reunião " + reuniaoTipo + " de " + ano + " do(a) " + orgaoNomeCompleto +
					", a qual foi conduzida pelo(a) " + designacao + designadoNome +
					" e teve como pauta: " + boldOpen;

			ata.setPauta(pbo.listarPorAta(idAta));

			for(int i = 1; i <= ata.getPauta().size(); i++) {
				String identificador = "(" + i + ") ";
				String pautaTitulo = StringEscapeUtils.escapeHtml4(ata.getPauta().get(i - 1).getTitulo());
				String pontoOuPontoEVirgula = (i == ata.getPauta().size() ? "." : "; ");

				texto += identificador + pautaTitulo + pontoOuPontoEVirgula;
			}

			texto += boldClose + " " + ata.getConsideracoesIniciais() + " ";

			for(int i = 1; i <= ata.getPauta().size(); i++){

					String identificador = "(" + i + ") ";
				String pautaTitulo = StringEscapeUtils.escapeHtml4(ata.getPauta().get(i - 1).getTitulo());
				String pautaDescricao = StringEscapeUtils.escapeHtml4(ata.getPauta().get(i - 1).getDescricao());

				texto += boldOpen + identificador + " " + pautaTitulo + boldClose + ", " + pautaDescricao + " ";
			}
			texto += boldClose + " " + ata.getConsideracoesIniciais() + " ";

			for(int i = 1; i <= ata.getPauta().size(); i++){

				String identificador = "(" + i + ") ";
				String pautaTitulo = StringEscapeUtils.escapeHtml4(ata.getPauta().get(i - 1).getTitulo());
				String pautaDescricao = StringEscapeUtils.escapeHtml4(ata.getPauta().get(i - 1).getDescricao());

				texto += boldOpen + identificador + " " + pautaTitulo + boldClose + ", " + pautaDescricao + " ";
			}


			texto += " Nada mais havendo a tratar, deu-se por encerrada a reunião, da qual eu, " + ataSecretarioNome +
					", lavrei a presente ata que, após aprovada, vai assinada por mim e pelos demais presentes.";



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
		Locale BRAZIL = new Locale("pt", "BR");
		int dia = DateUtils.getDayOfMonth(data);
		int mes = DateUtils.getMonth(data);
		int ano = DateUtils.getYear(data);
		int hora = DateUtils.getHour(data);
		int minuto = DateUtils.getMinute(data);
		String mesExtenso = new SimpleDateFormat("MMMM", BRAZIL).format(data);
		String anoExtenso = StringUtils.getExtenso(DateUtils.getYear(data));
		String horaExtenso = StringUtils.getExtenso(hora);
		String minutoExtenso = StringUtils.getExtenso(minuto);
		String resultado = "";
		String aos = StringUtils.tryPlural("Ao", dia);
		String as = StringUtils.tryPlural("à", hora);
		String horas = StringUtils.tryPlural("hora", hora);
		String diaExtenso = dia == 1 ? "primeiro" : StringUtils.getExtenso(dia);
		String dias = StringUtils.tryPlural("dia", dia);
		String minutos = StringUtils.tryPlural("minuto", minuto);
		
		resultado += aos + " " + diaExtenso + " " + dias + " do mês de " + mesExtenso + " de " + anoExtenso + ", ";

		resultado += as + " " + horaExtenso + " " + horas;
	
		resultado += minuto > 0 ? " e " + minutoExtenso + " " + minutos : "";
		return resultado;
	}

}
