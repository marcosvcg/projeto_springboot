package br.ucsal.academico.notas.controller;

import br.ucsal.academico.notas.domain.Matricula;
import br.ucsal.academico.notas.model.NotaDTO;
import br.ucsal.academico.notas.repos.MatriculaRepository;
import br.ucsal.academico.notas.service.NotaService;
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
@RequestMapping("/notas")
public class NotaController {

    private final NotaService notaService;
    private final MatriculaRepository matriculaRepository;

    public NotaController(final NotaService notaService,
            final MatriculaRepository matriculaRepository) {
        this.notaService = notaService;
        this.matriculaRepository = matriculaRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("matriculaValues", matriculaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Matricula::getId, Matricula::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("notas", notaService.findAll());
        return "nota/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("nota") final NotaDTO notaDTO) {
        return "nota/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("nota") @Valid final NotaDTO notaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "nota/add";
        }
        notaService.create(notaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("nota.create.success"));
        return "redirect:/notas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("nota", notaService.get(id));
        return "nota/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("nota") @Valid final NotaDTO notaDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "nota/edit";
        }
        notaService.update(id, notaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("nota.update.success"));
        return "redirect:/notas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        notaService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("nota.delete.success"));
        return "redirect:/notas";
    }

}
