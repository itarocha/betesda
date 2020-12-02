package br.com.itarocha.betesda.adapter.out.persistence.entity;

import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncaminhadorEntityTest {

    @Test
    public void testeBuilderComSucesso(){
        assertEquals(2, 1+1);
        EncaminhadorEntity pojo = EncaminhadorEntity.builder().build();
        assertEquals(LogicoEnum.S, pojo.getAtivo());
    }
}