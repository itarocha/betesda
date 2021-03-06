SELECT  tb.identificador
      , hospedagem.tipo_utilizacao  
      , tb.quarto_id
      , tb.quarto_numero
      , tb.leito_id
      , tb.leito_numero
      , p.id pessoa_id
      , p.nome AS pessoa_nome
      , p.telefone AS pessoa_telefone
      , e.cidade
      , e.uf
      , hospedagem.data_entrada AS data_entrada_hospedagem
      , COALESCE(hospedagem.data_efetiva_saida, hospedagem.data_prevista_saida) AS data_saida_hospedagem 
      , tb.data_primeira_entrada
      , tb.data_ultima_entrada
      , tb.data_entrada_leito
      , tb.data_saida_leito
      , tb.data_ini AS data_ini_no_periodo
      , tb.data_fim AS data_fim_no_periodo
      , hospede.hospedagem_id
      , tb.hospede_id
      , hospede.tipo_hospede_id
      , hospede.baixado
      , th.descricao tipo_hospede_descricao
      , hospedagem.destinacao_hospedagem_id
      , dh.descricao destinacao_hospedagem_descricao
      , hospedagem.data_prevista_saida
      , hospedagem.data_efetiva_saida
FROM    (
			SELECT  hl.hospede_id
                  , hl.id AS identificador					
			      , quarto.id AS quarto_id
			      , quarto.numero AS quarto_numero
			      , leito.id AS leito_id
			      , leito.numero AS leito_numero
			      , 0 AS tipo
			      , hl.data_entrada AS data_entrada_leito
			      , hl.data_saida AS data_saida_leito
			      , CASE WHEN hl.data_entrada < :DATA_INI THEN :DATA_INI ELSE hl.data_entrada END AS data_ini
			      , CASE WHEN hl.data_saida > :DATA_FIM THEN :DATA_FIM ELSE hl.data_saida END AS data_fim
			      , 'T' tipo_utilizacao
			      , (SELECT MIN(hlx.data_entrada) FROM hospede_leito hlx WHERE hlx.hospede_id = hl.hospede_id) AS data_primeira_entrada
			      , (SELECT MAX(hlx.data_entrada) FROM hospede_leito hlx WHERE hlx.hospede_id = hl.hospede_id) AS data_ultima_entrada
			FROM  hospede_leito hl
			INNER JOIN leito leito
			ON         leito.id = hl.leito_id
			INNER JOIN quarto quarto
			ON         quarto.id = leito.quarto_id
			WHERE ((hl.data_entrada BETWEEN :DATA_INI and :DATA_FIM) OR (hl.data_saida BETWEEN :DATA_INI and :DATA_FIM))
			OR    ((hl.data_entrada <= :DATA_INI) and (hl.data_saida >= :DATA_FIM)) 
			UNION ALL
			SELECT  h.id AS hospede_id
			      , h.id AS identificador 
			      , 99999 AS quarto_id
			      , 0 AS quarto_numero
			      , 99999 AS leito_id
			      , 0 AS leito_numero
			      , 1 AS tipo
			      , hpd.data_entrada AS data_entrada_leito
			      , COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida) AS data_saida_leito
			      , CASE WHEN hpd.data_entrada < :DATA_INI THEN :DATA_INI ELSE hpd.data_entrada END AS data_ini
			      , CASE 
			      		WHEN COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida) > :DATA_FIM 
			      		THEN :DATA_FIM 
			      		ELSE COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida)
			      	END	AS data_fim
			      , hpd.tipo_utilizacao
			      , hpd.data_entrada AS data_primeira_entrada
			      , hpd.data_entrada AS data_ultima_entrada
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
INNER JOIN tipo_hospede th
ON         th.id = hospede.tipo_hospede_id
INNER JOIN pessoa p
ON         p.id = hospede.pessoa_id
LEFT JOIN  endereco e
ON         e.id = p.endereco_id
ORDER BY   tb.tipo 
         , tb.quarto_numero
         , tb.leito_numero
         , p.nome
         , p.id 
         , hospede.hospedagem_id