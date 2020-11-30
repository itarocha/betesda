package br.com.itarocha.betesda.repository;

import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface DestinacaoHospedagemRepository extends JpaRepository<DestinacaoHospedagem, Long> {

}
