package br.com.itarocha.betesda.adapter.out.persistence.entity;

import br.com.itarocha.betesda.domain.model.TipoUtilizacaoHospedagem;
import br.com.itarocha.betesda.adapter.out.persistence.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="hospedagem")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hospedes"})
public class HospedagemEntity extends UserDateAudit implements Serializable {

	private static final long serialVersionUID = 1841335162635443594L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="entidade_id")
	//////////////////@NotNull(message="Entidade é obrigatório") // por enquanto não
	private EntidadeEntity entidade;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="encaminhador_id")
	//////////////////@NotNull(message="Encaminhador é obrigatório") // por enquanto não
	private EncaminhadorEntity encaminhador;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="destinacao_hospedagem_id")
	@NotNull(message="Destinação da Hospedagem é obrigatória")
	private DestinacaoHospedagemEntity destinacaoHospedagem;
			
	@NotNull(message="Data de Entrada precisa ser informado")
	@Column(name = "data_entrada")
	private LocalDate dataEntrada;
	
	@NotNull(message="Data Prevista de Saída precisa ser informada")
	@Column(name = "data_prevista_saida")
	private LocalDate dataPrevistaSaida;
	
	@Column(name = "data_efetiva_saida")
	private LocalDate dataEfetivaSaida;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_utilizacao", length=1)
	@NotNull(message="Tipo de Utilização precisa ser informada")
	private TipoUtilizacaoHospedagem tipoUtilizacao = TipoUtilizacaoHospedagem.T;
	
	@Lob 
	@Basic(fetch=FetchType.LAZY)
	private String observacoes;

	@Builder.Default
	@OneToMany(mappedBy = "hospedagem",fetch=FetchType.LAZY)
	private List<HospedeEntity> hospedes = new ArrayList<HospedeEntity>();

	@Builder.Default
	@OneToMany(mappedBy = "hospedagem",fetch=FetchType.LAZY)
	private List<HospedagemTipoServicoEntity> servicos = new ArrayList<HospedagemTipoServicoEntity>();
}
