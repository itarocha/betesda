package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.domain.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository {

    Pessoa save(Pessoa model);

    Optional<Pessoa> findById(Long id);

    void delete(Pessoa model);

    void deleteById(Long id);

    List<Pessoa> findAllOrderByNome();

    List<Pessoa> findAllLikeNomeLowerCase(String texto);

    List<Pessoa> getListaOutrosComCpf(Long id, String cpf);

    List<Pessoa> getListaOutrosComRg(Long id, String rg);

    List<Pessoa> getListaOutrosComCartaoSus(Long id, String cartaoSus);
}
