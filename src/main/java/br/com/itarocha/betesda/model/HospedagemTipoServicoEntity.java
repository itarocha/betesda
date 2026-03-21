package br.com.itarocha.betesda.model;

import br.com.itarocha.betesda.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="hospedagem_tipo_servico")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HospedagemTipoServicoEntity extends UserDateAudit implements Serializable{
	
	private static final long serialVersionUID = 4121535384605572478L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hospedagem_id")
	@NotNull(message="Hospedagem precisa ser informado")
	private HospedagemEntity hospedagem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tipo_servico_id")
	@NotNull(message="Tipo de Serviço precisa ser informado")
	private TipoServicoEntity tipoServico;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HospedagemEntity getHospedagem() {
		return this.hospedagem;
	}

	public void setHospedagem(HospedagemEntity hospedagem) {
		this.hospedagem = hospedagem;
	}

	public TipoServicoEntity getTipoServico() {
		return this.tipoServico;
	}

	public void setTipoServico(TipoServicoEntity tipoServico) {
		this.tipoServico = tipoServico;
	}

}
