package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.domain.hospedagem.*;

import java.time.LocalDate;

public interface MapaQuadroUseCase {
    MapaQuadro buildMapaQuadro(LocalDate dataBase);
}
