package br.com.itarocha.betesda.adapter.out.persistence.repository;

import br.com.itarocha.betesda.adapter.out.persistence.entity.DestinacaoHospedagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DestinacaoHospedagemRepository extends JpaRepository<DestinacaoHospedagemEntity, Long> {

    @Query("SELECT o FROM DestinacaoHospedagemEntity o ORDER BY o.descricao")
    List<DestinacaoHospedagemEntity> findAllOrderByDescricao();

}
