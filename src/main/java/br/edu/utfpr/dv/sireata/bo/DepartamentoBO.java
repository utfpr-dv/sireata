package br.edu.utfpr.dv.sireata.bo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.edu.utfpr.dv.sireata.dao.DepartamentoDAO;
import br.edu.utfpr.dv.sireata.model.Departamento;

public class DepartamentoBO {
	
	public Departamento buscarPorId(int id) throws Exception{
		try{
			DepartamentoDAO dao = new DepartamentoDAO();
			
			return dao.buscarPorId(id);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public Departamento buscarPorOrgao(int idOrgao) throws Exception{
		try{
			DepartamentoDAO dao = new DepartamentoDAO();
			
			return dao.buscarPorOrgao(idOrgao);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Departamento> listarTodos(boolean apenasAtivos) throws Exception{
		try{
			DepartamentoDAO dao = new DepartamentoDAO();
			
			return dao.listarTodos(apenasAtivos);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Departamento> listarPorCampus(int idCampus, boolean apenasAtivos) throws Exception{
		try{
			DepartamentoDAO dao = new DepartamentoDAO();
			
			return dao.listarPorCampus(idCampus, apenasAtivos);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Departamento> listarParaCriacaoAta(int idCampus, int idUsuario) throws Exception{
		try{
			DepartamentoDAO dao = new DepartamentoDAO();
			
			return dao.listarParaCriacaoAta(idCampus, idUsuario);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Departamento> listarParaConsultaAtas(int idCampus, int idUsuario) throws Exception{
		try{
			DepartamentoDAO dao = new DepartamentoDAO();
			
			return dao.listarParaConsultaAtas(idCampus, idUsuario);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public int salvar(Departamento departamento) throws Exception{
		if((departamento.getCampus() == null) || (departamento.getCampus().getIdCampus() == 0)){
			throw new Exception("Informe o câmpus do departamento.");
		}
		if(departamento.getNome().isEmpty()){
			throw new Exception("Informe o nome do departamento.");
		}
		
		try{
			DepartamentoDAO dao = new DepartamentoDAO();
			
			return dao.salvar(departamento);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}

}
