package br.edu.utfpr.dv.sireata.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.edu.utfpr.dv.sireata.bo.AtaBO;
import br.edu.utfpr.dv.sireata.model.Ata;
import br.edu.utfpr.dv.sireata.model.Ata.TipoAta;
import br.edu.utfpr.dv.sireata.util.DateUtils;

@Path("/ata")
public class AtaService {
	
	@GET
	@Path("/listar/{orgao}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listar(@PathParam("orgao") int idOrgao) {
		try {
			List<Ata> list = new AtaBO().listarPorOrgao(idOrgao);
			List<AtaJson> ret = new ArrayList<AtaJson>();
			
			for(Ata a : list) {
				AtaJson ata = new AtaJson();
				
				ata.setTipo(a.getTipo());
				ata.setNumero(a.getNumero());
				ata.setAno(DateUtils.getYear(a.getData()));
				ata.setData(DateUtils.format(a.getData(), "dd/MM/yyyy"));
				
				ret.add(ata);
			}
			
			return Response.ok(ret).build();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);

			return Response.status(Status.INTERNAL_SERVER_ERROR.ordinal(), e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/visualizar/{orgao}/{tipo}/{numero}/{ano}")
	@Produces("application/pdf")
	public Response baixar(@PathParam("orgao") int idOrgao, @PathParam("tipo") int tipo, @PathParam("numero") int numero, @PathParam("ano") int ano) {
		try {
			Ata ata = new AtaBO().buscarPorNumero(idOrgao, TipoAta.valueOf(tipo), numero, ano);
			
			if(ata != null) {
				return Response.ok().type("application/pdf").entity(ata.getDocumento()).build();
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);

			return Response.status(Status.INTERNAL_SERVER_ERROR.ordinal(), e.getMessage()).build();
		}
	}
	
	public class AtaJson {
		
		private TipoAta tipo;
		private int numero;
		private int ano;
		private String data;
		
		public AtaJson() {
			this.setTipo(TipoAta.ORDINARIA);
			this.setNumero(0);
			this.setAno(0);
			this.setData("");
		}
		
		public TipoAta getTipo() {
			return tipo;
		}
		public void setTipo(TipoAta tipo) {
			this.tipo = tipo;
		}
		public int getNumero() {
			return numero;
		}
		public void setNumero(int numero) {
			this.numero = numero;
		}
		public int getAno() {
			return ano;
		}
		public void setAno(int ano) {
			this.ano = ano;
		}
		public String getData() {
			return data;
		}
		public void setData(String data) {
			this.data = data;
		}
		
	}

}
