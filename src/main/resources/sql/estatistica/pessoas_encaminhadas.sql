-- Pessoas encaminhadas
SELECT  DISTINCT hospede.hospedagem_id
      , p.id
      , p.nome
      , p.data_nascimento
      , e.cidade
      , e.uf
      , hospede.tipo_hospede_id
      , th.descricao tipo_hospede_descricao
      , hospedagem.tipo_utilizacao
      , hospedagem.destinacao_hospedagem_id
      , dh.descricao destinacao_hospedagem_descricao
      , hospedagem.entidade_id
      , entidade.nome entidade_nome
FROM    (
			SELECT  hl.hospede_id
			      , hl.leito_id
			      , CASE WHEN hl.data_entrada < :DATA_INI THEN :DATA_INI ELSE hl.data_entrada END AS data_ini
			      , CASE WHEN hl.data_saida > :DATA_FIM THEN :DATA_FIM ELSE hl.data_saida END AS data_fim
			      , 'T' tipo_utilizacao
			FROM  hospede_leito hl
			WHERE ((hl.data_entrada BETWEEN :DATA_INI and :DATA_FIM) OR (hl.data_saida BETWEEN :DATA_INI and :DATA_FIM))
			OR    ((hl.data_entrada <= :DATA_INI) and (hl.data_saida >= :DATA_FIM)) 
			UNION ALL
			SELECT  h.id AS hospede_id
			      , 0 AS leito_id
			      , CASE WHEN hpd.data_entrada < :DATA_INI THEN :DATA_INI ELSE hpd.data_entrada END AS data_ini
			      , CASE 
			      		WHEN COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida) > :DATA_FIM 
			      		THEN :DATA_FIM 
			      		ELSE COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida)
			      	END	AS data_fim
			      , hpd.tipo_utilizacao
			FROM       hospedagem hpd
			INNER JOIN hospede h
			ON         hpd.id = h.hospedagem_id
			WHERE      (((hpd.data_entrada BETWEEN :DATA_INI and :DATA_FIM) OR (COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida) BETWEEN :DATA_INI and :DATA_FIM))
			OR          ((hpd.data_entrada <= :DATA_INI) AND (COALESCE(hpd.data_efetiva_saida,hpd.data_prevista_saida) >= :DATA_FIM)))
			AND         (hpd.tipo_utilizacao = 'P')
) AS tb
INNER JOIN hospede hospede
ON         hospede.id = tb.hospede_id
INNER JOIN hospedagem hospedagem
ON         hospedagem.id = hospede.hospedagem_id
INNER JOIN destinacao_hospedagem dh
ON         dh.id = hospedagem.destinacao_hospedagem_id
INNER JOIN entidade entidade
ON         entidade.id = hospedagem.entidade_id
INNER JOIN tipo_hospede th
ON         th.id = hospede.tipo_hospede_id
INNER JOIN pessoa p
ON         p.id = hospede.pessoa_id
LEFT JOIN  endereco e
ON         e.id = p.endereco_id
ORDER BY   p.nome
         , p.id 
         , hospede.hospedagem_id