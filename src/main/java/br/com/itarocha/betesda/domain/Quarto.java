package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quarto {

	private Long id;
	
	@NotNull(message="Número precisa ser informado")
	private Integer numero;
	
	@NotNull(message="Descrição é obrigatória")
	@Size(max = 255, message="Descrição não pode ter mais que 255 caracteres")
	private String descricao;

    private Set<DestinacaoHospedagem> destinacoes = new HashSet<>();
	
	private List<Leito> leitos;

	@Builder.Default
	//@Enumerated(EnumType.STRING)
	private LogicoEnum ativo = LogicoEnum.S;;

	//@Transient
	private String displayText;
}
