package br.com.itarocha.betesda.repository;

import br.com.itarocha.betesda.model.DestinacaoHospedagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface DestinacaoHospedagemEntityRepository extends JpaRepository<DestinacaoHospedagemEntity, Long> {

}
