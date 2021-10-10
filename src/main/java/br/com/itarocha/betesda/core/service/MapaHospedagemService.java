package br.com.itarocha.betesda.core.service;

import br.com.itarocha.betesda.core.ports.in.MapaHospedagemUseCase;
import br.com.itarocha.betesda.core.ports.out.HospedeMapaRepository;
import br.com.itarocha.betesda.core.ports.out.LeitoRepository;
import br.com.itarocha.betesda.core.ports.in.QuartoUseCase;
import br.com.itarocha.betesda.domain.Leito;
import br.com.itarocha.betesda.domain.LeitoDTO;
import br.com.itarocha.betesda.domain.Quarto;
import br.com.itarocha.betesda.domain.hospedagem.*;
import br.com.itarocha.betesda.domain.strategy.ClasseCelulaStrategy;
import br.com.itarocha.betesda.utils.LocalDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class MapaHospedagemService implements MapaHospedagemUseCase {

    private final QuartoUseCase quartoService;
    private final LeitoRepository leitoRepository;
    private final HospedeMapaRepository hospedeMapaRepository;
    private final ClasseCelulaStrategy classeCelulaStrategy;

    private static final int QTD_DIAS = 7;

    public MapaRetorno buildMapaRetorno(LocalDate dataBase) {
        MapaRetorno retorno = new MapaRetorno();

        LocalDate dIni = LocalDateUtils.primeiroDiaDaSemana(dataBase);
        LocalDate dFim = dIni.plusDays(QTD_DIAS - 1);

        retorno.setDataIni(dIni);
        retorno.setDataFim(dFim);

        //1 - MAPA DE HOSPEDAGENS
        List<HospedeMapa> listaHospedeMapa = hospedeMapaRepository.buildListaHospedeMapa(dIni, dFim);

        Map<Long, ArrayList<LinhaHospedagem>>  mapaLinhaHospedagem = new HashMap<>();

        Integer[] diasPadrao = new Integer[QTD_DIAS];
        Arrays.fill(diasPadrao, 0);

        listaHospedeMapa.stream().forEach(hm -> {
            LinhaHospedagem linhaHospedagem = new LinhaHospedagem();

            linhaHospedagem.setIdentificador(hm.getIdentificador());
            linhaHospedagem.setHpdId(hm.getHospedagemId());
            linhaHospedagem.setStatus(hm.getStatusHospedagem().toString());
            linhaHospedagem.setNome(hm.getPessoaNome());
            linhaHospedagem.setTelefone(hm.getPessoaTelefone());

            ClasseInicioEnum classeIni = classeCelulaStrategy.resolveClasseIni(hm);
            ClasseFimEnum classeFim = classeCelulaStrategy.resolveClasseFim(hm);
            linhaHospedagem.setClsIni(classeIni.getDescricao());
            linhaHospedagem.setClsFim(classeFim.getDescricao());

            int iIni = Math.toIntExact(DAYS.between(dIni, hm.getDataIniNoPeriodo() ));
            int iFim = Math.toIntExact(DAYS.between(dIni, hm.getDataFimNoPeriodo() ));
            Integer[] dias = diasPadrao.clone();
            IntStream.rangeClosed(iIni, iFim).forEach(n -> dias[n] = 1);
            linhaHospedagem.setDias(dias.clone());
            linhaHospedagem.setIdxIni(iIni);
            linhaHospedagem.setIdxFim(iFim);
            linhaHospedagem.setWidth((iFim - iIni + 1)*100);

            if (mapaLinhaHospedagem.containsKey(hm.getLeitoId())) {
                mapaLinhaHospedagem.get(hm.getLeitoId()).add(linhaHospedagem);
            } else {
                ArrayList<LinhaHospedagem> lst = new ArrayList<>();
                lst.add(linhaHospedagem);
                mapaLinhaHospedagem.put(hm.getLeitoId(), lst);
            }
        });

        retorno.setLinhas(buildListaMicroLeitos(mapaLinhaHospedagem));
        listaHospedeMapa.sort(Comparator.comparing(HospedeMapa::getPessoaNome));
        retorno.setHospedes(listaHospedeMapa);
        retorno.setCidades(buildPorCidade(retorno.getHospedes()));
        retorno.setDias(buildDias(dIni, dFim));
        retorno.setQuadro(buildQuadro(retorno.getLinhas()));
        return retorno;
    }

    @Override
    public List<HospedeMapa> buildListaHospedeMapa(LocalDate dataIni, LocalDate dataFim) {
        return hospedeMapaRepository.buildListaHospedeMapa(dataIni, dataFim);
    }

    private List<CidadeHospedagens> buildPorCidade(List<HospedeMapa> hospedes){
        Map<String, List<String>> porCidade = new TreeMap<>();
        hospedes.forEach(h -> {
            if (porCidade.get(h.getPessoaCidadeUfOrigem()) == null ) {
                List<String> lst = new ArrayList<>();
                lst.add(h.getIdentificador());
                porCidade.put(h.getPessoaCidadeUfOrigem(), lst);
            } else {
                porCidade.get(h.getPessoaCidadeUfOrigem()).add(h.getIdentificador());
            }
        });

        return porCidade.entrySet()
                .stream()
                .map(e -> new CidadeHospedagens(e.getKey(), porCidade.get(e.getKey())))
                .collect(Collectors.toList());
    }

    private Quadro buildQuadro(List<MicroLeito> linhas){
        //TRATATIVA DO QUADRO
        Quadro quadro = new Quadro();

        // Busca quartos e leitos do banco
        List<Quarto> listQuartoEntities = quartoService.findAll();

        // Cria um agrupamento de quartos <quartoNum, leitos.minimum, leitos.maximum>
        List<QuartoLeitos> listQuartosLeitos = listQuartoEntities
                .stream()
                .map(q -> {
                    Integer  minimum = q.getLeitos().stream().min(Comparator.comparing(Leito::getNumero)).get().getNumero();
                    Integer  maximum = q.getLeitos().stream().max(Comparator.comparing(Leito::getNumero)).get().getNumero();
                    QuartoLeitos ql = new QuartoLeitos(q.getId(), q.getNumero(), minimum, maximum);
                    return ql;
                }).collect(Collectors.toList());

        Integer minimum = Collections.max(listQuartosLeitos, Comparator.comparing(q -> q.getMinimum())).getMinimum();
        Integer maximum = Collections.max(listQuartosLeitos, Comparator.comparing(q -> q.getMaximum())).getMaximum();

        Integer[] arrayDiasVazio = new Integer[QTD_DIAS];
        Arrays.fill(arrayDiasVazio, 0);

        quadro.quartos = listQuartosLeitos
                .stream()
                .map(q -> {
                    QuadroQuarto qq = new QuadroQuarto(q.getId(), q.getNumero());
                    IntStream.rangeClosed(minimum, maximum)
                            .forEach( qtdLeitos -> qq.getLeitos().add(
                                    new QuadroLeito(0L, qtdLeitos, arrayDiasVazio)
                            ));
                    return qq;
                })
                .collect(Collectors.toList());

        // Varre mais uma vez os quartos do banco e seta os ids correspondentes. Os ids zerados significa que não existem no banco
        listQuartoEntities.forEach(q -> {
            q.getLeitos().forEach(leito -> {
                quadro.setLeitoIdPorNumero(q.getId(), leito.getNumero(), leito.getId());
            });
        });

        // Cada linha corresponde a um leitoId. Pode haver várias hospedagens no mesmo dia. Cada hospedagem possui vários dias
        // Setar todos os leitos do quadro (se encontrar)
        linhas.forEach(linha -> {
            linha.getHospedagens().forEach(lh -> {
                Long leitoId = linha.getLeitoId();
                Integer idxDiaIni = lh.getIdxIni();
                Integer idxDiaFim = lh.getIdxFim();
                quadro.quartos.forEach(qq -> qq.marcarDias(leitoId, idxDiaIni, idxDiaFim));
            });
        });

        return quadro;
    }

    private List<LocalDate> buildDias(LocalDate dIni, LocalDate dFim) {
        List<LocalDate> retorno = new ArrayList<>();
        for (LocalDate d = dIni; !d.isAfter(dFim);  d = d.plusDays(1)){
            retorno.add(d);
        }
        return retorno;
    }

    private List<MicroLeito> buildListaMicroLeitos(Map<Long, ArrayList<LinhaHospedagem>> mapaLinhaHospedagem) {
        Map<Long, MicroLeito> mapaMicroLeitos = buildListaLeitos();
        List<MicroLeito> listaMicroLeitos = mapaMicroLeitos.entrySet()
                .stream()
                .map( m -> {
                            MicroLeito leito = m.getValue();
                            List<LinhaHospedagem> lst = mapaLinhaHospedagem.get(m.getKey());
                            if (lst != null) {
                                leito.setHospedagens(lst);
                            }
                            return leito;
                        }
                ).collect(Collectors.toList());

        Collections.sort(listaMicroLeitos, Comparator.comparing(MicroLeito::getQuartoNumero)
                                                        .thenComparing(Comparator.comparing(MicroLeito::getLeitoNumero)));
        return listaMicroLeitos;
    }

    private Map<Long, MicroLeito> buildListaLeitos() {
        Map<Long, MicroLeito> _mapLeitos = leitoRepository.getListaCabecalhosLeitos()
                .stream()
                .collect(Collectors.toMap(  LeitoDTO::getId,
                        leito -> new MicroLeito(   leito.getId(),
                                leito.getNumero(),
                                leito.getQuartoId(),
                                leito.getQuartoNumero() )) );
        _mapLeitos.put(99999L, new MicroLeito(9999L, 9999, 9999L, 9999));
        return _mapLeitos;
    }
}