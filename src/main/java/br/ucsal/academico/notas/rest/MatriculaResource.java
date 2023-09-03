package br.ucsal.academico.notas.rest;

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

import br.ucsal.academico.notas.model.MatriculaDTO;
import br.ucsal.academico.notas.model.NotaDTO;
import br.ucsal.academico.notas.service.MatriculaService;
import br.ucsal.academico.notas.service.NotaService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/api/matriculas", produces = MediaType.APPLICATION_JSON_VALUE)
public class MatriculaResource {

    private final MatriculaService matriculaService;
    private final NotaService notaService;


    public MatriculaResource(final MatriculaService matriculaService, final NotaService notaService) {
        this.matriculaService = matriculaService;
        this.notaService = notaService;
    }
    
    @GetMapping("/{id}/notas")
    public ResponseEntity<List<NotaDTO>> getAllNotasByMatricula(@PathVariable(name = "id") final Long id) {
    	return ResponseEntity.ok(notaService.getAllNotasByMatriculaId(id));
    }
    
    @GetMapping
    public ResponseEntity<List<MatriculaDTO>> getAllMatriculas() {
        return ResponseEntity.ok(matriculaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDTO> getMatricula(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(matriculaService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMatricula(
            @RequestBody @Valid final MatriculaDTO matriculaDTO) {
        final Long createdId = matriculaService.create(matriculaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMatricula(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MatriculaDTO matriculaDTO) {
        matriculaService.update(id, matriculaDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMatricula(@PathVariable(name = "id") final Long id) {
        matriculaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
