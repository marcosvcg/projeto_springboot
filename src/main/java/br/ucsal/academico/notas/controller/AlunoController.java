package br.ucsal.academico.notas.controller;

import br.ucsal.academico.notas.domain.Curso;
import br.ucsal.academico.notas.model.AlunoDTO;
import br.ucsal.academico.notas.repos.CursoRepository;
import br.ucsal.academico.notas.service.AlunoService;
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
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final CursoRepository cursoRepository;

    public AlunoController(final AlunoService alunoService, final CursoRepository cursoRepository) {
        this.alunoService = alunoService;
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
        model.addAttribute("alunos", alunoService.findAll());
        return "aluno/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("aluno") final AlunoDTO alunoDTO) {
        return "aluno/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("aluno") @Valid final AlunoDTO alunoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && alunoService.emailExists(alunoDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.aluno.email");
        }
        if (bindingResult.hasErrors()) {
            return "aluno/add";
        }
        alunoService.create(alunoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("aluno.create.success"));
        return "redirect:/alunos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("aluno", alunoService.get(id));
        return "aluno/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("aluno") @Valid final AlunoDTO alunoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final AlunoDTO currentAlunoDTO = alunoService.get(id);
        if (!bindingResult.hasFieldErrors("email") &&
                !alunoDTO.getEmail().equalsIgnoreCase(currentAlunoDTO.getEmail()) &&
                alunoService.emailExists(alunoDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.aluno.email");
        }
        if (bindingResult.hasErrors()) {
            return "aluno/edit";
        }
        alunoService.update(id, alunoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("aluno.update.success"));
        return "redirect:/alunos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = alunoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            alunoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("aluno.delete.success"));
        }
        return "redirect:/alunos";
    }

}
