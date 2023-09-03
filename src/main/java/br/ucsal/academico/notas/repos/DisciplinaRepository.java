package br.ucsal.academico.notas.repos;

import br.ucsal.academico.notas.domain.Curso;
import br.ucsal.academico.notas.domain.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    Disciplina findFirstByCurso(Curso curso);

    boolean existsByNomeIgnoreCase(String nome);

}
