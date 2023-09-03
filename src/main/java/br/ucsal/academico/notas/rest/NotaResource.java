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
import br.ucsal.academico.notas.model.NotaDTO;
import br.ucsal.academico.notas.service.NotaService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/api/notas", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotaResource {

    private final NotaService notaService;

    public NotaResource(final NotaService notaService) {
        this.notaService = notaService;
    }

    @GetMapping
    public ResponseEntity<List<NotaDTO>> getAllNotas() {
        return ResponseEntity.ok(notaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaDTO> getNota(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(notaService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createNota(
            @RequestBody @Valid final NotaDTO NotaDTO) {
        final Long createdId = notaService.create(NotaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNota(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final NotaDTO NotaDTO) {
        notaService.update(id, NotaDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteNota(@PathVariable(name = "id") final Long id) {
        notaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
