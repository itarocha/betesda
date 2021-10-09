package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.domain.*;

import java.util.List;

public interface EntidadeUseCase {
    Entidade create(Entidade model);
    void remove(Long id);
    Entidade update(Entidade model);
    Entidade find(Long id);
    List<Entidade> findAll();
    List<Entidade> consultar(String texto);
    List<ItemDictionary> listSelect();
}
