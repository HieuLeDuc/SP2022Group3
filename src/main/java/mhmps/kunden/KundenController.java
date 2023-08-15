package mhmps.kunden;

import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import mhmps.mitarbeiter.EditForm;

@PreAuthorize("isAuthenticated()")
@Controller
public class KundenController {

	private final KundenManagement kundenManagement;

	KundenController(KundenManagement kundenManagement) {

		Assert.notNull(kundenManagement, "KundenManagement must not be null!");

		this.kundenManagement = kundenManagement;
	}

//Ausgeben der Kundendaten in einer Liste
	@GetMapping("/kundenliste")
	String customers(Model model) {

		model.addAttribute("kundenList", kundenManagement.findAll());

		return "kundenliste";
	}

//neuen Kunden anlegen
	@PostMapping("/kundeAnlegen")
	String registerNew(@Valid KundenForm form, Errors result) {

		if (result.hasErrors()) {
			return "kundeAnlegen";
		}

		kundenManagement.createKunde(form);

		return "redirect:/kundenliste";
	}

	@GetMapping("/kundeAnlegen")
	String register(Model model, KundenForm form) {
		return "kundeAnlegen";
	}

//Kundendaten bearbeiten
	@GetMapping("/kundeBearbeiten/{id}")
	String edit(@PathVariable Long id, Model model, EditForm form) {

		model.addAttribute("kunde", kundenManagement.getKundeById(id));

		return "kundeBearbeiten";
	}

	@PostMapping("/kundenliste/edit/{id}")
	@PreAuthorize("isAuthenticated()")
	String edit(@PathVariable Long id, @Valid EditForm form, Errors result) {
		if (result.hasErrors()) {
			return "kundeBearbeiten";
		}

		kundenManagement.editKunde(form, id);

		return "redirect:/kundenliste";
	}

//Kunden löschen
	@GetMapping("/kundenliste/{id}")
	public String RemoveKunde(@PathVariable Long id) {
		kundenManagement.deleteKunde(id);
		return "redirect:/kundenliste";
	}

}