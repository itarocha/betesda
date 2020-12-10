package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.DestinacaoHospedagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DestinacaoHospedagemJpaRepository extends JpaRepository<DestinacaoHospedagemEntity, Long> {

    @Query("SELECT o FROM DestinacaoHospedagemEntity o ORDER BY o.descricao")
    List<DestinacaoHospedagemEntity> findAllOrderByDescricao();

}
