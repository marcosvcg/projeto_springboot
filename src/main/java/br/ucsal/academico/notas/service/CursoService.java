package br.ucsal.academico.notas.service;

import br.ucsal.academico.notas.domain.Aluno;
import br.ucsal.academico.notas.domain.Curso;
import br.ucsal.academico.notas.domain.Disciplina;
import br.ucsal.academico.notas.model.CursoDTO;
import br.ucsal.academico.notas.repos.AlunoRepository;
import br.ucsal.academico.notas.repos.CursoRepository;
import br.ucsal.academico.notas.repos.DisciplinaRepository;
import br.ucsal.academico.notas.util.NotFoundException;
import br.ucsal.academico.notas.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public CursoService(final CursoRepository cursoRepository,
            final AlunoRepository alunoRepository,
            final DisciplinaRepository disciplinaRepository) {
        this.cursoRepository = cursoRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<CursoDTO> findAll() {
        final List<Curso> cursos = cursoRepository.findAll(Sort.by("id"));
        return cursos.stream()
                .map((curso) -> mapToDTO(curso, new CursoDTO()))
                .toList();
    }

    public CursoDTO get(final Long id) {
        return cursoRepository.findById(id)
                .map((curso) -> mapToDTO(curso, new CursoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CursoDTO cursoDTO) {
        final Curso curso = new Curso();
        mapToEntity(cursoDTO, curso);
        return cursoRepository.save(curso).getId();
    }

    public void update(final Long id, final CursoDTO cursoDTO) {
        final Curso curso = cursoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cursoDTO, curso);
        cursoRepository.save(curso);
    }

    public void delete(final Long id) {
        cursoRepository.deleteById(id);
    }

    private CursoDTO mapToDTO(final Curso curso, final CursoDTO cursoDTO) {
        cursoDTO.setId(curso.getId());
        cursoDTO.setNome(curso.getNome());
        return cursoDTO;
    }

    private Curso mapToEntity(final CursoDTO cursoDTO, final Curso curso) {
        curso.setNome(cursoDTO.getNome());
        return curso;
    }

    public boolean nomeExists(final String nome) {
        return cursoRepository.existsByNomeIgnoreCase(nome);
    }

    public String getReferencedWarning(final Long id) {
        final Curso curso = cursoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Aluno cursoAluno = alunoRepository.findFirstByCurso(curso);
        if (cursoAluno != null) {
            return WebUtils.getMessage("curso.aluno.curso.referenced", cursoAluno.getId());
        }
        final Disciplina cursoDisciplina = disciplinaRepository.findFirstByCurso(curso);
        if (cursoDisciplina != null) {
            return WebUtils.getMessage("curso.disciplina.curso.referenced", cursoDisciplina.getId());
        }
        return null;
    }

}
