package br.ucsal.academico.notas.repos;

import br.ucsal.academico.notas.domain.Aluno;
import br.ucsal.academico.notas.domain.Disciplina;
import br.ucsal.academico.notas.domain.Matricula;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    Matricula findFirstByAluno(Aluno aluno);

    Matricula findFirstByDisciplina(Disciplina disciplina);
    
}
