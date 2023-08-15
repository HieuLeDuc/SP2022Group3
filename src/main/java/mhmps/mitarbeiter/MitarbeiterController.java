package mhmps.mitarbeiter;

import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import mhmps.mitarbeiter.Mitarbeiter.MitarbeiterIdentifier;

@Controller
public class MitarbeiterController {

	private final MitarbeiterManagement mitarbeiterManagement;

	MitarbeiterController(MitarbeiterManagement mitarbeiterManagement) {

		Assert.notNull(mitarbeiterManagement, "MitarbeiterManagement must not be null!");

		this.mitarbeiterManagement = mitarbeiterManagement;
	}
//Mitabeiterdaten in Form einer Liste
	@GetMapping("/mitarbeiterliste")
	@PreAuthorize("hasRole('BOSS')")
	String customers(Model model) {

		model.addAttribute("mitarbeiterList", mitarbeiterManagement.findAll());

		return "mitarbeiterliste";
	}

//neuen Mitarbeiter anlegen
	@PostMapping("/mitarbeiterFreigeben")
	@PreAuthorize("hasRole('BOSS')")
	String registerNew(@Valid MitarbeiterForm form, Errors result) {

		if (result.hasErrors()) {
			return "mitarbeiterFreigeben";
		}

		mitarbeiterManagement.createMitarbeiter(form);

		return "redirect:/mitarbeiterliste";
	}

	@GetMapping("/mitarbeiterFreigeben")
	@PreAuthorize("hasRole('BOSS')")
	String register(Model model, MitarbeiterForm form) {
		return "mitarbeiterFreigeben";
	}

//Mitarbeiter bearbeiten
	@GetMapping("/mitarbeiterBearbeiten/{id}")
	@PreAuthorize("hasRole('BOSS')")
	String edit(@PathVariable MitarbeiterIdentifier id, Model model, EditForm form) {

		model.addAttribute("mitarbeiter", mitarbeiterManagement.getMitarbeiterById(id));

		return "mitarbeiterBearbeiten";
	}

	@PostMapping("/mitarbeiterliste/edit/{id}")
	@PreAuthorize("hasRole('BOSS')")
	String edit(@PathVariable MitarbeiterIdentifier id, @Valid EditForm form, Errors result) {
		if (result.hasErrors()) {
			return "mitarbeiterBearbeiten";
		}

		mitarbeiterManagement.editMitarbeiter(form, id);

		return "redirect:/mitarbeiterliste";
	}

//Mitarbeiter löschen falls es nicht der Boss ist
	@GetMapping("/mitarbeiterliste/{id}")
	@PreAuthorize("hasRole('BOSS')")
	public String RemoveMitarbeiter(@PathVariable MitarbeiterIdentifier id) {
		if (!mitarbeiterManagement.getMitarbeiterById(id).isBoss())
			mitarbeiterManagement.deleteMitarbeiter(id);
		return "redirect:/mitarbeiterliste";
	}
}
