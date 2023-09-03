package br.ucsal.academico.notas.model;

import jakarta.validation.constraints.NotNull;


public class MatriculaDTO {

    private Long id;

    @NotNull
    private Integer ano;

    @NotNull
    private Integer semestre;

    @NotNull
    private Long aluno;

    @NotNull
    private Long disciplina;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(final Integer ano) {
        this.ano = ano;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(final Integer semestre) {
        this.semestre = semestre;
    }

    public Long getAluno() {
        return aluno;
    }

    public void setAluno(final Long aluno) {
        this.aluno = aluno;
    }

    public Long getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(final Long disciplina) {
        this.disciplina = disciplina;
    }

}
