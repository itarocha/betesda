package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.DestinacaoHospedagemMapper;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.repository.DestinacaoHospedagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DestinacaoHospedagemService {

	private final DestinacaoHospedagemMapper mapper;

	private final DestinacaoHospedagemRepository repositorio;

	public DestinacaoHospedagemEntity create(DestinacaoHospedagemEntity model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		repositorio.findById(id).ifPresent(model -> repositorio.delete(model));
	}
	
	public DestinacaoHospedagem find(Long id) {
		DestinacaoHospedagemEntity entity = null;
		Optional<DestinacaoHospedagemEntity> retorno = repositorio.findById(id);
		return retorno.isPresent() ? mapper.toModel(retorno.get()) : null;
	}

	public List<DestinacaoHospedagem> findAll() {
		return repositorio.findAllOrderByDescricao()
				.stream()
				.map(mapper::toModel)
				.collect(Collectors.toList());
	}

	public List<SelectValueVO> listSelect() {
		return repositorio.findAllOrderByDescricao()
				.stream()
				.map(mapper::toModel)
				.map(mapper::toSelectValueVO)
				.collect(Collectors.toList());
	}

}
