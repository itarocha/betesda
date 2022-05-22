package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.domain.Endereco;

public interface EnderecoRepository {
    Endereco save(Endereco endereco);
}
