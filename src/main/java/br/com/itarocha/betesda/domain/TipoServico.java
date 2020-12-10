package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoServico {
	private Long id;
	private String descricao;
	private LogicoEnum ativo = LogicoEnum.S;
}
