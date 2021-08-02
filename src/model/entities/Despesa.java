package model.entities;

import java.util.Date;

public class Despesa {
	
	private Integer id;
	private String descricao;
	private String formaPagamento;
	private Date dataEntrada;
	private Double valor;
	private Double juros;
	private Boolean fixo;
	
	private Usuario usuario;
	
	public Despesa() {
	}

	public Despesa(Integer id, String descricao, String formaPagamento, Date dataEntrada, Double valor, Double juros,
			Boolean fixo, Usuario usuario) {
		this.id = id;
		this.descricao = descricao;
		this.formaPagamento = formaPagamento;
		this.dataEntrada = dataEntrada;
		this.valor = valor;
		this.juros = juros;
		this.fixo = fixo;
		this.usuario = usuario;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getJuros() {
		return juros;
	}

	public void setJuros(Double juros) {
		this.juros = juros;
	}

	public Boolean getFixo() {
		return fixo;
	}

	public void setFixo(Boolean fixo) {
		this.fixo = fixo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Despesa other = (Despesa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Despesa [id=" + id + ", descricao=" + descricao + ", formaPagamento=" + formaPagamento
				+ ", dataEntrada=" + dataEntrada + ", valor=" + valor + ", juros=" + juros + ", fixo=" + fixo
				+ ", usuario=" + usuario + "]";
	}	

}
