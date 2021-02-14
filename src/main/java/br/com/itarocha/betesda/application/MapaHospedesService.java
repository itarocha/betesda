package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.domain.hospedagem.HospedeMapa;
import br.com.itarocha.betesda.domain.hospedagem.MapaHospedes;
import br.com.itarocha.betesda.utils.LocalDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapaHospedesService {

    private final MapaHospedagemService mapaHospedagemService;

    public static final int QTD_DIAS = 7;

    public MapaHospedes buildMapaHospedes(LocalDate dataBase) {
        MapaHospedes retorno = new MapaHospedes();

        LocalDate dIni = LocalDateUtils.primeiroDiaDaSemana(dataBase);
        LocalDate dFim = dIni.plusDays(QTD_DIAS - 1);
        LocalDate hoje = LocalDate.now();

        retorno.setDataIni(dIni);
        retorno.setDataFim(dFim);

        //1 - MAPA DE HOSPEDAGENS
        List<HospedeMapa> listaHospedeLeito = mapaHospedagemService.buildListaHospedeMapa(dIni, dFim);

        // Dias
        LocalDate dtmp = dIni;
        // Qualquer índice abaixo de indiceDataPassada é data inferior a hoje
        int[] indiceDataPassada = {-1};
        while (dtmp.compareTo(dFim) != 1) {
            if (dtmp.isBefore(hoje)) {
                indiceDataPassada[0]++;
            }
            retorno.getDias().add(dtmp);
            dtmp = dtmp.plusDays(1);
        }

        retorno.setHospedes(listaHospedeLeito);
        retorno.getHospedes().sort((a, b) -> a.getPessoaNome().compareTo(b.getPessoaNome()) );

        return retorno;
    }
}
