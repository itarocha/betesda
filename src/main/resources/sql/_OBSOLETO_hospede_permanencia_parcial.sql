SELECT      new br.com.itarocha.betesda.report.HospedePermanencia( hospede.pessoa.id
			, hospedagem.id
			, hospedagem.encaminhador.id
			, hospedagem.dataEntrada
			, COALESCE(hospedagem.dataEfetivaSaida,hospedagem.dataPrevistaSaida) AS dataSaida
			, 'P'
			, hospedagem
			, hospede
			, hospede.pessoa)
FROM        HospedeEntity hospede
INNER JOIN  hospede.hospedagem hospedagem
WHERE       (((hospedagem.dataEntrada BETWEEN :DATA_INI AND :DATA_FIM) OR (COALESCE(hospedagem.dataEfetivaSaida,hospedagem.dataPrevistaSaida) BETWEEN :DATA_INI AND :DATA_FIM))
OR          ((hospedagem.dataEntrada <= :DATA_INI) AND (COALESCE(hospedagem.dataEfetivaSaida,hospedagem.dataPrevistaSaida) >= :DATA_FIM)))
AND         (hospedagem.tipoUtilizacao = :TIPO_UTILIZACAO)
ORDER BY    hospede.pessoa.id
		  , hospedagem.id
		  , hospedagem.dataEntrada