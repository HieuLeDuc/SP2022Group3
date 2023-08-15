package mhmps.mitarbeiter;

import org.springframework.validation.Errors;

public class EditForm {

	private final String vorname, nachname, email, adresse;
	private final String enabled;

	public EditForm(String vorname, String nachname, String email, String adresse, String enabled) {

		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.adresse = adresse;
		this.enabled = enabled;
	}

	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public String getEmail() {
		return email;
	}

	public String getAdresse() {
		return adresse;
	}

	public String getEnabled() {
		return enabled;
	}

	public void validate(Errors errors) {
		// Complex validation goes here
	}
}
