package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.model.Logico;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncaminhadorTest {

    @Test
    public void testeBuilderComSucesso(){
        Encaminhador pojo = Encaminhador.builder().build();
        assertEquals(Logico.S, pojo.getAtivo());
    }
}