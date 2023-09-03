package br.ucsal.academico.notas.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class AlunoDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nome;

    @NotNull
    @Size(max = 300)
    private String email;

    @NotNull
    private Long curso;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Long getCurso() {
        return curso;
    }

    public void setCurso(final Long curso) {
        this.curso = curso;
    }

}
