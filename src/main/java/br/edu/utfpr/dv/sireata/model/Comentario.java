package br.edu.utfpr.dv.sireata.model;

public class Comentario {
	
	public enum SituacaoComentario{
		NAOANALISADO(0), ACEITO(1), RECUSADO(2);
		
		private final int value; 
		SituacaoComentario(int value){ 
			this.value = value; 
		}
		
		public int getValue(){ 
			return value;
		}
		
		public static SituacaoComentario valueOf(int value){
			for(SituacaoComentario u : SituacaoComentario.values()){
				if(u.getValue() == value){
					return u;
				}
			}
			
			return null;
		}
		
		public String toString(){
			switch(this){
				case NAOANALISADO:
					return "Não Analisado";
				case ACEITO:
					return "Aceito";
				case RECUSADO:
					return "Recusado";
				default:
					return "";
			}
		}
	}
	
	private int idComentario;
	private Pauta pauta;
	private Usuario usuario;
	private SituacaoComentario situacao;
	private String comentarios;
	private SituacaoComentario situacaoComentarios;
	private String motivo;
	
	public Comentario(){
		this.setIdComentario(0);
		this.setPauta(new Pauta());
		this.setUsuario(new Usuario());
		this.setSituacao(SituacaoComentario.NAOANALISADO);
		this.setSituacaoComentarios(SituacaoComentario.NAOANALISADO);
		this.setComentarios("");
		this.setMotivo("");
	}
	
	public int getIdComentario() {
		return idComentario;
	}
	public void setIdComentario(int idComentario) {
		this.idComentario = idComentario;
	}
	public Pauta getPauta() {
		return pauta;
	}
	public void setPauta(Pauta pauta) {
		this.pauta = pauta;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public SituacaoComentario getSituacao() {
		return situacao;
	}
	public void setSituacao(SituacaoComentario situacao) {
		this.situacao = situacao;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	public SituacaoComentario getSituacaoComentarios() {
		return situacaoComentarios;
	}
	public void setSituacaoComentarios(SituacaoComentario situacaoComentarios) {
		this.situacaoComentarios = situacaoComentarios;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}
