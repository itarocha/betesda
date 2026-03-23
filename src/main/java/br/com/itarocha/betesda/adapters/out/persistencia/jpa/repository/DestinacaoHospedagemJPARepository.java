package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.DestinacaoHospedagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DestinacaoHospedagemJPARepository extends JpaRepository<DestinacaoHospedagemEntity, Long> {

    @Query("SELECT model FROM DestinacaoHospedagemEntity model ORDER BY model.descricao")
    List<DestinacaoHospedagemEntity> findAll();

}
