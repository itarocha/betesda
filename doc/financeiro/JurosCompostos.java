package br.com.itarocha.betesda.utils.financeiro;

public class JurosCompostos {

    public static double calcularTaxa(double valorPresente, double valorPrestacao, int numPrestacoes) {
        if (valorPresente <= 0 || valorPrestacao <= 0 || numPrestacoes <= 0) {
            throw new IllegalArgumentException("Todos os valores devem ser positivos.");
        }
        if (valorPrestacao * numPrestacoes <= valorPresente) {
            throw new IllegalArgumentException("O valor total das prestacoes deve ser maior que o valor presente.");
        }

        double taxa = 0.1;
        for (int i = 0; i < 100; i++) {
            double fator = Math.pow(1 + taxa, -numPrestacoes);
            double valorAtual = valorPrestacao * (1 - fator) / taxa;
            double derivada = (fator * numPrestacoes) / (1 + taxa) - (1 - fator) / (taxa * taxa);
            double novaTaxa = taxa - (valorAtual - valorPresente) / derivada;
            if (Math.abs(novaTaxa - taxa) < 1e-10) {
                return novaTaxa * 100;
            }
            taxa = novaTaxa;
        }
        return taxa * 100;
    }

    public static double calcularValorFuturo(double valorPresente, double taxa, int numPeriodos) {
        return valorPresente * Math.pow(1 + taxa / 100, numPeriodos);
    }

    public static double calcularPrestacao(double valorPresente, double taxa, int numPrestacoes) {
        double taxaDecimal = taxa / 100;
        return valorPresente * (taxaDecimal * Math.pow(1 + taxaDecimal, numPrestacoes))
                / (Math.pow(1 + taxaDecimal, numPrestacoes) - 1);
    }

    public static void main(String[] args) {
        double valorTaxa = calcularTaxa(50000, 2700, 24);
        System.out.println(valorTaxa);
    }
}
