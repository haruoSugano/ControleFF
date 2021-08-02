package model.entities;

import java.util.Date;

public class Entrada {

	private Integer id;
	private String tipoEntrada;
	private Date dataEntrada;
	private Double valorBruto;
	private Double acrescimo;
	private Boolean fixo;
	
	private Usuario usuario;
	private Renda renda;
	
	public Entrada() {
	}

	public Entrada(Integer id, String tipoEntrada, Date dataEntrada, Double valorBruto, Double acrescimo, Boolean fixo,
			Usuario usuario, Renda renda) {
		this.id = id;
		this.tipoEntrada = tipoEntrada;
		this.dataEntrada = dataEntrada;
		this.valorBruto = valorBruto;
		this.acrescimo = acrescimo;
		this.fixo = fixo;
		this.usuario = usuario;
		this.renda = renda;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipoEntrada() {
		return tipoEntrada;
	}

	public void setTipoEntrada(String tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public Double getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(Double valorBruto) {
		this.valorBruto = valorBruto;
	}

	public Double getAcrescimo() {
		return acrescimo;
	}

	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}

	public Boolean getFixo() {
		return fixo;
	}

	public void setFixo(Boolean fixo) {
		this.fixo = fixo;
	}

	public Renda getRenda() {
		return renda;
	}

	public void setRenda(Renda renda) {
		this.renda = renda;
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
		Entrada other = (Entrada) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Entrada [id=" + id + ", tipoEntrada=" + tipoEntrada + ", dataEntrada=" + dataEntrada + ", valorBruto="
				+ valorBruto + ", acrescimo=" + acrescimo + ", fixo=" + fixo + ", usuario=" + usuario + "]";
	}

	
}
