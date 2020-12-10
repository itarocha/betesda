package br.com.itarocha.betesda.domain.hospedagem;

import java.time.LocalDate;

// Substituído por HospedeMapa
@Deprecated
public class HospedeLeitoMapa {

	private String identificador;
	private String tipoUtilizacao;
	private String tipoUtilizacaoDescricao;
	private Long quartoId;
	private Integer quartoNumero;
	private Long leitoId;
	private Integer leitoNumero;
	private Long pessoaId;
	private String pessoaNome;
	private String pessoaTelefone;
	private String cidade;
	private String uf;
	private String pessoaCidadeUfOrigem;
	private LocalDate dataEntradaHospedagem;
	private LocalDate dataSaidaHospedagem;

	private LocalDate dataPrimeiraEntrada;
	private LocalDate dataUltimaEntrada;
	
	private LocalDate dataPrevistaSaida;
	private LocalDate dataEfetivaSaida;
	
	private LocalDate dataEntradaLeito;
	private LocalDate dataSaidaLeito;
	private LocalDate dataIniNoPeriodo;
	private LocalDate dataFimNoPeriodo;
	private Long hospedagemId;
	private Long hospedeId;
	private Long tipoHospedeId;
	private Boolean baixado;
	private String tipoHospedeDescricao;
	private Long destinacaoHospedagemId;
	private String	destinacaoHospedagemDescricao;
	private Integer[] dias;
	
	private CellStatusHospedagem statusHospedagem;
	
	public HospedeLeitoMapa() {}
	
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getTipoUtilizacao() {
		return tipoUtilizacao;
	}
	public void setTipoUtilizacao(String tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}
	public String getTipoUtilizacaoDescricao() {
		return tipoUtilizacaoDescricao;
	}

	public void setTipoUtilizacaoDescricao(String tipoUtilizacaoDescricao) {
		this.tipoUtilizacaoDescricao = tipoUtilizacaoDescricao;
	}

	public Long getQuartoId() {
		return quartoId;
	}
	public void setQuartoId(Long quartoId) {
		this.quartoId = quartoId;
	}
	public Integer getQuartoNumero() {
		return quartoNumero;
	}
	public void setQuartoNumero(Integer quartoNumero) {
		this.quartoNumero = quartoNumero;
	}
	public Long getLeitoId() {
		return leitoId;
	}
	public void setLeitoId(Long leitoId) {
		this.leitoId = leitoId;
	}
	public Integer getLeitoNumero() {
		return leitoNumero;
	}
	public void setLeitoNumero(Integer leitoNumero) {
		this.leitoNumero = leitoNumero;
	}
	public Long getPessoaId() {
		return pessoaId;
	}
	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}
	public String getPessoaNome() {
		return pessoaNome;
	}
	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
	}
	public String getPessoaTelefone() {
		return pessoaTelefone;
	}

	public void setPessoaTelefone(String pessoaTelefone) {
		this.pessoaTelefone = pessoaTelefone;
	}

	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getPessoaCidadeUfOrigem() {
		return pessoaCidadeUfOrigem;
	}

	public void setPessoaCidadeUfOrigem(String pessoaCidadeUfOrigem) {
		this.pessoaCidadeUfOrigem = pessoaCidadeUfOrigem;
	}

	public LocalDate getDataEntradaHospedagem() {
		return dataEntradaHospedagem;
	}
	public void setDataEntradaHospedagem(LocalDate dataEntradaHospedagem) {
		this.dataEntradaHospedagem = dataEntradaHospedagem;
	}
	public LocalDate getDataSaidaHospedagem() {
		return dataSaidaHospedagem;
	}
	public void setDataSaidaHospedagem(LocalDate dataSaidaHospedagem) {
		this.dataSaidaHospedagem = dataSaidaHospedagem;
	}
	public LocalDate getDataPrimeiraEntrada() {
		return dataPrimeiraEntrada;
	}

	public void setDataPrimeiraEntrada(LocalDate dataPrimeiraEntrada) {
		this.dataPrimeiraEntrada = dataPrimeiraEntrada;
	}

	public LocalDate getDataUltimaEntrada() {
		return dataUltimaEntrada;
	}

	public void setDataUltimaEntrada(LocalDate dataUltimaEntrada) {
		this.dataUltimaEntrada = dataUltimaEntrada;
	}

	public LocalDate getDataPrevistaSaida() {
		return dataPrevistaSaida;
	}

	public void setDataPrevistaSaida(LocalDate dataPrevistaSaida) {
		this.dataPrevistaSaida = dataPrevistaSaida;
	}

	public LocalDate getDataEfetivaSaida() {
		return dataEfetivaSaida;
	}

	public void setDataEfetivaSaida(LocalDate dataEfetivaSaida) {
		this.dataEfetivaSaida = dataEfetivaSaida;
	}

	public LocalDate getDataEntradaLeito() {
		return dataEntradaLeito;
	}
	public void setDataEntradaLeito(LocalDate dataEntradaLeito) {
		this.dataEntradaLeito = dataEntradaLeito;
	}
	public LocalDate getDataSaidaLeito() {
		return dataSaidaLeito;
	}
	public void setDataSaidaLeito(LocalDate dataSaidaLeito) {
		this.dataSaidaLeito = dataSaidaLeito;
	}
	public LocalDate getDataIniNoPeriodo() {
		return dataIniNoPeriodo;
	}
	public void setDataIniNoPeriodo(LocalDate dataIniNoPeriodo) {
		this.dataIniNoPeriodo = dataIniNoPeriodo;
	}
	public LocalDate getDataFimNoPeriodo() {
		return dataFimNoPeriodo;
	}
	public void setDataFimNoPeriodo(LocalDate dataFimNoPeriodo) {
		this.dataFimNoPeriodo = dataFimNoPeriodo;
	}
	public Long getHospedagemId() {
		return hospedagemId;
	}
	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}
	public Long getHospedeId() {
		return hospedeId;
	}
	public void setHospedeId(Long hospedeId) {
		this.hospedeId = hospedeId;
	}
	public Long getTipoHospedeId() {
		return tipoHospedeId;
	}
	public void setTipoHospedeId(Long tipoHospedeId) {
		this.tipoHospedeId = tipoHospedeId;
	}
	public Boolean getBaixado() {
		return baixado;
	}
	public void setBaixado(Boolean baixado) {
		this.baixado = baixado;
	}
	public String getTipoHospedeDescricao() {
		return tipoHospedeDescricao;
	}
	public void setTipoHospedeDescricao(String tipoHospedeDescricao) {
		this.tipoHospedeDescricao = tipoHospedeDescricao;
	}
	public Long getDestinacaoHospedagemId() {
		return destinacaoHospedagemId;
	}
	public void setDestinacaoHospedagemId(Long destinacaoHospedagemId) {
		this.destinacaoHospedagemId = destinacaoHospedagemId;
	}
	public String getDestinacaoHospedagemDescricao() {
		return destinacaoHospedagemDescricao;
	}
	public void setDestinacaoHospedagemDescricao(String destinacaoHospedagemDescricao) {
		this.destinacaoHospedagemDescricao = destinacaoHospedagemDescricao;
	}

	public CellStatusHospedagem getStatusHospedagem() {
		return statusHospedagem;
	}

	public void setStatusHospedagem(CellStatusHospedagem statusHospedagem) {
		this.statusHospedagem = statusHospedagem;
	}

	public Integer[] getDias() {
		return dias;
	}

	public void setDias(Integer[] dias) {
		this.dias = dias;
	}
}
