package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.domain.hospedagem.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class MapaCidadesService {

    private final MapaHospedesService mapaHospedesService;

    //MAPA CIDADES
    public MapaCidades buildMapaCidades(LocalDate dataBase) {
        MapaHospedes mapaHospedes = mapaHospedesService.buildMapaHospedes(dataBase);
        MapaCidades retorno = new MapaCidades();
        retorno.setDataIni(mapaHospedes.getDataIni());
        retorno.setDataFim(mapaHospedes.getDataFim());
        retorno.setDias(mapaHospedes.getDias());

        Map<String, List<HospedeMapa>> porCidade = new TreeMap<>();
        mapaHospedes.getHospedes().forEach(h -> {
            if (porCidade.get(h.getPessoaCidadeUfOrigem()) == null ) {
                List<HospedeMapa> lst = new ArrayList<>();
                lst.add(h);
                porCidade.put(h.getPessoaCidadeUfOrigem(), lst);
            } else {
                List<HospedeMapa> lst = porCidade.get(h.getPessoaCidadeUfOrigem());
                lst.add(h);
            }
        });

        List<HospedagensPorCidade> cidades = new ArrayList<>();
        porCidade.keySet().forEach(k -> {
            cidades.add(new HospedagensPorCidade(k, porCidade.get(k)));
        });
        retorno.setCidades(cidades);

        return retorno;
    }


}
