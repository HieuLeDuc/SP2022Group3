package mhmps.mitarbeiter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Email;
import org.springframework.validation.Errors;

public class MitarbeiterForm {

	private final @NotEmpty(message = "Bitte Vornamen eingeben!") String vorname;
	private final @NotEmpty(message = "Bitte Nachnamen eingeben!") String nachname;
	private final @Email(message = "Bitte gültige E-Mailadresse eingeben!") @NotEmpty(message = "Bitte E-Mailadresse eingeben!") String email;
	private final @NotEmpty(message = "Bitte Nutzernamen eingeben!") String nutzername;
	private final @NotEmpty(message = "Bitte Passwort eingeben!") String password;

	public MitarbeiterForm(String vorname, String nachname, String email, String nutzername, String password) {

		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.nutzername = nutzername;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public String getNutzername() {
		return nutzername;
	}

	public void validate(Errors errors) {
		// Complex validation goes here
	}
}
