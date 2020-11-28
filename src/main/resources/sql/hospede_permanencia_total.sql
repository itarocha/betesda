SELECT      new br.com.itarocha.betesda.report.HospedePermanencia( hospede.pessoa.id
			, hospedagem.id
			, hospedagem.encaminhador.id
			, hospedeLeito.dataEntrada
			, hospedeLeito.dataSaida
			, 'T'
			, hospedagem
			, hospede
			, hospede.pessoa)
FROM        HospedeLeito hospedeLeito
INNER JOIN  hospedeLeito.leito leito
INNER JOIN  hospedeLeito.hospede hospede
INNER JOIN  hospede.hospedagem hospedagem
WHERE       ((hospedeLeito.dataEntrada BETWEEN :DATA_INI AND :DATA_FIM) OR (hospedeLeito.dataSaida BETWEEN :DATA_INI AND :DATA_FIM))
OR          ((hospedeLeito.dataEntrada <= :DATA_INI) AND (hospedeLeito.dataSaida >= :DATA_FIM))
ORDER BY    hospede.pessoa.id
		  , hospedagem.id
		  , hospedeLeito.dataEntrada
