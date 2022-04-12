package br.edu.utfpr.dv.sireata.model;

public class Anexo {
	
	private int idAnexo;
	private Ata ata = new Ata();
	private int ordem;
	private String descricao;
	private byte[] arquivo;
	
	public Anexo() {
		this.setIdAnexo(0);
		this.setDescricao("");
		this.setOrdem(0);
		this.setArquivo(null);
	}
	
	public Anexo(Ata ata) {
		this();
		this.ata = ata;
	}
	
	public int getIdAnexo() {
		return idAnexo;
	}
	public void setIdAnexo(int idAnexo) {
		this.idAnexo = idAnexo;
	}
	public Ata getAta() {
		return ata;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getOrdem() {
		return ordem;
	}
	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	public byte[] getArquivo() {
		return arquivo;
	}
	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

}
