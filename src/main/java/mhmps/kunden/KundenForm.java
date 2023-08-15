package mhmps.kunden;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.Errors;

class KundenForm {

	private final @NotEmpty(message = "Bitte Vornamen eingeben!") String vorname;
	private final @NotEmpty(message = "Bitte Nachnamen eingeben!") String nachname;
	private final @NotEmpty(message = "Bitte Adresse eingeben!") String adresse;
	private final @Email(message = "Bitte gültige E-Mailadresse eingeben!") @NotEmpty(message = "Bitte E-Mailadresse eingeben!") String email;

	public KundenForm(String vorname, String nachname, String adresse, String email) {

		this.vorname = vorname;
		this.nachname = nachname;
		this.adresse = adresse;
		this.email = email;
	}

	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public String getAdresse() {
		return adresse;
	}

	public String getEmail() {
		return email;
	}

	public void validate(Errors errors) {
		// Complex validation goes here
	}
}