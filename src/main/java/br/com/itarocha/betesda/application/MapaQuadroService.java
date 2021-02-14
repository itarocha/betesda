package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.application.port.in.QuartoUseCase;
import br.com.itarocha.betesda.domain.Quarto;
import br.com.itarocha.betesda.domain.hospedagem.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class MapaQuadroService {

    private final MapaHospedagemService mapaHospedagemService;
    private final MapaHospedesService mapaHospedesService;
    private final QuartoUseCase quartoService;

    private static final int QTD_DIAS = 7;

    public MapaQuadro buildMapaQuadro(LocalDate dataBase) {

        AtomicInteger atomicMinLeito = new AtomicInteger(Integer.MAX_VALUE);
        AtomicInteger atomicMaxLeito = new AtomicInteger(Integer.MIN_VALUE);

        Quadro quadro = new Quadro();

        // Busca quartos e leitos do banco
        List<Quarto> listQuartoEntities = quartoService.findAll();
        // Cria os quartos e verifica minimo e maximo dos leitos
        listQuartoEntities.forEach(q -> {
            quadro.quartos.add(new QuadroQuarto( q.getId(), q.getNumero()));

            q.getLeitos().forEach(l -> {
                if (l.getNumero() < atomicMinLeito.get()) {
                    atomicMinLeito.set(l.getNumero());
                } else if (l.getNumero() > atomicMaxLeito.get()) {
                    atomicMaxLeito.set(l.getNumero());
                }
            });
        });

        // Adiciona todos os leitos em todos os quartos. Todos os leitos estão com id zerado
        quadro.quartos.forEach(q -> {
            for (Integer n = atomicMinLeito.get(); n <= atomicMaxLeito.get(); n++) {
                q.leitos.add(new QuadroLeito(0L, n, QTD_DIAS));
            }
        });

        /*
        // Varre mais uma vez os quartos do banco e seta os ids correspondentes. Os ids zerados significa que não existem no banco
        listQuartoEntities.forEach(q -> {
            q.getLeitos().forEach(leito -> {
                quadro.setLeitoIdPorNumero(q.getId(), leito.getNumero(), leito.getId());
            });
        });



        MapaQuadro retorno = MapaQuadro.builder()
//                .dataIni(mapaHospedes.getDataIni())
//                .dataFim(mapaHospedes.getDataFim())
//                .dias(mapaHospedes.getDias())
//                .quadro(quadro)
                .build();




        quadro.quartos.forEach(q -> {
            q.leitos.forEach(leito -> {
                if (leito.id == 0) {
                    //System.out.print("[  ] ");
                } else {
                    // Busca o array de linhas de hospedagens.
                    // Cada quarto contém um array
                    Long leitoId = leito.id;

                    retorno.getLinhas()
                            .stream()
                            .filter(_leito -> _leito.getLeitoId().equals( leitoId )) // theLeito.getId().equals
                            .findFirst()
                            .ifPresent(theLeito -> {

                                theLeito.getHospedagens().forEach(linhaHpd -> {
                                    // Quando um índice maior que zero, significa que há hospedagem nesse dia da semana
                                    for (int i = 0; i < QTD_DIAS; i++) {
                                        boolean marcar = linhaHpd.getDias()[i] > 0;
                                        if ( marcar ) {
                                            leito.getDias()[i] = 1;
                                        }
                                    }
                                });
                            });
                }

            });
        });


        */
        MapaHospedes mapaHospedes = mapaHospedesService.buildMapaHospedes(dataBase);
        return MapaQuadro.builder()
                .dataIni(mapaHospedes.getDataIni())
                .dataFim(mapaHospedes.getDataFim())
                .dias(mapaHospedes.getDias())
                .quadro(quadro)
                .build();
    }

}
