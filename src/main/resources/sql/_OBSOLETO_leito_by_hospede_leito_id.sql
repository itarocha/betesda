SELECT      new br.com.itarocha.betesda.domain.LeitoDTO(l.id, l.numero, q.id, q.numero)
FROM        HospedeLeitoEntity hospedeLeito
INNER JOIN  hospedeLeito.leito l
INNER JOIN  l.quarto q
WHERE       hospedeLeito.id = :id