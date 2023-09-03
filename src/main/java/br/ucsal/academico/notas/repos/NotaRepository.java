package br.ucsal.academico.notas.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.ucsal.academico.notas.domain.Matricula;
import br.ucsal.academico.notas.domain.Nota;


public interface NotaRepository extends JpaRepository<Nota, Long> {

    Nota findFirstByMatricula(Matricula matricula);
    
}
