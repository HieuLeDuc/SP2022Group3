package mhmps.bestellung;

//import mhmps.kunden.Kunde.KundenIdentifier;
import com.lowagie.text.DocumentException;

import mhmps.Lieferungen.Lieferungen;
import mhmps.Lieferungen.LieferungenData;
import mhmps.Lieferungen.LieferungenRepository;
import mhmps.kunden.KundenManagement;
import mhmps.moebel.BundleRepository;
//import mhmps.moebel.Bundle;
import mhmps.moebel.MoebelRepository;

//import java.util.Optional;

import javax.validation.Valid;

import mhmps.rechnung.RechnungService;
import org.salespointframework.catalog.Product;
//import org.salespointframework.core.AbstractEntity;
import org.salespointframework.order.Cart;
//import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
//import org.salespointframework.useraccount.UserAccount;
//import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")
class bestellungController {

	private final OrderManagement<Bestellung> bestellungManagement;
	private final KundenManagement kundenManagement;
	private final RechnungService rechnungService;
	private LieferungenData lieferungenData;
	private LieferungenRepository lieferungenRepository;
	private MoebelRepository moebel;
	private BundleRepository bundle;

	// etabliert die Bestellungen
	
	bestellungController(OrderManagement<Bestellung> bestellungManagement, KundenManagement kundenManagement,
			RechnungService rechnungService, LieferungenData lieferungenData,
			LieferungenRepository lieferungenRepository, MoebelRepository moebel, BundleRepository bundle) {
		this.rechnungService = rechnungService;

		Assert.notNull(bestellungManagement, "OrderManagement must not be null!");
		Assert.notNull(kundenManagement, "KundenManagement must not be null!");
		Assert.notNull(lieferungenData, "LieferungenData must not be null!");
		Assert.notNull(lieferungenRepository, "LieferungenRepository must not be null!");
		Assert.notNull(moebel, "MoebelRepository must not be null!");
		Assert.notNull(bundle, "BundleRepository must not be null!");
		this.bundle = bundle;
		this.moebel = moebel;
		this.lieferungenRepository = lieferungenRepository;
		this.lieferungenData = lieferungenData;
		this.bestellungManagement = bestellungManagement;
		this.kundenManagement = kundenManagement;
	}

	// hier wird der Warenkorb aufgebaut und gefüllt
	
	@ModelAttribute("cart")
	Cart initializeCart() {
		return new Cart();
	}

	@PostMapping("/cart")
	String addBundle(@RequestParam("pid") Product bundle, @ModelAttribute Cart cart) {

		cart.addOrUpdateItem(bundle, Quantity.of(1));

		return "redirect:cart";
	}

	@GetMapping("/cart")
	String basket(CheckoutForm form) {
		return "cart";
	}

	@GetMapping("/cart/delete/{id}")
	String basketdelete(@PathVariable String id, @ModelAttribute Cart cart) {
		cart.removeItem(id);
		return "redirect:/cart";
	}

	@PostMapping("/checkout")
	String buy(@ModelAttribute Cart cart, @Valid CheckoutForm form, Errors result,
			RedirectAttributes redirectAttributes) throws DocumentException, IOException {

		if (result.hasErrors()) {
			return "redirect:/cart";
		}

		var bestellung = new Bestellung(
				kundenManagement.getKundeById(Long.parseLong(form.getKundennummer())).getUserAccount(), Cash.CASH,
				form.getLieferart());

		cart.addItemsTo(bestellung);

		if (form.getLieferart() != Lieferart.Abholung) {
			var lieferung = new Lieferungen(LocalDate.of(2020, 5, 13), null, null, null, null);
			System.out.print("ID:" + cart.stream().findFirst().get().getId() + "\n");
			float masse = 0;
			/*
			for (int i = 0; i < cart.toList().size(); i++) {
				if(cart.getItem(cart.toList().get(i).getId()).get().getProduct().getCategories()==null)
					masse += moebel.findById(cart.getItem(cart.toList().get(i).getId()).get().getProduct().getId()).get()
						.getMasse();
				else
					masse += bundle.findById(cart.getItem(cart.toList().get(i).getId()).get().getProduct().getId()).get()
					.getMasse();
			}
			*/
			if (form.getLieferart() == Lieferart.LKW_mieten) {
				lieferung.setGemietet("True");
				lieferung.setLieferadresse("-");

			} else {
				lieferung.setGemietet("False");
				lieferung.setLieferadresse(form.getLieferadresse());
			}
			if (masse < 100)
				lieferung.setFahrzeug("MiniMobil");
			else if (masse > 3000)
				lieferung.setFahrzeug("MaxiMobil");
			else
				lieferung.setFahrzeug("MediMobil");
			lieferungenData.saveLieferungen(lieferung);
			bestellung.setLieferung(lieferung);
		}

		// bestellungManagement.payOrder(bestellung);
		// bestellungManagement.completeOrder(bestellung);
		bestellungManagement.save(bestellung);

		rechnungService.getInvoice(bestellung);

		cart.clear();

		redirectAttributes.addFlashAttribute("orderRef", bestellung.getId().getIdentifier());

		return "redirect:/";

	}

	@GetMapping("/bestellung")
	// @PreAuthorize("hasRole('BOSS')")
	String bestellung(Model model) {

		model.addAttribute("ordersPaid", bestellungManagement.findBy(OrderStatus.PAID));
		model.addAttribute("ordersCompleted", bestellungManagement.findBy(OrderStatus.COMPLETED));
		model.addAttribute("ordersOpen", bestellungManagement.findBy(OrderStatus.OPEN));
		model.addAttribute("ordersCancelled", bestellungManagement.findBy(OrderStatus.CANCELLED));

		return "orders";
	}

	@GetMapping("/bestellung/setPaid/{id}")
	String payBestellung(@PathVariable OrderIdentifier id) {
		bestellungManagement.payOrder(bestellungManagement.get(id).get());
		return "redirect:/bestellung";
	}

	@GetMapping("/bestellung/cancel/{id}")
	String cancelBestellung(@PathVariable OrderIdentifier id) {
		var bestellung = bestellungManagement.get(id).get().getLieferung();
		bestellungManagement.delete(bestellungManagement.get(id).get());
		if (bestellung != null)
			lieferungenRepository.delete(bestellung);
		return "redirect:/bestellung";
	}

}