package br.ucsal.academico.notas.rest;

import br.ucsal.academico.notas.model.AlunoDTO;
import br.ucsal.academico.notas.service.AlunoService;
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
@RequestMapping(value = "/api/alunos", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlunoResource {

    private final AlunoService alunoService;

    public AlunoResource(final AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> getAllAlunos() {
        return ResponseEntity.ok(alunoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> getAluno(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(alunoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAluno(@RequestBody @Valid final AlunoDTO alunoDTO) {
        final Long createdId = alunoService.create(alunoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAluno(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AlunoDTO alunoDTO) {
        alunoService.update(id, alunoDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAluno(@PathVariable(name = "id") final Long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
