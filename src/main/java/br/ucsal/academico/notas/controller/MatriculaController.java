package br.ucsal.academico.notas.controller;

import br.ucsal.academico.notas.domain.Aluno;
import br.ucsal.academico.notas.domain.Disciplina;
import br.ucsal.academico.notas.model.MatriculaDTO;
import br.ucsal.academico.notas.repos.AlunoRepository;
import br.ucsal.academico.notas.repos.DisciplinaRepository;
import br.ucsal.academico.notas.service.MatriculaService;
import br.ucsal.academico.notas.util.CustomCollectors;
import br.ucsal.academico.notas.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public MatriculaController(final MatriculaService matriculaService,
            final AlunoRepository alunoRepository,
            final DisciplinaRepository disciplinaRepository) {
        this.matriculaService = matriculaService;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("alunoValues", alunoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Aluno::getId, Aluno::getNome)));
        model.addAttribute("disciplinaValues", disciplinaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Disciplina::getId, Disciplina::getNome)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("matriculas", matriculaService.findAll());
        return "matricula/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("matricula") final MatriculaDTO matriculaDTO) {
        return "matricula/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("matricula") @Valid final MatriculaDTO matriculaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "matricula/add";
        }
        matriculaService.create(matriculaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("matricula.create.success"));
        return "redirect:/matriculas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("matricula", matriculaService.get(id));
        return "matricula/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("matricula") @Valid final MatriculaDTO matriculaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "matricula/edit";
        }
        matriculaService.update(id, matriculaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("matricula.update.success"));
        return "redirect:/matriculas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = matriculaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            matriculaService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("matricula.delete.success"));
        }
        return "redirect:/matriculas";
    }

}
