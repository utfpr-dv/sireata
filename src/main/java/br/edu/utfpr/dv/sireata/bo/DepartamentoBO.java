package br.edu.utfpr.dv.sireata.bo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.edu.utfpr.dv.sireata.dao.DAO;
import br.edu.utfpr.dv.sireata.dao.DepartamentoDAO;
import br.edu.utfpr.dv.sireata.model.Departamento;

public class DepartamentoBO extends BOFactory{
	
	public Departamento buscarPorId(int id) throws Exception{
		try{
			DepartamentoDAO dao = (DepartamentoDAO) createDAO();
			
			return dao.buscarPorId(id);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public Departamento buscarPorOrgao(int idOrgao) throws Exception{
		try{
			DepartamentoDAO dao = (DepartamentoDAO) createDAO();
			
			return dao.buscarPorOrgao(idOrgao);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Departamento> listarTodos(boolean apenasAtivos) throws Exception{
		try{
			DepartamentoDAO dao = (DepartamentoDAO) createDAO();
			
			return dao.listarTodos(apenasAtivos);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Departamento> listarPorCampus(int idCampus, boolean apenasAtivos) throws Exception{
		try{
			DepartamentoDAO dao = (DepartamentoDAO) createDAO();
			
			return dao.listarPorCampus(idCampus, apenasAtivos);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Departamento> listarParaCriacaoAta(int idCampus, int idUsuario) throws Exception{
		try{
			DepartamentoDAO dao = (DepartamentoDAO) createDAO();
			
			return dao.listarParaCriacaoAta(idCampus, idUsuario);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Departamento> listarParaConsultaAtas(int idCampus, int idUsuario) throws Exception{
		try{
			DepartamentoDAO dao = (DepartamentoDAO) createDAO();
			
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
			DepartamentoDAO dao = (DepartamentoDAO) createDAO();
			
			return dao.salvar(departamento);
		}catch(Exception e){
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public DAO<Departamento> createDAO() {
		return new DepartamentoDAO();
	}
}
