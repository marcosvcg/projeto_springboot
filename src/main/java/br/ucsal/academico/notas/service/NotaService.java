package br.ucsal.academico.notas.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import br.ucsal.academico.notas.domain.Matricula;
import br.ucsal.academico.notas.domain.Nota;
import br.ucsal.academico.notas.model.NotaDTO;
import br.ucsal.academico.notas.repos.MatriculaRepository;
import br.ucsal.academico.notas.repos.NotaRepository;
import br.ucsal.academico.notas.util.NotFoundException;


@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final MatriculaRepository matriculaRepository;

    public NotaService(final NotaRepository notaRepository,
            final MatriculaRepository matriculaRepository) {
        this.notaRepository = notaRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public List<NotaDTO> getAllNotasByMatriculaId(final Long id) {
    	final List<Nota> notas = notaRepository.findAll(Sort.by("id"));
        return notas.stream()
                .filter(nota -> nota.getMatricula().getId().equals(id))
                .map((nota) -> mapToDTO(nota, new NotaDTO()))
                .toList();
    }
    
    public List<NotaDTO> findAll() {
        final List<Nota> notas = notaRepository.findAll(Sort.by("id"));
        return notas.stream()
                .map((nota) -> mapToDTO(nota, new NotaDTO()))
                .toList();
    }

    public NotaDTO get(final Long id) {
        return notaRepository.findById(id)
                .map((nota) -> mapToDTO(nota, new NotaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final NotaDTO notaDTO) {
        final Nota nota = new Nota();
        mapToEntity(notaDTO, nota);
        return notaRepository.save(nota).getId();
    }

    public void update(final Long id, final NotaDTO notaDTO) {
        final Nota nota = notaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(notaDTO, nota);
        notaRepository.save(nota);
    }

    public void delete(final Long id) {
        notaRepository.deleteById(id);
    }

    private NotaDTO mapToDTO(final Nota nota, final NotaDTO notaDTO) {
        notaDTO.setId(nota.getId());
        notaDTO.setValor(nota.getValor());
        notaDTO.setMatricula(nota.getMatricula() == null ? null : nota.getMatricula().getId());
        return notaDTO;
    }

    private Nota mapToEntity(final NotaDTO notaDTO, final Nota nota) {
        nota.setValor(notaDTO.getValor());
        final Matricula matricula = notaDTO.getMatricula() == null ? null : matriculaRepository.findById(notaDTO.getMatricula())
                .orElseThrow(() -> new NotFoundException("matricula not found"));
        nota.setMatricula(matricula);
        return nota;
    }

}
