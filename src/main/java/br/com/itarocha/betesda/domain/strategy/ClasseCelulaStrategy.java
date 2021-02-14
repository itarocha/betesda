package br.com.itarocha.betesda.domain.strategy;

import br.com.itarocha.betesda.domain.hospedagem.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class ClasseCelulaStrategy {

    private Map<ClasseInicioEnum, Predicate<HospedeMapa>> mapaClasseInicio;
    private Map<ClasseFimEnum, Predicate<HospedeMapa>> mapaClasseFim;

    public ClasseCelulaStrategy(){
        mapaClasseInicio = new HashMap<>();
        mapaClasseFim = new HashMap<>();

        mapaClasseInicio.put(ClasseInicioEnum.VINDO,    h -> h.getDataEntradaLeito().equals(h.getDataIniNoPeriodo())
                && h.getDataEntradaLeito().isAfter(h.getDataPrimeiraEntrada()) );
        mapaClasseInicio.put(ClasseInicioEnum.INICIO,   h -> h.getDataEntradaLeito().equals(h.getDataIniNoPeriodo())
                && !h.getDataEntradaLeito().isAfter(h.getDataPrimeiraEntrada()));
        mapaClasseInicio.put(ClasseInicioEnum.DURANTE,  h -> h.getDataEntradaLeito().isBefore(h.getDataIniNoPeriodo()));

        mapaClasseFim.put(ClasseFimEnum.INDO,       h -> h.getDataSaidaLeito().equals(h.getDataFimNoPeriodo())
                &&  h.getDataSaidaLeito().isBefore(h.getDataUltimaEntrada()));
        mapaClasseFim.put(ClasseFimEnum.BAIXADO,    h -> h.getDataSaidaLeito().equals(h.getDataFimNoPeriodo())
                &&  h.getBaixado());
        mapaClasseFim.put(ClasseFimEnum.FIM,        h -> h.getDataSaidaLeito().equals(h.getDataFimNoPeriodo())
                && !h.getBaixado());
        mapaClasseFim.put(ClasseFimEnum.DURANTE,    h -> h.getDataSaidaLeito().isAfter(h.getDataFimNoPeriodo()));
    }

    public ClasseInicioEnum resolveClasseIni(HospedeMapa hm){
        return mapaClasseInicio.entrySet()
                .stream()
                .filter(m -> m.getValue().test(hm))
                .map(m -> m.getKey())
                .findFirst()
                .orElse(ClasseInicioEnum.INDEFINIDO);
    }

    public ClasseFimEnum resolveClasseFim(HospedeMapa hm){
        return mapaClasseFim.entrySet()
                .stream()
                .filter(m -> m.getValue().test(hm))
                .map(m -> m.getKey())
                .findFirst()
                .orElse(ClasseFimEnum.INDEFINIDO);
    }
}
