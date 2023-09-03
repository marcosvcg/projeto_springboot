package br.ucsal.academico.notas.controller;

import br.ucsal.academico.notas.domain.Curso;
import br.ucsal.academico.notas.model.DisciplinaDTO;
import br.ucsal.academico.notas.repos.CursoRepository;
import br.ucsal.academico.notas.service.DisciplinaService;
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
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;
    private final CursoRepository cursoRepository;

    public DisciplinaController(final DisciplinaService disciplinaService,
            final CursoRepository cursoRepository) {
        this.disciplinaService = disciplinaService;
        this.cursoRepository = cursoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("cursoValues", cursoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Curso::getId, Curso::getNome)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("disciplinas", disciplinaService.findAll());
        return "disciplina/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("disciplina") final DisciplinaDTO disciplinaDTO) {
        return "disciplina/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("disciplina") @Valid final DisciplinaDTO disciplinaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("nome") && disciplinaService.nomeExists(disciplinaDTO.getNome())) {
            bindingResult.rejectValue("nome", "Exists.disciplina.nome");
        }
        if (bindingResult.hasErrors()) {
            return "disciplina/add";
        }
        disciplinaService.create(disciplinaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("disciplina.create.success"));
        return "redirect:/disciplinas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("disciplina", disciplinaService.get(id));
        return "disciplina/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("disciplina") @Valid final DisciplinaDTO disciplinaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final DisciplinaDTO currentDisciplinaDTO = disciplinaService.get(id);
        if (!bindingResult.hasFieldErrors("nome") &&
                !disciplinaDTO.getNome().equalsIgnoreCase(currentDisciplinaDTO.getNome()) &&
                disciplinaService.nomeExists(disciplinaDTO.getNome())) {
            bindingResult.rejectValue("nome", "Exists.disciplina.nome");
        }
        if (bindingResult.hasErrors()) {
            return "disciplina/edit";
        }
        disciplinaService.update(id, disciplinaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("disciplina.update.success"));
        return "redirect:/disciplinas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = disciplinaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            disciplinaService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("disciplina.delete.success"));
        }
        return "redirect:/disciplinas";
    }

}
