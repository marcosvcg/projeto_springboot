package br.ucsal.academico.notas.rest;

import br.ucsal.academico.notas.model.DisciplinaDTO;
import br.ucsal.academico.notas.service.DisciplinaService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/disciplinas", produces = MediaType.APPLICATION_JSON_VALUE)
public class DisciplinaResource {

    private final DisciplinaService disciplinaService;

    public DisciplinaResource(final DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaDTO>> getAllDisciplinas() {
        return ResponseEntity.ok(disciplinaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> getDisciplina(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(disciplinaService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createDisciplina(
            @RequestBody @Valid final DisciplinaDTO disciplinaDTO) {
        final Long createdId = disciplinaService.create(disciplinaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDisciplina(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final DisciplinaDTO disciplinaDTO) {
        disciplinaService.update(id, disciplinaDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDisciplina(@PathVariable(name = "id") final Long id) {
        disciplinaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
