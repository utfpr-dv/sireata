package br.edu.utfpr.dv.sireata.model;

public class ParticipanteReport {
	
	public int ordem;
	public String nome;
	public boolean presente;
	public String motivo;
	
	public ParticipanteReport(){
		this.setOrdem(0);
		this.setNome("");
		this.setPresente(true);
		this.setMotivo("");
	}
	
	public int getOrdem() {
		return ordem;
	}
	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public boolean isPresente() {
		return presente;
	}
	public void setPresente(boolean presente) {
		this.presente = presente;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}
