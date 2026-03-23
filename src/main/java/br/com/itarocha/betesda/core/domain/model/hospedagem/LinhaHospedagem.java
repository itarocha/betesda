package br.com.itarocha.betesda.core.domain.model.hospedagem;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LinhaHospedagem {
	
	private Long hpdId;
	private String identificador;
	private String nome;
	private String telefone;
	private String status;
	private Integer idxIni;
	private Integer idxFim;
	private Integer width;
	private String clsIni;
	private String clsFim;
	private Integer[] dias;
	
}
