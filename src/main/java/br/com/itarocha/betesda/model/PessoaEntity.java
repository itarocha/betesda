package br.com.itarocha.betesda.model;

import br.com.itarocha.betesda.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="pessoa")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PessoaEntity extends UserDateAudit implements Serializable {
	
	private static final long serialVersionUID = 615363304475476825L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull(message="Nome é obrigatório")
	@Size(min = 3, max = 64, message="Nome deve ter entre 2 a 64 caracteres")
	private String nome;

	@NotNull(message="Data de Nascimento é obrigatório")
	private LocalDate dataNascimento;

	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private EstadoCivil estadoCivil;
	
	@Size(min = 0, max = 11, message="CPF deve ter 11 caracteres")
	private String cpf;
	
	@Size(min = 15, max = 15, message="Cartão do SUS deve ter 15 caracteres")
	private String cartaoSus;

	@Size(max = 32, message="RG deve ter até 32 caracteres")
	private String rg;
	
	@Column(length=64)
	private String naturalidadeCidade;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 2)
	private UnidadeFederacao naturalidadeUf;
	
	@Column(length=64)
	private String nacionalidade;
	
	@Column(length=64)
	private String profissao;

	@Valid
	@ManyToOne()
	@NotNull(message="Endereço deve ser preenchido")
	private EnderecoEntity endereco;
	
	@Size(max = 16, message="Telefone não pode ter mais que 16 caracteres")
	private String telefone;
	
	@Size(max = 16, message="Telefone 2 não pode ter mais que 16 caracteres")
	private String telefone2;

	@jakarta.validation.constraints.Email(message="Email inválido")
	@Size(max = 64, message="Email deve ter no máximo 64 caracteres")
	private String email;
	
	@Lob 
	@Basic(fetch=FetchType.LAZY)
	private String observacoes;

	public PessoaEntity(){
		this.endereco = new EnderecoEntity();
		this.sexo = Sexo.F;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCartaoSus() {
		return cartaoSus;
	}

	public void setCartaoSus(String cartaoSus) {
		this.cartaoSus = cartaoSus;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getNaturalidadeCidade() {
		return naturalidadeCidade;
	}

	public void setNaturalidadeCidade(String naturalidadeCidade) {
		this.naturalidadeCidade = naturalidadeCidade;
	}

	public UnidadeFederacao getNaturalidadeUf() {
		return naturalidadeUf;
	}

	public void setNaturalidadeUf(UnidadeFederacao naturalidadeUf) {
		this.naturalidadeUf = naturalidadeUf;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public EnderecoEntity getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoEntity endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	@Override
    public String toString() {
        return String.format("Pessoa[id=%d, nome='%s']",id, nome);
    }	

}
