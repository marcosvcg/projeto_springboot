package br.ucsal.academico.notas.controller;

import br.ucsal.academico.notas.model.CursoDTO;
import br.ucsal.academico.notas.service.CursoService;
import br.ucsal.academico.notas.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(final CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("cursos", cursoService.findAll());
        return "curso/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("curso") final CursoDTO cursoDTO) {
        return "curso/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("curso") @Valid final CursoDTO cursoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("nome") && cursoService.nomeExists(cursoDTO.getNome())) {
            bindingResult.rejectValue("nome", "Exists.curso.nome");
        }
        if (bindingResult.hasErrors()) {
            return "curso/add";
        }
        cursoService.create(cursoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("curso.create.success"));
        return "redirect:/cursos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("curso", cursoService.get(id));
        return "curso/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("curso") @Valid final CursoDTO cursoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final CursoDTO currentCursoDTO = cursoService.get(id);
        if (!bindingResult.hasFieldErrors("nome") &&
                !cursoDTO.getNome().equalsIgnoreCase(currentCursoDTO.getNome()) &&
                cursoService.nomeExists(cursoDTO.getNome())) {
            bindingResult.rejectValue("nome", "Exists.curso.nome");
        }
        if (bindingResult.hasErrors()) {
            return "curso/edit";
        }
        cursoService.update(id, cursoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("curso.update.success"));
        return "redirect:/cursos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = cursoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            cursoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("curso.delete.success"));
        }
        return "redirect:/cursos";
    }

}
