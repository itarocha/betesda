SELECT     l.id leito_id
         , l.numero leito_numero
         , q.id quarto_id
         , q.numero quarto_numero
FROM       leito l
INNER JOIN quarto q
ON         q.id = l.quarto_id
ORDER BY   q.numero
         , l.numero