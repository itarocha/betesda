SELECT hospedagem 
FROM   Hospedagem hospedagem
WHERE  EXISTS(
				SELECT      h
				FROM        hospedagem h
				INNER JOIN  hospede hpd
				ON          hpd.hospedagem_id = h.id
				INNER JOIN  hospede_leito hl
				WHERE       h.id = hospedagem.id
				AND         hl.id = :id
)
