package br.com.itarocha.betesda.persistencia.model;

import br.com.itarocha.betesda.persistencia.model.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name="hospedagem_tipo_servico")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
