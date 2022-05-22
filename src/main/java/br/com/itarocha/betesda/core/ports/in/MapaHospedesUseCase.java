package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.domain.hospedagem.MapaHospedes;

import java.time.LocalDate;

public interface MapaHospedesUseCase {
    MapaHospedes buildMapaHospedes(LocalDate dataBase);
}
