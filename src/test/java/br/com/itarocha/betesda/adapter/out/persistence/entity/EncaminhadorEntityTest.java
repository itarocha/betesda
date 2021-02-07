package br.com.itarocha.betesda.adapter.out.persistence.entity;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EncaminhadorEntity;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static br.com.itarocha.betesda.jooq.model.Tables.HOSPEDE_LEITO;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncaminhadorEntityTest {
    @Test
    public void testeBuilderComSucesso(){
        assertEquals(2, 1+1);
        EncaminhadorEntity pojo = EncaminhadorEntity.builder().build();
        assertEquals(LogicoEnum.S, pojo.getAtivo());
    }
}