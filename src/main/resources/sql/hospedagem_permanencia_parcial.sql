SELECT      
DISTINCT    hospedagem
FROM        Hospede hospede
INNER JOIN  hospede.hospedagem hospedagem
INNER JOIN  hospede.pessoa pessoa 
WHERE       (((hospedagem.dataEntrada BETWEEN :DATA_INI AND :DATA_FIM) OR (COALESCE(hospedagem.dataEfetivaSaida,hospedagem.dataPrevistaSaida) BETWEEN :DATA_INI AND :DATA_FIM))
OR          ((hospedagem.dataEntrada <= :DATA_INI) AND (COALESCE(hospedagem.dataEfetivaSaida,hospedagem.dataPrevistaSaida) >= :DATA_FIM)))
AND         (hospedagem.tipoUtilizacao = :TIPO_UTILIZACAO)
ORDER BY    hospedagem.id
