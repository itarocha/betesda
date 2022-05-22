package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.*;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.*;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.HospedagemMapper;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.core.ports.out.HospedagemRepository;
import br.com.itarocha.betesda.domain.Hospedagem;
import br.com.itarocha.betesda.domain.HospedagemNew;
import br.com.itarocha.betesda.domain.HospedeNew;
import br.com.itarocha.betesda.domain.enums.TipoUtilizacaoHospedagemEnum;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospedagemRepositoryImpl implements HospedagemRepository {

	private final DSLContext create;

	private final HospedagemJpaRepository hospedagemRepo;
	private final EntidadeJpaRepository entidadeRepo;
	private final EncaminhadorJpaRepository encaminhadorRepo;
	private final DestinacaoHospedagemJpaRepository destinacaoHospedagemRepo;
	private final PessoaJpaRepository pessoaRepo;
	private final TipoHospedeJpaRepository tipoHospedeRepo;
	private final HospedeJpaRepository hospedeRepo;
	private final QuartoJpaRepository quartoRepo;
	private final LeitoJpaRepository leitoRepo;
	private final HospedeLeitoJpaRepository hospedeLeitoRepo;
	private final TipoServicoJpaRepository tipoServicoRepo;
	private final HospedagemTipoServicoJpaRepository hospedagemTipoServicoRepo;
	private final HospedagemMapper hospedagemMapper;

	@Override
	public Hospedagem save(Hospedagem model) {
		return null;
	}

	public Hospedagem save(HospedagemNew model) {
		try {
			HospedagemEntity toSave = HospedagemEntity.builder().build();
			toSave.setDataEntrada(model.getDataEntrada());
			toSave.setDataPrevistaSaida(model.getDataPrevistaSaida());
			toSave.setTipoUtilizacao(model.getTipoUtilizacao());
			toSave.setObservacoes(model.getObservacoes());

			Optional<EntidadeEntity> entidadeOpt = entidadeRepo.findById(model.getEntidadeId());
			if (entidadeOpt.isPresent()){
				toSave.setEntidade(entidadeOpt.get());
			}

			Optional<EncaminhadorEntity> encaminhadorOpt = encaminhadorRepo.findById(model.getEncaminhadorId());
			if (encaminhadorOpt.isPresent()){
				toSave.setEncaminhador(encaminhadorOpt.get());
			}

			Optional<DestinacaoHospedagemEntity> destinacaoHospedagemOpt = destinacaoHospedagemRepo.findById(model.getDestinacaoHospedagemId());
			if (destinacaoHospedagemOpt.isPresent()){
				toSave.setDestinacaoHospedagem(destinacaoHospedagemOpt.get());
			}

			HospedagemEntity hospedagemSaved = hospedagemRepo.save(toSave);

			for (HospedeNew hvo: model.getHospedes()) {
				HospedeEntity hospedeToSave = HospedeEntity.builder().build();
				hospedeToSave.setHospedagem(hospedagemSaved);

				Optional<PessoaEntity> pessoaOpt = pessoaRepo.findById(hvo.getPessoaId());
				if (pessoaOpt.isPresent()){
					hospedeToSave.setPessoa(pessoaOpt.get());
				}

				Optional<TipoHospedeEntity> tipoHospedeOpt = tipoHospedeRepo.findById(hvo.getTipoHospedeId());
				if (tipoHospedeOpt.isPresent()){
					hospedeToSave.setTipoHospede(tipoHospedeOpt.get());
				}
				HospedeEntity hospedeSaved = hospedeRepo.save(hospedeToSave);

				hospedagemSaved.getHospedes().add(hospedeSaved);

				if ((hvo.getAcomodacao() != null) && (TipoUtilizacaoHospedagemEnum.T.equals(toSave.getTipoUtilizacao())) ) {
					//TODO: Tem um código igual no transferir. Refatorar criar método
					Optional<QuartoEntity> quartoOpt = quartoRepo.findById(hvo.getAcomodacao().getQuartoId());
					Optional<LeitoEntity> leitoOpt = leitoRepo.findById(hvo.getAcomodacao().getLeitoId());

					if (quartoOpt.isPresent() && leitoOpt.isPresent()) {
						HospedeLeitoEntity hospedeLeitoToSave = HospedeLeitoEntity.builder().build();
						hospedeLeitoToSave.setHospede(hospedeToSave);
						hospedeLeitoToSave.setDataEntrada(model.getDataEntrada());
						hospedeLeitoToSave.setDataSaida(model.getDataPrevistaSaida());
						hospedeLeitoToSave.setQuarto(quartoOpt.get());
						hospedeLeitoToSave.setLeito(leitoOpt.get());

						HospedeLeitoEntity hospedeLeitoSaved = hospedeLeitoRepo.save(hospedeLeitoToSave);
						hospedeSaved.getLeitos().add(hospedeLeitoSaved);
					}
				}
			}
			if ((model.getServicos().length > 0) && (TipoUtilizacaoHospedagemEnum.P.equals(model.getTipoUtilizacao())) ) {
				for (Long tipoServicoId : model.getServicos()) {
					Optional<TipoServicoEntity> tipoServicoOpt = tipoServicoRepo.findById(tipoServicoId);
					if (tipoServicoOpt.isPresent()) {
						HospedagemTipoServicoEntity hospedagemTipoServicoToSave = HospedagemTipoServicoEntity.builder().build();
						hospedagemTipoServicoToSave.setTipoServico(tipoServicoOpt.get());
						hospedagemTipoServicoToSave.setHospedagem(hospedagemSaved);
						HospedagemTipoServicoEntity hospedagemTipoServicoSaved = hospedagemTipoServicoRepo.save(hospedagemTipoServicoToSave);
						hospedagemSaved.getServicos().add(hospedagemTipoServicoSaved);
					}
				}
			}

			return hospedagemMapper.toModel(hospedagemSaved);
		} catch ( DataIntegrityViolationException e) {
			throw new IntegridadeException("Falha de integridade ao tentar gravar Hospagem"
					, e.getMostSpecificCause().getMessage());
		}
	}

	@Override
	public Optional<Hospedagem> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public void delete(Hospedagem model) {

	}

	@Override
	public void deleteById(Long id) {

	}

	@Override
	public Hospedagem findHospedagemByHospedagemId(Long hospedagemId) {
		return null;
	}

	@Override
	public Hospedagem updateDataPrevistaSaida(Long hospedagemId, LocalDate dataRenovacao) {
		return null;
	}

}
