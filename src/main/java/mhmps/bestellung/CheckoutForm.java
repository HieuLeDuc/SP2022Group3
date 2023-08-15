package mhmps.bestellung;

import javax.validation.constraints.NotEmpty;

import org.springframework.validation.Errors;

class CheckoutForm {

	private final @NotEmpty String kundennummer;
	private final Lieferart lieferart;
	private String lieferadresse;

	public CheckoutForm(String kundennummer, Lieferart lieferart, String lieferadresse) {

		this.kundennummer = kundennummer;
		this.lieferart = lieferart;
		this.lieferadresse = lieferadresse;
	}

	public String getKundennummer() {
		return kundennummer;
	}

	public String getLieferadresse() {
		return lieferadresse;
	}

	public Lieferart getLieferart() {
		return lieferart;
	}

	public void validate(Errors errors) {
		// Complex validation goes here
	}
}