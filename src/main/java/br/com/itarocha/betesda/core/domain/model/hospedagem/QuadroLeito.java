package br.com.itarocha.betesda.core.domain.model.hospedagem;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuadroLeito {
	public Long id;
	public Integer numero;
	private Integer[] dias;
	private LocalDate[] datas;

	public QuadroLeito(Long id, Integer numero, int numeroDias) {
		this.id = id;
		this.numero = numero;

		this.dias = new Integer[numeroDias];
		for (int i = 0; i < numeroDias; i++) {
			this.dias[i] = 0;
		}
	}
}