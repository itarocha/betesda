package br.com.itarocha.betesda.repository;

import br.com.itarocha.betesda.model.Leito;
import br.com.itarocha.betesda.model.Quarto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface QuartoRepository extends JpaRepository<Quarto, Long> {
	
	 @Query("SELECT o FROM Leito o WHERE (o.quarto.id = :quartoId) AND (o.id <> :id) AND (o.numero = :numero)")
	 Collection<Leito> existeOutroLeitoComEsseNumero(@Param("id") Long leito_id, @Param("quartoId") Long quartoId, @Param("numero") Integer numero);
	 
	 @Query("SELECT o FROM Leito o WHERE (o.quarto.id = :quartoId) AND (o.numero = :numero)")
	 Collection<Leito> existeOutroLeitoComEsseNumero(@Param("quartoId") Long quartoId, @Param("numero") Integer numero);

	 //@Query("SELECT q FROM Quarto q FETCH ALL PROPERTIES ORDER BY q.numero")
	 @Query("SELECT q FROM Quarto q ORDER BY q.numero")
	 List<Quarto> findAllOrderByQuartoNumero();

	 @Query("SELECT o FROM Quarto o WHERE (o.id <> :id) AND (o.numero = :numero)") 
	 List<Quarto> existeOutroQuartoComEsseNumero(@Param("id") Long id, @Param("numero") Integer numero);

	 @Query("SELECT o FROM Quarto o WHERE (o.numero = :numero)") 
	 List<Quarto> existeOutroQuartoComEsseNumero(@Param("numero") Integer numero);

	 @Query("SELECT q FROM Quarto q JOIN q.destinacoes d FETCH ALL PROPERTIES WHERE d.id = :id ORDER BY q.numero") // JOIN FETCH
	 List<Quarto> findByDestinacaoHospedagemId(@Param("id") Long id);
}
