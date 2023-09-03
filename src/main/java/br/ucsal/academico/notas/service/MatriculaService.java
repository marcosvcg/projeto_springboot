package br.ucsal.academico.notas.service;

import br.ucsal.academico.notas.domain.Aluno;
import br.ucsal.academico.notas.domain.Disciplina;
import br.ucsal.academico.notas.domain.Matricula;
import br.ucsal.academico.notas.domain.Nota;
import br.ucsal.academico.notas.model.MatriculaDTO;
import br.ucsal.academico.notas.repos.AlunoRepository;
import br.ucsal.academico.notas.repos.DisciplinaRepository;
import br.ucsal.academico.notas.repos.MatriculaRepository;
import br.ucsal.academico.notas.repos.NotaRepository;
import br.ucsal.academico.notas.util.NotFoundException;
import br.ucsal.academico.notas.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final NotaRepository notaRepository;

    public MatriculaService(final MatriculaRepository matriculaRepository,
            final AlunoRepository alunoRepository, final DisciplinaRepository disciplinaRepository,
            final NotaRepository notaRepository) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.notaRepository = notaRepository;
    }
    
    public List<MatriculaDTO> findAll() {
        final List<Matricula> matriculas = matriculaRepository.findAll(Sort.by("id"));
        return matriculas.stream()
                .map((matricula) -> mapToDTO(matricula, new MatriculaDTO()))
                .toList();
    }
    
    public MatriculaDTO get(final Long id) {
        return matriculaRepository.findById(id)
                .map((matricula) -> mapToDTO(matricula, new MatriculaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MatriculaDTO matriculaDTO) {
        final Matricula matricula = new Matricula();
        mapToEntity(matriculaDTO, matricula);
        return matriculaRepository.save(matricula).getId();
    }

    public void update(final Long id, final MatriculaDTO matriculaDTO) {
        final Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(matriculaDTO, matricula);
        matriculaRepository.save(matricula);
    }

    public void delete(final Long id) {
        matriculaRepository.deleteById(id);
    }

    private MatriculaDTO mapToDTO(final Matricula matricula, final MatriculaDTO matriculaDTO) {
        matriculaDTO.setId(matricula.getId());
        matriculaDTO.setAno(matricula.getAno());
        matriculaDTO.setSemestre(matricula.getSemestre());
        matriculaDTO.setAluno(matricula.getAluno() == null ? null : matricula.getAluno().getId());
        matriculaDTO.setDisciplina(matricula.getDisciplina() == null ? null : matricula.getDisciplina().getId());
        return matriculaDTO;
    }

    private Matricula mapToEntity(final MatriculaDTO matriculaDTO, final Matricula matricula) {
        matricula.setAno(matriculaDTO.getAno());
        matricula.setSemestre(matriculaDTO.getSemestre());
        final Aluno aluno = matriculaDTO.getAluno() == null ? null : alunoRepository.findById(matriculaDTO.getAluno())
                .orElseThrow(() -> new NotFoundException("aluno not found"));
        matricula.setAluno(aluno);
        final Disciplina disciplina = matriculaDTO.getDisciplina() == null ? null : disciplinaRepository.findById(matriculaDTO.getDisciplina())
                .orElseThrow(() -> new NotFoundException("disciplina not found"));
        matricula.setDisciplina(disciplina);
        return matricula;
    }

    public String getReferencedWarning(final Long id) {
        final Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Nota matriculaNota = notaRepository.findFirstByMatricula(matricula);
        if (matriculaNota != null) {
            return WebUtils.getMessage("matricula.nota.matricula.referenced", matriculaNota.getId());
        }
        return null;
    }

}
