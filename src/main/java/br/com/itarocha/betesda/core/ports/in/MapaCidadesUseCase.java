package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.domain.hospedagem.MapaCidades;

import java.time.LocalDate;

public interface MapaCidadesUseCase {
    MapaCidades buildMapaCidades(LocalDate dataBase);
}
