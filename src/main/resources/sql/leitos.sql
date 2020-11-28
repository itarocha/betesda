SELECT      new br.com.itarocha.betesda.model.LeitoVO(l.id, l.numero, q.id, q.numero, l.tipoLeito) 
FROM        Leito l
INNER JOIN  l.quarto q
ORDER BY    q.numero, l.numero

