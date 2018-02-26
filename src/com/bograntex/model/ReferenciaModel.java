package com.bograntex.model;

public class ReferenciaModel {
	
	private String referencia;
	private String tamanho;
	private String grade;
	private Integer quantidade;
	
	public ReferenciaModel(String referencia, String tamanho, String grade, Integer quantidade) {
		this.referencia = referencia;
		this.tamanho = tamanho;
		this.grade = grade;
		this.setQuantidade(quantidade);
	}
	
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getTamanho() {
		return tamanho;
	}
	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

}