package br.edu.utfpr.dv.sireata.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.edu.utfpr.dv.sireata.bo.CampusBO;
import br.edu.utfpr.dv.sireata.model.Campus;

@Path("/campus")
public class CampusService {

	@GET
	@Path("/listar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listar() {
		try {
			List<Campus> list = new CampusBO().listarTodos(true);
			List<CampusJson> ret = new ArrayList<CampusJson>();
			
			for(Campus c : list) {
				CampusJson campus = new CampusJson();
				
				campus.setCodigo(c.getIdCampus());
				campus.setNome(c.getNome());
				
				ret.add(campus);
			}
			
			return Response.ok(ret).build();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);

			return Response.status(Status.INTERNAL_SERVER_ERROR.ordinal(), e.getMessage()).build();
		}
	}
	
	public class CampusJson {
		
		private int codigo;
		private String nome;
		
		public CampusJson() {
			this.setCodigo(0);
			this.setNome("");
		}
		
		public int getCodigo() {
			return codigo;
		}
		public void setCodigo(int codigo) {
			this.codigo = codigo;
		}
		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		
	}
	
}
