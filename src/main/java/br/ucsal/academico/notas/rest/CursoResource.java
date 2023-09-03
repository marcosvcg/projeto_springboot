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

import br.ucsal.academico.notas.model.AlunoDTO;
import br.ucsal.academico.notas.model.CursoDTO;
import br.ucsal.academico.notas.service.AlunoService;
import br.ucsal.academico.notas.service.CursoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/api/cursos", produces = MediaType.APPLICATION_JSON_VALUE)
public class CursoResource {

    private final CursoService cursoService;
    private final AlunoService alunoService;
    
    public CursoResource(final CursoService cursoService, final AlunoService alunoService) {
        this.cursoService = cursoService;
        this.alunoService = alunoService;
    }
    
    // task 3
    @GetMapping("/{id}/alunos")
    public ResponseEntity<List<AlunoDTO>> getAllAlunosByCurso(@PathVariable(name = "id") final Long id) {
    	return ResponseEntity.ok(alunoService.getAllAlunosByCursoId(id));
    }

    @GetMapping
    public ResponseEntity<List<CursoDTO>> getAllCursos() {
        return ResponseEntity.ok(cursoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> getCurso(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(cursoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCurso(@RequestBody @Valid final CursoDTO cursoDTO) {
        final Long createdId = cursoService.create(cursoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCurso(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CursoDTO cursoDTO) {
        cursoService.update(id, cursoDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCurso(@PathVariable(name = "id") final Long id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
