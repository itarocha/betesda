SELECT      new br.com.itarocha.betesda.model.LeitoVO(l.id, l.numero, q.id, q.numero, l.tipoLeito) 
FROM        HospedeLeito hospedeLeito
INNER JOIN  hospedeLeito.leito l
INNER JOIN  l.quarto q
WHERE       hospedeLeito.id = :id