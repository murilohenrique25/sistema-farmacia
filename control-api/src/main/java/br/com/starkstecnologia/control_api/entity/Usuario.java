package br.com.starkstecnologia.control_api.entity;

import br.com.starkstecnologia.control_api.types.Role;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(schema = "sistema", name = "usuario")
@EntityListeners(AuditingEntityListener.class)
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entrega_id_seq")
    @SequenceGenerator(name = "entrega_id_seq", sequenceName = "sistema.entrega_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    private String nome;

    private boolean ativo;

    private String turno;

    private String telefone;

    private String cpf;

    @ManyToOne
    @JoinColumn(name = "id_matriz")
    private Matriz matriz;

    @Column(name = "primeiro_acesso", columnDefinition = "boolean default false")
    private Boolean primeiroAcesso = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 25)
    private Role role = Role.ROLE_CLIENTE;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "sistema", name = "usuario_permissao", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_permissao"))
    private Set<Permissao> permissoes = new HashSet<>();

    @CreatedDate
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name = "data_alteracao")
    private LocalDateTime dataAlteracao;

    @CreatedBy
    @Column(name = "criado_por")
    private String criadoPor;

    @LastModifiedBy
    @Column(name = "alterado_por")
    private String alteradoPor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public String getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(String criadoPor) {
        this.criadoPor = criadoPor;
    }

    public String getAlteradoPor() {
        return alteradoPor;
    }

    public void setAlteradoPor(String alteradoPor) {
        this.alteradoPor = alteradoPor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Matriz getMatriz() {
        return matriz;
    }

    public void setMatriz(Matriz matriz) {
        this.matriz = matriz;
    }

    public Set<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Set<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    public Boolean isPrimeiroAcesso() {
        return primeiroAcesso != null ? primeiroAcesso : false;
    }

    public void setPrimeiroAcesso(Boolean primeiroAcesso) {
        this.primeiroAcesso = primeiroAcesso;
    }
}
