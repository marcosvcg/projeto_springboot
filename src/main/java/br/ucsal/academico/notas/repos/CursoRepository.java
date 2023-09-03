package br.ucsal.academico.notas.repos;

import br.ucsal.academico.notas.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByNomeIgnoreCase(String nome);

}
