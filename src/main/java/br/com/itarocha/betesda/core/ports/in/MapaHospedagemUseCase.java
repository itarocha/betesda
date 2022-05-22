package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.domain.hospedagem.*;

import java.time.LocalDate;
import java.util.*;

public interface MapaHospedagemUseCase {
    MapaRetorno buildMapaRetorno(LocalDate dataBase);
    List<HospedeMapa> buildListaHospedeMapa(LocalDate dataIni, LocalDate dataFim);
}