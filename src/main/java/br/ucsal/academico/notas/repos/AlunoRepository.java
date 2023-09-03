package br.ucsal.academico.notas.repos;

import br.ucsal.academico.notas.domain.Aluno;
import br.ucsal.academico.notas.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Aluno findFirstByCurso(Curso curso);

}
