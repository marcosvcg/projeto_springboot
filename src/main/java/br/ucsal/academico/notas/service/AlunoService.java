package br.ucsal.academico.notas.service;

import br.ucsal.academico.notas.domain.Aluno;
import br.ucsal.academico.notas.domain.Curso;
import br.ucsal.academico.notas.domain.Matricula;
import br.ucsal.academico.notas.model.AlunoDTO;
import br.ucsal.academico.notas.repos.AlunoRepository;
import br.ucsal.academico.notas.repos.CursoRepository;
import br.ucsal.academico.notas.repos.MatriculaRepository;
import br.ucsal.academico.notas.util.NotFoundException;
import br.ucsal.academico.notas.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;

    public AlunoService(final AlunoRepository alunoRepository,
            final CursoRepository cursoRepository, final MatriculaRepository matriculaRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
    }
    
    // task 3
    public List<AlunoDTO> getAllAlunosByCursoId(final Long id) {
    	final List<Aluno> alunos = alunoRepository.findAll(Sort.by("id"));
        return alunos.stream()
                .filter(aluno -> aluno.getCurso().getId().equals(id))
                .map((aluno) -> mapToDTO(aluno, new AlunoDTO()))
                .toList();
    }

    public List<AlunoDTO> findAll() {
        final List<Aluno> alunos = alunoRepository.findAll(Sort.by("id"));
        return alunos.stream()
                .map((aluno) -> mapToDTO(aluno, new AlunoDTO()))
                .toList();
    }

    public AlunoDTO get(final Long id) {
        return alunoRepository.findById(id)
                .map((aluno) -> mapToDTO(aluno, new AlunoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AlunoDTO alunoDTO) {
        final Aluno aluno = new Aluno();
        mapToEntity(alunoDTO, aluno);
        return alunoRepository.save(aluno).getId();
    }

    public void update(final Long id, final AlunoDTO alunoDTO) {
        final Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(alunoDTO, aluno);
        alunoRepository.save(aluno);
    }

    public void delete(final Long id) {
        alunoRepository.deleteById(id);
    }

    private AlunoDTO mapToDTO(final Aluno aluno, final AlunoDTO alunoDTO) {
        alunoDTO.setId(aluno.getId());
        alunoDTO.setNome(aluno.getNome());
        alunoDTO.setEmail(aluno.getEmail());
        alunoDTO.setCurso(aluno.getCurso() == null ? null : aluno.getCurso().getId());
        return alunoDTO;
    }

    private Aluno mapToEntity(final AlunoDTO alunoDTO, final Aluno aluno) {
        aluno.setNome(alunoDTO.getNome());
        aluno.setEmail(alunoDTO.getEmail());
        final Curso curso = alunoDTO.getCurso() == null ? null : cursoRepository.findById(alunoDTO.getCurso())
                .orElseThrow(() -> new NotFoundException("curso not found"));
        aluno.setCurso(curso);
        return aluno;
    }

    public boolean emailExists(final String email) {
        return alunoRepository.existsByEmailIgnoreCase(email);
    }

    public String getReferencedWarning(final Long id) {
        final Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Matricula alunoMatricula = matriculaRepository.findFirstByAluno(aluno);
        if (alunoMatricula != null) {
            return WebUtils.getMessage("aluno.matricula.aluno.referenced", alunoMatricula.getId());
        }
        return null;
    }

}
