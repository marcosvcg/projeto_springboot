package br.ucsal.academico.notas.service;

import br.ucsal.academico.notas.domain.Curso;
import br.ucsal.academico.notas.domain.Disciplina;
import br.ucsal.academico.notas.domain.Matricula;
import br.ucsal.academico.notas.model.DisciplinaDTO;
import br.ucsal.academico.notas.repos.CursoRepository;
import br.ucsal.academico.notas.repos.DisciplinaRepository;
import br.ucsal.academico.notas.repos.MatriculaRepository;
import br.ucsal.academico.notas.util.NotFoundException;
import br.ucsal.academico.notas.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;

    public DisciplinaService(final DisciplinaRepository disciplinaRepository,
            final CursoRepository cursoRepository, final MatriculaRepository matriculaRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public List<DisciplinaDTO> findAll() {
        final List<Disciplina> disciplinas = disciplinaRepository.findAll(Sort.by("id"));
        return disciplinas.stream()
                .map((disciplina) -> mapToDTO(disciplina, new DisciplinaDTO()))
                .toList();
    }

    public DisciplinaDTO get(final Long id) {
        return disciplinaRepository.findById(id)
                .map((disciplina) -> mapToDTO(disciplina, new DisciplinaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final DisciplinaDTO disciplinaDTO) {
        final Disciplina disciplina = new Disciplina();
        mapToEntity(disciplinaDTO, disciplina);
        return disciplinaRepository.save(disciplina).getId();
    }

    public void update(final Long id, final DisciplinaDTO disciplinaDTO) {
        final Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(disciplinaDTO, disciplina);
        disciplinaRepository.save(disciplina);
    }

    public void delete(final Long id) {
        disciplinaRepository.deleteById(id);
    }

    private DisciplinaDTO mapToDTO(final Disciplina disciplina, final DisciplinaDTO disciplinaDTO) {
        disciplinaDTO.setId(disciplina.getId());
        disciplinaDTO.setNome(disciplina.getNome());
        disciplinaDTO.setCurso(disciplina.getCurso() == null ? null : disciplina.getCurso().getId());
        return disciplinaDTO;
    }

    private Disciplina mapToEntity(final DisciplinaDTO disciplinaDTO, final Disciplina disciplina) {
        disciplina.setNome(disciplinaDTO.getNome());
        final Curso curso = disciplinaDTO.getCurso() == null ? null : cursoRepository.findById(disciplinaDTO.getCurso())
                .orElseThrow(() -> new NotFoundException("curso not found"));
        disciplina.setCurso(curso);
        return disciplina;
    }

    public boolean nomeExists(final String nome) {
        return disciplinaRepository.existsByNomeIgnoreCase(nome);
    }

    public String getReferencedWarning(final Long id) {
        final Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Matricula disciplinaMatricula = matriculaRepository.findFirstByDisciplina(disciplina);
        if (disciplinaMatricula != null) {
            return WebUtils.getMessage("disciplina.matricula.disciplina.referenced", disciplinaMatricula.getId());
        }
        return null;
    }

}
