Select<Record> s1 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(1));
Select<Record> s2 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(2));
Select<Record> s3 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(3));
Select<Record> s4 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(4));

Result<Record> result = create().select().from(s1.union(s2).union(s3).union(s4)).fetch();
assertEquals(4, result.getNumberOfRecords());

https://www.jooq.org/doc/latest/manual/sql-building/conditional-expressions/boolean-operators/

and(Condition)            // Combine conditions with AND
and(String)               // Combine conditions with AND. Convenience for adding plain SQL to the right-hand side
and(String, Object...)    // Combine conditions with AND. Convenience for adding plain SQL to the right-hand side
and(String, QueryPart...) // Combine conditions with AND. Convenience for adding plain SQL to the right-hand side
andExists(Select<?>)      // Combine conditions with AND. Convenience for adding an exists predicate to the rhs
andNot(Condition)         // Combine conditions with AND. Convenience for adding an inverted condition to the rhs
andNotExists(Select<?>)   // Combine conditions with AND. Convenience for adding an inverted exists predicate to the rhs

or(Condition)             // Combine conditions with OR
or(String)                // Combine conditions with OR. Convenience for adding plain SQL to the right-hand side
or(String, Object...)     // Combine conditions with OR. Convenience for adding plain SQL to the right-hand side
or(String, QueryPart...)  // Combine conditions with OR. Convenience for adding plain SQL to the right-hand side
orExists(Select<?>)       // Combine conditions with OR. Convenience for adding an exists predicate to the rhs
orNot(Condition)          // Combine conditions with OR. Convenience for adding an inverted condition to the rhs
orNotExists(Select<?>)    // Combine conditions with OR. Convenience for adding an inverted exists predicate to the rhs

not()                     // Invert a condition (synonym for DSL.not(Condition)

(TITLE = 'Animal Farm' OR TITLE = '1984')
  AND NOT (AUTHOR.LAST_NAME = 'Orwell')

BOOK.TITLE.eq("Animal Farm").or(BOOK.TITLE.eq("1984"))
  .andNot(AUTHOR.LAST_NAME.eq("Orwell"))

Condition a = BOOK.TITLE.eq("Animal Farm");
Condition b = BOOK.TITLE.eq("1984");
Condition c = AUTHOR.LAST_NAME.eq("Orwell");

Condition combined1 = a.or(b);             // These OR-connected conditions form a new condition, wrapped in parentheses
Condition combined2 = combined1.andNot(c); // The left-hand side of the AND NOT () operator is already wrapped in parentheses
--------------------------------------

SELECT

  -- Searched case
  CASE WHEN AUTHOR.FIRST_NAME = 'Paulo'  THEN 'brazilian'
       WHEN AUTHOR.FIRST_NAME = 'George' THEN 'english'
                                         ELSE 'unknown'
  END,

  -- Simple case
  CASE AUTHOR.FIRST_NAME WHEN 'Paulo'  THEN 'brazilian'
                         WHEN 'George' THEN 'english'
                                       ELSE 'unknown'
  END
FROM AUTHOR


create.select(

  // Searched case
  when(AUTHOR.FIRST_NAME.eq("Paulo"), "brazilian")
  .when(AUTHOR.FIRST_NAME.eq("George"), "english")
  .otherwise("unknown");

  // Simple case
  choose(AUTHOR.FIRST_NAME)
  .when("Paulo", "brazilian")
  .when("George", "english")
  .otherwise("unknown"))
.from(AUTHOR)
.fetch();

// COALESCE
create.select(coalesce(null, null, 1)).fetch();


// BETWEEN
BOOK.PUBLISHED_IN.between(1920).and(1940)
BOOK.PUBLISHED_IN.notBetween(1920).and(1940)

======================================================================

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