package br.com.itarocha.betesda.repository;

import br.com.itarocha.betesda.model.HospedagemTipoServico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedagemServicoRepository extends JpaRepository<HospedagemTipoServico, Long> {

}
