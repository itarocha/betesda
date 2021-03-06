SELECT      new br.com.itarocha.betesda.domain.LeitoVO(l.id, l.numero, q.id, q.numero, l.tipoLeito)
FROM        LeitoEntity l
INNER JOIN  l.quarto q
ORDER BY    q.numero, l.numero

