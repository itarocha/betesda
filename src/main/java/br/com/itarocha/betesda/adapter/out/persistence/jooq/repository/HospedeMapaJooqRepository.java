package br.com.itarocha.betesda.adapter.out.persistence.jooq.repository;

import br.com.itarocha.betesda.core.ports.out.HospedeMapaRepository;
import br.com.itarocha.betesda.domain.hospedagem.HospedeMapa;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static br.com.itarocha.betesda.jooq.model.Tables.*;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.coalesce;

@Service
@RequiredArgsConstructor
public class HospedeMapaJooqRepository implements HospedeMapaRepository {

    private final DSLContext create;

    @Override
    public List<HospedeMapa> buildListaHospedeMapa(LocalDate dataIni, LocalDate dataFim){
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

        //, (SELECT MIN(hlx.data_entrada) FROM hospede_leito hlx WHERE hlx.hospede_id = hl.hospede_id) AS data_primeira_entrada
        //, (SELECT MAX(hlx.data_entrada) FROM hospede_leito hlx WHERE hlx.hospede_id = hl.hospede_id) AS data_ultima_entrada

        //dsl.selectFrom(SERIES)
        //        .orderBy(DSL.field(dsl.select(DSL.max(COMPETITION.COMPETITION_DATE)).from(COMPETITION)
        //                .where(COMPETITION.SERIES_ID.eq(SERIES.ID))).desc())
        //        .fetch()

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
                                , field(select(min(HOSPEDE_LEITO.DATA_ENTRADA)).from(HOSPEDE_LEITO).where(HOSPEDE_LEITO.HOSPEDE_ID.eq(hl.HOSPEDE_ID))).as("data_primeira_entrada")
                                , field(select(max(HOSPEDE_LEITO.DATA_ENTRADA)).from(HOSPEDE_LEITO).where(HOSPEDE_LEITO.HOSPEDE_ID.eq(hl.HOSPEDE_ID))).as("data_ultima_entrada")
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
        return create.select(
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
    }
}
