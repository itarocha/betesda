package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.application.out.report.RelatorioGeralRepository;
import br.com.itarocha.betesda.domain.hospedagem.PessoaEncaminhada;
import br.com.itarocha.betesda.domain.hospedagem.RelatorioGeral;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static br.com.itarocha.betesda.jooq.model.Tables.*;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.coalesce;

@Repository
@RequiredArgsConstructor
public class RelatorioGeralAdapter implements RelatorioGeralRepository {

    private final DSLContext create;

    @Override
    public List<PessoaEncaminhada> listPessoasEncaminhadas(LocalDate dataIni, LocalDate dataFim) {
        br.com.itarocha.betesda.jooq.model.tables.HospedeLeito hl = HOSPEDE_LEITO.as("hl");
        Field<String> tipoUtilizacaoT = val("T");

        Condition a1 = hl.DATA_ENTRADA.between(dataIni, dataFim).or(hl.DATA_SAIDA.between(dataIni, dataFim));
        Condition a2 = hl.DATA_ENTRADA.le(dataIni).and(hl.DATA_SAIDA.ge(dataFim));
        Condition condicaoA = a1.or(a2);

        Select<Record5<Long, Long, LocalDate, LocalDate, String>> s1 =
                create.select(hl.HOSPEDE_ID
                        , hl.LEITO_ID
                        , when(hl.DATA_ENTRADA.lessThan(dataIni), dataIni).otherwise(hl.DATA_ENTRADA).as("data_ini")
                        , when(hl.DATA_SAIDA.greaterThan(dataFim), dataFim).otherwise(hl.DATA_SAIDA).as("data_fim")
                        , tipoUtilizacaoT.as("tipo_utilizacao")
                )
                        .from(hl)
                        .where(condicaoA);

        br.com.itarocha.betesda.jooq.model.tables.Hospedagem hpd = HOSPEDAGEM.as("hpd");
        br.com.itarocha.betesda.jooq.model.tables.Hospede h = HOSPEDE.as("h");

        Field<Long> const0 = val(0L);
        Condition b1 = hpd.DATA_ENTRADA.between(dataIni, dataFim)
                .or(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA)
                        .between(dataIni, dataFim));
        Condition b2 = hpd.DATA_ENTRADA.le(dataIni)
                .and(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA).ge(dataFim));
        Condition b3 = hpd.TIPO_UTILIZACAO.eq("P");
        Condition condicaoB = b1.or(b2).and(b3);

        Select<Record5<Long, Long, LocalDate, LocalDate, String>> s2 =
                create.select(h.ID.as("hospede_id")
                        , const0.as("leito_id")
                        , when(hpd.DATA_ENTRADA.lt(dataIni), dataIni).otherwise(hpd.DATA_ENTRADA).as("data_ini")
                        , when(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA).greaterThan(dataFim), dataFim)
                                .otherwise(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA)).as("data_fim")
                        , hpd.TIPO_UTILIZACAO.as("tipo_utilizacao")
                )
                        .from(hpd)
                        .innerJoin(h)
                        .on(hpd.ID.eq(h.HOSPEDAGEM_ID))
                        .where(condicaoB);

        Select<Record5<Long, Long, LocalDate, LocalDate, String>> s3 = s1.unionAll(s2);

        List<PessoaEncaminhada> lista =
                create.selectDistinct(
                        HOSPEDE.HOSPEDAGEM_ID.as("hospedagemId")
                        , PESSOA.ID.as("pessoaId")
                        , PESSOA.NOME.as("pessoaNome")
                        , PESSOA.DATA_NASCIMENTO.as("pessoaDataNascimento")
                        , ENDERECO.CIDADE
                        , ENDERECO.UF
                        , HOSPEDE.TIPO_HOSPEDE_ID.as("tipoHospedeId")
                        , TIPO_HOSPEDE.DESCRICAO.as("tipoHospedeDescricao")
                        , s3.field("tipo_utilizacao", String.class).as("tipoUtilizacao")
                        , HOSPEDAGEM.DESTINACAO_HOSPEDAGEM_ID.as("destinacaoHospedagemId")
                        , DESTINACAO_HOSPEDAGEM.DESCRICAO.as("destinacaoHospedagemDescricao")
                        , ENTIDADE.ID.as("entidadeId")
                        , ENTIDADE.NOME.as("entidadeNome")
                )
                        .from(s3)
                        .innerJoin(HOSPEDE).on(HOSPEDE.ID.eq(s3.field("hospede_id", Long.class)))
                        .innerJoin(HOSPEDAGEM).on(HOSPEDAGEM.ID.eq(HOSPEDE.HOSPEDAGEM_ID))
                        .innerJoin(DESTINACAO_HOSPEDAGEM).on(DESTINACAO_HOSPEDAGEM.ID.eq(HOSPEDAGEM.DESTINACAO_HOSPEDAGEM_ID))
                        .innerJoin(ENTIDADE).on(ENTIDADE.ID.eq(HOSPEDAGEM.ENTIDADE_ID))
                        .innerJoin(TIPO_HOSPEDE).on(TIPO_HOSPEDE.ID.eq(HOSPEDE.TIPO_HOSPEDE_ID))
                        .innerJoin(PESSOA).on(PESSOA.ID.eq(HOSPEDE.PESSOA_ID))
                        .leftJoin(ENDERECO).on(ENDERECO.ID.eq(PESSOA.ENDERECO_ID))
                        .orderBy(	PESSOA.NOME
                                ,   PESSOA.ID
                                ,	HOSPEDE.HOSPEDAGEM_ID
                        )
                        .fetchInto(PessoaEncaminhada.class);
        return lista;
    }

    @Override
    public List<RelatorioGeral> listRelatorioGeral(LocalDate dataIni, LocalDate dataFim) {
        br.com.itarocha.betesda.jooq.model.tables.HospedeLeito hl = HOSPEDE_LEITO.as("hl");
        Field<String> tipoUtilizacaoT = val("T");

        Condition a1 = hl.DATA_ENTRADA.between(dataIni, dataFim).or(hl.DATA_SAIDA.between(dataIni, dataFim));
        Condition a2 = hl.DATA_ENTRADA.le(dataIni).and(hl.DATA_SAIDA.ge(dataFim));
        Condition condicaoA = a1.or(a2);

        Select<Record5<Long, Long, LocalDate, LocalDate, String>> s1 =
                create.select(hl.HOSPEDE_ID
                        , hl.LEITO_ID
                        , when(hl.DATA_ENTRADA.lessThan(dataIni), dataIni).otherwise(hl.DATA_ENTRADA).as("data_ini")
                        , when(hl.DATA_SAIDA.greaterThan(dataFim), dataFim).otherwise(hl.DATA_SAIDA).as("data_fim")
                        , tipoUtilizacaoT.as("tipo_utilizacao")
                )
                        .from(hl)
                        .where(condicaoA);

        br.com.itarocha.betesda.jooq.model.tables.Hospedagem hpd = HOSPEDAGEM.as("hpd");
        br.com.itarocha.betesda.jooq.model.tables.Hospede h = HOSPEDE.as("h");

        Field<Long> const0 = val(0L);
        Condition b1 = hpd.DATA_ENTRADA.between(dataIni, dataFim)
                .or(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA)
                        .between(dataIni, dataFim));
        Condition b2 = hpd.DATA_ENTRADA.le(dataIni)
                .and(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA).ge(dataFim));
        Condition b3 = hpd.TIPO_UTILIZACAO.eq("P");
        Condition condicaoB = b1.or(b2).and(b3);

        Select<Record5<Long, Long, LocalDate, LocalDate, String>> s2 =
                create.select(h.ID.as("hospede_id")
                        , const0.as("leito_id")
                        , when(hpd.DATA_ENTRADA.lt(dataIni), dataIni).otherwise(hpd.DATA_ENTRADA).as("data_ini")
                        , when(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA).greaterThan(dataFim), dataFim)
                                .otherwise(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA)).as("data_fim")
                        , hpd.TIPO_UTILIZACAO.as("tipo_utilizacao")
                )
                        .from(hpd)
                        .innerJoin(h)
                        .on(hpd.ID.eq(h.HOSPEDAGEM_ID))
                        .where(condicaoB);

        Select<Record5<Long, Long, LocalDate, LocalDate, String>> s3 = s1.unionAll(s2);

        List<RelatorioGeral> lista =
                create.select(
                        HOSPEDE.HOSPEDAGEM_ID.as("hospedagemId")
                        , s3.field("hospede_id", Long.class).as("hospedeId")
                        , s3.field("leito_id", Long.class).as("leitoId")
                        , s3.field("data_ini", LocalDate.class).as("dataIni")
                        , s3.field("data_fim", LocalDate.class).as("dataFim")
                        , localDateDiff(s3.field("data_fim", LocalDate.class), s3.field("data_ini", LocalDate.class)).add(1).as("dias")
                        , s3.field("tipo_utilizacao", String.class).as("tipoUtilizacao")
                        , HOSPEDE.PESSOA_ID.as("pessoaId")
                        , PESSOA.NOME.as("pessoaNome")
                        , PESSOA.DATA_NASCIMENTO.as("pessoaDataNascimento")
                        , ENDERECO.CIDADE
                        , ENDERECO.UF
                        , HOSPEDE.TIPO_HOSPEDE_ID.as("tipoHospedeId")
                        , TIPO_HOSPEDE.DESCRICAO.as("tipoHospedeDescricao")
                        , ENTIDADE.ID.as("entidadeId")
                        , ENTIDADE.NOME.as("entidadeNome")
                )
                        .from(s3)
                        .innerJoin(HOSPEDE).on(HOSPEDE.ID.eq(s3.field("hospede_id", Long.class)))
                        .innerJoin(PESSOA).on(PESSOA.ID.eq(HOSPEDE.PESSOA_ID))
                        .innerJoin(TIPO_HOSPEDE).on(TIPO_HOSPEDE.ID.eq(HOSPEDE.TIPO_HOSPEDE_ID))
                        .innerJoin(HOSPEDAGEM).on(HOSPEDAGEM.ID.eq(HOSPEDE.HOSPEDAGEM_ID))
                        .innerJoin(ENTIDADE).on(ENTIDADE.ID.eq(HOSPEDAGEM.ENTIDADE_ID))
                        .leftJoin(ENDERECO).on(ENDERECO.ID.eq(PESSOA.ENDERECO_ID))
                        .orderBy(	HOSPEDE.ID
                                , 	s3.field("data_ini") )
                        .fetchInto(RelatorioGeral.class);

        return lista;
    }
}