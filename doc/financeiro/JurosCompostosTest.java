package br.com.itarocha.betesda.utils.financeiro;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JurosCompostosTest {

    @Test
    void testCalcularTaxa_20Cenarios() {
        double[][] massaDados = {
                {1000, 120, 12},
                {500, 50, 12},
                {2000, 180, 24},
                {10000, 500, 24},
                {5000, 200, 36},
                {3000, 350, 10},
                {800, 90, 12},
                {1500, 140, 12},
                {2500, 230, 12},
                {6000, 280, 36},
                {4000, 380, 12},
                {750, 70, 12},
                {1200, 110, 12},
                {1800, 165, 12},
                {3500, 320, 12},
                {9000, 420, 36},
                {5500, 250, 24},
                {4500, 400, 12},
                {100, 15, 6},
                {700, 65, 12},
                {50000, 2756.65, 24}
        };

        for (int i = 0; i < massaDados.length; i++) {
            double valorPresente = massaDados[i][0];
            double valorPrestacao = massaDados[i][1];
            int numPrestacoes = (int) massaDados[i][2];

            double taxa = JurosCompostos.calcularTaxa(valorPresente, valorPrestacao, numPrestacoes);

            double prestacaoCalculada = JurosCompostos.calcularPrestacao(valorPresente, taxa, numPrestacoes);
            double diferenca = Math.abs(prestacaoCalculada - valorPrestacao);
            double tolerancia = valorPrestacao * 0.01;

            assertTrue(diferenca < tolerancia,
                    "Cenario " + (i+1) + ": Taxa=" + taxa + "%, Prestacao calculada=" + prestacaoCalculada);
        }
    }

    @Test
    void testCalcularValorFuturo_20Cenarios() {
        double[][] massaDados = {
                {1000, 5, 12},
                {500, 2, 24},
                {2000, 1.5, 36},
                {3000, 3, 6},
                {5000, 4, 18},
                {150, 1, 48},
                {800, 2.5, 30},
                {1200, 3.5, 12},
                {2500, 1, 60},
                {4000, 2, 24},
                {600, 5, 10},
                {3500, 3, 20},
                {1800, 4.5, 8},
                {100, 6, 12},
                {4500, 2.2, 15},
                {750, 1.8, 40},
                {3200, 3.2, 14},
                {550, 4, 22},
                {2800, 1.2, 50},
                {900, 5.5, 6}
        };

        for (int i = 0; i < massaDados.length; i++) {
            double valorPresente = massaDados[i][0];
            double taxa = massaDados[i][1];
            int numPeriodos = (int) massaDados[i][2];

            double valorFuturo = JurosCompostos.calcularValorFuturo(valorPresente, taxa, numPeriodos);

            assertTrue(valorFuturo > valorPresente,
                    "Cenario " + (i+1) + ": VF=" + valorFuturo + " deve ser > VP=" + valorPresente);
            assertEquals(valorPresente * Math.pow(1 + taxa/100, numPeriodos), valorFuturo, 0.01,
                    "Cenario " + (i+1));
        }
    }

    @Test
    void testCalcularPrestacao_20Cenarios() {
        double[][] massaDados = {
                {1000, 5, 12},
                {500, 2, 24},
                {2000, 1.5, 36},
                {3000, 3, 6},
                {5000, 4, 18},
                {150, 1, 48},
                {800, 2.5, 30},
                {1200, 3.5, 12},
                {2500, 1, 60},
                {4000, 2, 24},
                {600, 5, 10},
                {3500, 3, 20},
                {1800, 4.5, 8},
                {100, 6, 12},
                {4500, 2.2, 15},
                {750, 1.8, 40},
                {3200, 3.2, 14},
                {550, 4, 22},
                {2800, 1.2, 50},
                {900, 5.5, 6}
        };

        for (int i = 0; i < massaDados.length; i++) {
            double valorPresente = massaDados[i][0];
            double taxa = massaDados[i][1];
            int numPrestacoes = (int) massaDados[i][2];

            double prestacao = JurosCompostos.calcularPrestacao(valorPresente, taxa, numPrestacoes);
            double totalPago = prestacao * numPrestacoes;

            assertTrue(prestacao > 0, "Cenario " + (i+1) + ": Prestacao deve ser positiva");
            assertTrue(totalPago > valorPresente,
                    "Cenario " + (i+1) + ": Total pago=" + totalPago + " deve ser > VP=" + valorPresente);
        }
    }

    @Test
    void testCalcularNumeroPeriodos_20Cenarios() {
        double[][] massaDados = {
                {1000, 5, 120},
                {500, 2, 50},
                {2000, 1.5, 230},
                {3000, 3, 350},
                {5000, 4, 580},
                {150, 1, 200},
                {800, 2.5, 90},
                {1200, 3.5, 140},
                {2500, 1, 2900},
                {4000, 2, 450},
                {600, 5, 70},
                {3500, 3, 400},
                {1800, 4.5, 210},
                {100, 6, 15},
                {4500, 2.2, 510},
                {750, 1.8, 90},
                {3200, 3.2, 370},
                {550, 4, 65},
                {2800, 1.2, 3100},
                {900, 5.5, 105}
        };

        for (int i = 0; i < massaDados.length; i++) {
            double valorPresente = massaDados[i][0];
            double taxa = massaDados[i][1];
            double valorPrestacao = massaDados[i][2];

            double taxaDecimal = taxa / 100;
            double numerador = Math.log(valorPrestacao / (valorPrestacao - valorPresente * taxaDecimal));
            double denominador = Math.log(1 + taxaDecimal);
            int numPeriodosEsperado = (int) Math.ceil(numerador / denominador);

            double prestacaoCalculada = JurosCompostos.calcularPrestacao(valorPresente, taxa, numPeriodosEsperado);
            double prestacaoAnterior = JurosCompostos.calcularPrestacao(valorPresente, taxa, numPeriodosEsperado - 1);

            assertTrue(prestacaoCalculada <= valorPrestacao,
                    "Cenario " + (i+1) + ": Prestacao=" + prestacaoCalculada + " <= " + valorPrestacao);
            assertTrue(prestacaoAnterior > valorPrestacao,
                    "Cenario " + (i+1) + ": Com " + (numPeriodosEsperado-1) + " parcelas, prestacao=" + prestacaoAnterior + " > " + valorPrestacao);
        }
    }

    @Test
    void testCalcularTaxa_ValoresInvalidos() {
        assertThrows(IllegalArgumentException.class, () ->
                JurosCompostos.calcularTaxa(0, 100, 12));
        assertThrows(IllegalArgumentException.class, () ->
                JurosCompostos.calcularTaxa(1000, 0, 12));
        assertThrows(IllegalArgumentException.class, () ->
                JurosCompostos.calcularTaxa(1000, 100, 0));
        assertThrows(IllegalArgumentException.class, () ->
                JurosCompostos.calcularTaxa(1000, 80, 12));
    }

    @Test
    void testCalcularValorFuturo_ValoresInvalidos() {
        assertThrows(IllegalArgumentException.class, () ->
                JurosCompostos.calcularValorFuturo(0, 5, 12));
        assertThrows(IllegalArgumentException.class, () ->
                JurosCompostos.calcularValorFuturo(1000, -5, 12));
    }
}
