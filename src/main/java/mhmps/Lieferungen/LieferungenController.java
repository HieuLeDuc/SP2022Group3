package mhmps.Lieferungen;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@PreAuthorize("isAuthenticated()")
@Controller
public class LieferungenController {
	private LieferungenData lieferungenData;

	public LieferungenController(LieferungenData lieferungenData) {
		super();
		this.lieferungenData = lieferungenData;
	}
//Ausgabe der Lieferungen als Tabelle
	@GetMapping("/lieferungens")
	public String listLieferungens(Model model) {
		model.addAttribute("lieferungens", lieferungenData.getAllLieferungens());
		return "lieferungen";
	}

//Anlegen einer neuen Lieferung
	@GetMapping("/lieferungens/new")
	public String createLieferungenForm(Model model) {

		Lieferungen lieferungen = new Lieferungen();
		model.addAttribute("lieferungen", lieferungen);
		return "create_lieferungen";

	}

	@PostMapping("/lieferungens")
	public String saveLieferungen(@ModelAttribute("lieferungen") Lieferungen lieferungen) {
		lieferungenData.saveLieferungen(lieferungen);
		return "redirect:/lieferungens";
	}

//Bearbeitung einer Lieferung
	@GetMapping("/lieferungens/edit/{id}")
	public String editLieferungenForm(@PathVariable Long id, Model model) {
		model.addAttribute("lieferungen", lieferungenData.getLieferungenById(id));
		return "edit_lieferungen";
	}

	@PostMapping("/lieferungens/{id}")
	public String updateLieferungen(@PathVariable Long id, @ModelAttribute("lieferungen") Lieferungen lieferungen,
			Model model) {

		Lieferungen existingLieferungen = lieferungenData.getLieferungenById(id);
		existingLieferungen.setId(id);
		existingLieferungen.setDatum(lieferungen.getDatum());
		existingLieferungen.setFahrzeug(lieferungen.getFahrzeug());
		existingLieferungen.setLieferadresse(lieferungen.getLieferadresse());
		existingLieferungen.setGemietet(lieferungen.getGemietet());
		existingLieferungen.setUhrzeit(lieferungen.getUhrzeit());

		lieferungenData.updateLieferungen(existingLieferungen);
		return "redirect:/lieferungens";
	}

//Löschen einer Lieferung
	@GetMapping("/lieferungens/{id}")
	public String deleteMoebel(@PathVariable Long id) {
		lieferungenData.deleteLieferungenById(id);
		return "redirect:/lieferungens";
	}

}
