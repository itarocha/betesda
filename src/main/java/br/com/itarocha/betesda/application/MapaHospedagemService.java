package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.application.port.in.QuartoUseCase;
import br.com.itarocha.betesda.domain.LeitoDTO;
import br.com.itarocha.betesda.domain.Quarto;
import br.com.itarocha.betesda.domain.hospedagem.*;
import br.com.itarocha.betesda.domain.strategy.ClasseCelulaStrategy;
import br.com.itarocha.betesda.utils.LocalDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jooq.*;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ValueRange;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static br.com.itarocha.betesda.jooq.model.Tables.*;
import static br.com.itarocha.betesda.jooq.model.Tables.HOSPEDAGEM;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.jooq.impl.DSL.*;

@Service
@RequiredArgsConstructor
public class MapaHospedagemService {

    private final QuartoUseCase quartoService;
    private final ClasseCelulaStrategy classeCelulaStrategy;

    private final DSLContext create;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int QTD_DIAS = 7;

    @Deprecated
    public MapaRetorno buildMapaRetorno(LocalDate dataBase) {
        MapaRetorno retorno = new MapaRetorno();

        LocalDate dIni = LocalDateUtils.primeiroDiaDaSemana(dataBase);
        LocalDate dFim = dIni.plusDays(QTD_DIAS - 1);
        LocalDate hoje = LocalDate.now();

        retorno.setDataIni(dIni);
        retorno.setDataFim(dFim);

        //1 - MAPA DE HOSPEDAGENS
        List<HospedeMapa> listaHospedeLeito = buildHospedeLeito(dIni, dFim);

        Map<Long, MicroLeito> _mapLeitos = buildListaLeitos();
        Map<Long, ArrayList<LinhaHospedagem>>  _mapHospedagens = new HashMap<>();

        listaHospedeLeito.forEach(hospedeMapa -> {
            LinhaHospedagem linhaHospedagem = new LinhaHospedagem();

            ClasseInicioEnum classeIni = classeCelulaStrategy.resolveClasseIni(hospedeMapa);
            ClasseFimEnum classeFim = classeCelulaStrategy.resolveClasseFim(hospedeMapa);

            linhaHospedagem.setClsIni(classeIni.getDescricao());
            linhaHospedagem.setClsFim(classeFim.getDescricao());

            int iIni = Math.toIntExact(DAYS.between(dIni, hospedeMapa.getDataIniNoPeriodo() ));
            int iFim = Math.toIntExact(DAYS.between(dIni, hospedeMapa.getDataFimNoPeriodo() ));
            Integer[] dias = new Integer[QTD_DIAS];
            Arrays.fill(dias, 0);
            IntStream.rangeClosed(iIni, iFim).forEach(n -> dias[n] = 1);

            linhaHospedagem.setDias(dias);
            linhaHospedagem.setIdxIni(iIni);
            linhaHospedagem.setIdxFim(iFim);
            linhaHospedagem.setWidth((iFim - iIni + 1)*100);

            hospedeMapa.setDias(dias);

            linhaHospedagem.setIdentificador(hospedeMapa.getIdentificador());
            linhaHospedagem.setHpdId(hospedeMapa.getHospedagemId());
            linhaHospedagem.setStatus(hospedeMapa.getStatusHospedagem().toString());
            linhaHospedagem.setNome(hospedeMapa.getPessoaNome());
            linhaHospedagem.setTelefone(hospedeMapa.getPessoaTelefone());


            if (_mapHospedagens.containsKey(hospedeMapa.getLeitoId())) {
                _mapHospedagens.get(hospedeMapa.getLeitoId()).add(linhaHospedagem);
            } else {
                ArrayList<LinhaHospedagem> lst = new ArrayList<>();
                lst.add(linhaHospedagem);
                _mapHospedagens.put(hospedeMapa.getLeitoId(), lst);
            }
        });

        _mapLeitos.entrySet().forEach(m -> {
            Long leitoId = m.getKey();
            MicroLeito leito = m.getValue();
            List<LinhaHospedagem> lst = _mapHospedagens.get(leitoId);
            if (lst != null) {
                leito.setHospedagens(lst);
            }
            retorno.getLinhas().add(leito);
        });
        retorno.getLinhas()
                .sort((a, b) -> String.format("%06d-%06d", a.getQuartoNumero(), a.getLeitoNumero())
                .compareTo( String.format("%06d-%06d", b.getQuartoNumero(), b.getLeitoNumero()) ) );

        // 2 - HÓSPEDES
        //TODO Criar estrutura pessoa+hospedagem, detalhes
        retorno.setHospedes(listaHospedeLeito);
        retorno.getHospedes()
                .sort((a, b) -> a.getPessoaNome().compareTo(b.getPessoaNome()) );

        //INÍCIO
        Map<String, List<String>> porCidade = new TreeMap<>();
        retorno.getHospedes().forEach(h -> {
            if (porCidade.get(h.getPessoaCidadeUfOrigem()) == null ) {
                List<String> lst = new ArrayList<>();
                lst.add(h.getIdentificador());
                porCidade.put(h.getPessoaCidadeUfOrigem(), lst);
            } else {
                List<String> lst = porCidade.get(h.getPessoaCidadeUfOrigem());
                lst.add(h.getIdentificador());
            }
        });

        List<CidadeHospedagens> cidades = new ArrayList<>();
        porCidade.keySet().forEach(k -> {
            cidades.add(new CidadeHospedagens(k, porCidade.get(k)));
        });
        retorno.setCidades(cidades);

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

        //TRATATIVA DO QUADRO

        Quadro quadro = new Quadro();

        // Busca quartos e leitos do banco
        List<Quarto> listQuartoEntities = quartoService.findAll();
        // Cria os quartos e verifica minimo e maximo dos leitos
        AtomicInteger atomicMinLeito = new AtomicInteger(Integer.MAX_VALUE);
        AtomicInteger atomicMaxLeito = new AtomicInteger(Integer.MIN_VALUE);
        listQuartoEntities.forEach(q -> {
            quadro.quartos.add(new QuadroQuarto( q.getId(), q.getNumero()));

            q.getLeitos().forEach(l -> {
                if (l.getNumero() < atomicMinLeito.get()) {
                    atomicMinLeito.set(l.getNumero());
                } else if (l.getNumero() > atomicMaxLeito.get() ) {
                    atomicMaxLeito.set(l.getNumero());
                }
            });
        });

        // Adiciona todos os leitos em todos os quartos. Todos os leitos estão com id zerado
        quadro.quartos.forEach(q -> {
            IntStream.rangeClosed(atomicMinLeito.get(), atomicMaxLeito.get()).forEach(
                    n -> q.leitos.add(new QuadroLeito(0L, n, QTD_DIAS))
            );
        });

        // Varre mais uma vez os quartos do banco e seta os ids correspondentes. Os ids zerados significa que não existem no banco
        listQuartoEntities.forEach(q -> {
            q.getLeitos().forEach(leito -> {
                quadro.setLeitoIdPorNumero(q.getId(), leito.getNumero(), leito.getId());
            });
        });

        quadro.quartos.forEach(q -> {
            q.leitos.forEach(leito -> {
                if (!leito.id.equals(0)) {
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

        retorno.setQuadro(quadro);

        return retorno;
    }

    public List<HospedeMapa> buildHospedeLeito(LocalDate dataIni, LocalDate dataFim){
        // PRIMEIRO SELECT - HÓSPEDES COM LEITO
        br.com.itarocha.betesda.jooq.model.tables.HospedeLeito hl = HOSPEDE_LEITO.as("hl");
        br.com.itarocha.betesda.jooq.model.tables.Leito leito = LEITO.as("leito");
        br.com.itarocha.betesda.jooq.model.tables.Quarto quarto = QUARTO.as("quarto");
        br.com.itarocha.betesda.jooq.model.tables.HospedeLeito hlx = HOSPEDE_LEITO.as("hlx");
        Field<Integer> tipo0 = val(0);
        Field<String> tipoUtilizacaoT = val("T");

        Condition a1 = hl.DATA_ENTRADA.between(dataIni, dataFim)
                .or(hl.DATA_SAIDA.between(dataIni, dataFim));
        Condition a2 = hl.DATA_ENTRADA.le(dataIni).and(hl.DATA_SAIDA.ge(dataFim));
        Condition condicaoA = a1.or(a2);

        Select<Record14<Long, Long, Long, Integer, Long, Integer, Integer, LocalDate, LocalDate, LocalDate, LocalDate, String, LocalDate, LocalDate>>
                s1 =
                create.select(hl.HOSPEDE_ID.as("hospede_id")
                        , hl.ID.as("identificador")
                        , quarto.ID.as("quarto_id")
                        , quarto.NUMERO.as("quarto_numero")
                        , leito.ID.as("leito_id")
                        , leito.NUMERO.as("leito_numero")
                        , tipo0.as("tipo")
                        , hl.DATA_ENTRADA.as("data_entrada_leito")
                        , hl.DATA_SAIDA.as("data_saida_leito")
                        , when(hl.DATA_ENTRADA.lessThan(dataIni), dataIni).otherwise(hl.DATA_ENTRADA).as("data_ini")
                        , when(hl.DATA_SAIDA.greaterThan(dataFim), dataFim).otherwise(hl.DATA_SAIDA).as("data_fim")
                        , tipoUtilizacaoT.as("tipo_utilizacao")
                        , min(hl.DATA_ENTRADA).as("data_primeira_entrada")
                        , max(hl.DATA_ENTRADA).as("data_ultima_entrada")
                )
                        .from(hl)
                        .innerJoin(leito).on(hl.LEITO_ID.eq(leito.ID))
                        .innerJoin(quarto).on(quarto.ID.eq(leito.QUARTO_ID))
                        .where(condicaoA)
                        .groupBy(hl.HOSPEDE_ID, hl.ID, quarto.ID, quarto.NUMERO, leito.ID, leito.NUMERO, hl.DATA_ENTRADA, hl.DATA_SAIDA);


        // SEGUNDO SQL - HÓSPEDES SEM LEITO
        br.com.itarocha.betesda.jooq.model.tables.Hospedagem hpd = HOSPEDAGEM.as("hpd");
        br.com.itarocha.betesda.jooq.model.tables.Hospede h = HOSPEDE.as("h");

        Field<Long> const99999 = val(99999L);
        Field<Integer> const0 = val(0);
        Field<Integer> tipo1 = val(1);

        Condition b1 = hpd.DATA_ENTRADA.between(dataIni, dataFim)
                .or(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA)
                        .between(dataIni, dataFim));
        Condition b2 = hpd.DATA_ENTRADA.le(dataIni)
                .and(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA).ge(dataFim));
        Condition b3 = hpd.TIPO_UTILIZACAO.eq("P");
        Condition condicaoB = b1.or(b2).and(b3);

        Select<Record14<Long, Long, Long, Integer, Long, Integer, Integer, LocalDate, LocalDate, LocalDate, LocalDate, String, LocalDate, LocalDate>>
                s2 =
                create.select(h.ID.as("hospede_id")
                        , h.ID.as("identificador")
                        , const99999.as("quarto_id")
                        , const0.as("quarto_numero")
                        , const99999.as("leito_id")
                        , const0.as("leito_numero")
                        , tipo1.as("tipo")
                        , hpd.DATA_ENTRADA.as("data_entrada_leito")
                        , coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA).as("data_saida_leito")
                        , when(hpd.DATA_ENTRADA.lessThan(dataIni), dataIni).otherwise(hpd.DATA_ENTRADA).as("data_ini").as("data_ini")
                        , when(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA).greaterThan(dataFim), dataFim)
                                .otherwise(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA)).as("data_fim")
                        , hpd.TIPO_UTILIZACAO
                        , hpd.DATA_ENTRADA.as("data_primeira_entrada")
                        , hpd.DATA_ENTRADA.as("data_ultima_entrada")
                )
                        .from(hpd)
                        .innerJoin(h)
                        .on(hpd.ID.eq(h.HOSPEDAGEM_ID))
                        .where(condicaoB);

        // TERCEIRO SQL - UNION
        Select<Record14<Long, Long, Long, Integer, Long, Integer, Integer, LocalDate, LocalDate, LocalDate, LocalDate, String, LocalDate, LocalDate>> s3 =
                s1.unionAll(s2);

        // QUARTO SQL - JUNCOES
        List<HospedeMapa> lista =
                create.select(
                        s3.field("identificador", Long.class).as("id")
                        , HOSPEDAGEM.TIPO_UTILIZACAO.as("tipoUtilizacao")
                        , s3.field("quarto_id", Long.class).as("quartoId")
                        , s3.field("quarto_numero", Integer.class).as("quartoNumero")
                        , s3.field("leito_id", Long.class).as("leitoId")
                        , s3.field("leito_numero", Integer.class).as("leitoNumero")
                        , PESSOA.ID.as("pessoa_id").as("pessoaId")
                        , PESSOA.NOME.as("pessoa_nome").as("pessoaNome")
                        , PESSOA.TELEFONE.as("pessoa_telefone").as("pessoaTelefone")
                        , ENDERECO.CIDADE
                        , ENDERECO.UF
                        , HOSPEDAGEM.DATA_ENTRADA.as("dataEntradaHospedagem")
                        , coalesce(HOSPEDAGEM.DATA_EFETIVA_SAIDA, HOSPEDAGEM.DATA_PREVISTA_SAIDA).as("dataSaidaHospedagem")
                        , s3.field("data_primeira_entrada", LocalDate.class).as("dataPrimeiraEntrada")
                        , s3.field("data_ultima_entrada", LocalDate.class).as("dataUltimaEntrada")
                        , s3.field("data_entrada_leito", LocalDate.class).as("dataEntradaLeito")
                        , s3.field("data_saida_leito", LocalDate.class).as("dataSaidaLeito")
                        , s3.field("data_ini", LocalDate.class).as("dataIniNoPeriodo")
                        , s3.field("data_fim", LocalDate.class).as("dataFimNoPeriodo")
                        , HOSPEDE.HOSPEDAGEM_ID.as("hospedagemId")
                        , s3.field("hospede_id", Long.class).as("hospedeId")
                        , HOSPEDE.TIPO_HOSPEDE_ID.as("tipoHospedeId")
                        , HOSPEDE.BAIXADO.as("baixadoString")
                        , TIPO_HOSPEDE.DESCRICAO.as("tipoHospedeDescricao")
                        , HOSPEDAGEM.DESTINACAO_HOSPEDAGEM_ID.as("destinacaoHospedagemId")
                        , DESTINACAO_HOSPEDAGEM.DESCRICAO.as("destinacaoHospedagemDescricao")
                        , HOSPEDAGEM.DATA_PREVISTA_SAIDA.as("dataPrevistaSaida")
                        , HOSPEDAGEM.DATA_EFETIVA_SAIDA.as("dataEfetivaSaida")
                )
                        .from(s3)
                        .innerJoin(HOSPEDE).on(HOSPEDE.ID.eq(s3.field("hospede_id", Long.class)))
                        .innerJoin(HOSPEDAGEM).on(HOSPEDAGEM.ID.eq(HOSPEDE.HOSPEDAGEM_ID))
                        .innerJoin(DESTINACAO_HOSPEDAGEM).on(DESTINACAO_HOSPEDAGEM.ID.eq(HOSPEDAGEM.DESTINACAO_HOSPEDAGEM_ID))
                        .innerJoin(TIPO_HOSPEDE).on(TIPO_HOSPEDE.ID.eq(HOSPEDE.TIPO_HOSPEDE_ID))
                        .innerJoin(PESSOA).on(PESSOA.ID.eq(HOSPEDE.PESSOA_ID))
                        .leftJoin(ENDERECO).on(ENDERECO.ID.eq(PESSOA.ENDERECO_ID))
                        .orderBy( s3.field("tipo")
                                , s3.field("quarto_numero")
                                , s3.field("leito_numero")
                                , PESSOA.NOME
                                , PESSOA.ID
                                , HOSPEDE.HOSPEDAGEM_ID
                        ).fetchInto(HospedeMapa.class);

        return lista;
    }

    private Map<Long, MicroLeito> buildListaLeitos() {
        Map<Long, MicroLeito> _mapLeitos = new TreeMap<>();
        listCabecalhosLeitos().stream()
                .forEach(
                        record -> _mapLeitos.put(   record.getId(),
                                                    new MicroLeito(record.getId(),
                                                            record.getNumero(),
                                                            record.getQuartoId(),
                                                            record.getQuartoNumero() )
                                                )
                );
        _mapLeitos.put(99999L, new MicroLeito(9999L, 9999, 9999L, 9999));
        return _mapLeitos;
    }

    public MapaLinhas buildMapaLinhas(LocalDate dataBase) {
        MapaLinhas retorno = new MapaLinhas();

        LocalDate dIni = LocalDateUtils.primeiroDiaDaSemana(dataBase);
        LocalDate dFim = dIni.plusDays(QTD_DIAS - 1);
        LocalDate hoje = LocalDate.now();

        retorno.setDataIni(dIni);
        retorno.setDataFim(dFim);

        //1 - MAPA DE HOSPEDAGENS
        List<HospedeMapa> listaHospedeLeito = buildHospedeLeito(dIni, dFim);

        Map<Long, MiniLeito> _mapLeitos = buildListaMiniLeitos();
        Map<Long, ArrayList<Linha>>  _mapHospedagens = new HashMap<>();

        listaHospedeLeito.forEach(hm -> {
            String classeIni = classeCelulaStrategy.resolveClasseIni(hm).getDescricao();
            String classeFim = classeCelulaStrategy.resolveClasseFim(hm).getDescricao();

            Linha linhaHospedagem = new Linha();

            linhaHospedagem.setIdentificador(hm.getIdentificador());
            linhaHospedagem.setHpdId(hm.getHospedagemId());
            linhaHospedagem.setStatus(hm.getStatusHospedagem().toString());
            linhaHospedagem.setNome(hm.getPessoaNome());
            linhaHospedagem.setTelefone(hm.getPessoaTelefone());
            linhaHospedagem.setDataIni(hm.getDataIniNoPeriodo());
            linhaHospedagem.setDataFim(hm.getDataFimNoPeriodo());

            linhaHospedagem.setClsIni(classeIni.trim());
            linhaHospedagem.setClsFim(classeFim.trim());

            if (_mapHospedagens.containsKey(hm.getLeitoId())) {
                _mapHospedagens.get(hm.getLeitoId()).add(linhaHospedagem);
            } else {
                ArrayList<Linha> lst = new ArrayList<>();
                lst.add(linhaHospedagem);
                _mapHospedagens.put(hm.getLeitoId(), lst);
            }
        });

        _mapLeitos.entrySet().forEach(m -> {
            Long leitoId = m.getKey();
            MiniLeito leito = m.getValue();
            List<Linha> lst = _mapHospedagens.get(leitoId);
            if (lst != null) {
                leito.setLinhas(lst);
            }
            retorno.getLeitos().add(leito);
        });
        retorno.getLeitos().sort((a, b) -> String.format("%06d-%06d", a.getQuartoNumero(), a.getLeitoNumero())
                .compareTo( String.format("%06d-%06d", b.getQuartoNumero(), b.getLeitoNumero()) ) );

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
        return retorno;
    }

    private Map<Long, MiniLeito> buildListaMiniLeitos() {
        Map<Long, MiniLeito> _mapLeitos = new TreeMap<>();
        listCabecalhosLeitos().stream()
                .forEach( r -> _mapLeitos.put(  r.getId(), new MiniLeito(   r.getId(),
                                                                            r.getNumero(),
                                                                            r.getQuartoId(),
                                                                            r.getQuartoNumero() ))
        );
        _mapLeitos.put(99999L, new MiniLeito(9999L, 9999, 9999L, 9999));
        return _mapLeitos;
    }

    //TODO Mover para repositório
    private List<LeitoDTO> listCabecalhosLeitos(){
        // Substituindo /sql/_OBSOLETO_leitos_header_native.sql
        return create.select(LEITO.ID, LEITO.NUMERO, QUARTO.ID, QUARTO.NUMERO)
                .from(LEITO)
                .innerJoin(QUARTO).on(LEITO.QUARTO_ID.eq(QUARTO.ID))
                .orderBy(QUARTO.NUMERO, LEITO.NUMERO)
                .fetch()
                .map(r -> new LeitoDTO(r.get(LEITO.ID),r.get(LEITO.NUMERO),r.get(QUARTO.ID),r.get(QUARTO.NUMERO)));
    }
}