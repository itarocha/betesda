SELECT hospedagem 
FROM   HospedagemEntity hospedagem
WHERE  EXISTS(
				SELECT      h
				FROM        hospedagemEntity h
				INNER JOIN  hospede hpd
				ON          hpd.hospedagem_id = h.id
				INNER JOIN  hospede_leito hl
				WHERE       h.id = hospedagem.id
				AND         hl.id = :id
)
