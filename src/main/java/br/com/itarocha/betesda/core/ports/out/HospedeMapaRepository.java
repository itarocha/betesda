package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.domain.hospedagem.HospedeMapa;

import java.time.LocalDate;
import java.util.List;

public interface HospedeMapaRepository {
    List<HospedeMapa> buildListaHospedeMapa(LocalDate dataIni, LocalDate dataFim);
}
