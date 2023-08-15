package mhmps.kunden;

import javax.persistence.*;
import org.salespointframework.useraccount.UserAccount;

/*
 * Kundenklasse zur Speicherung und Verwaltung von Kundendaten
 */
@Entity
public class Kunde {

	@OneToOne
	private UserAccount userAccount;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long kundennummer;
	
	private String adresse;

	public Kunde() {
	}

	// Getter und Setter
	
	public Kunde(UserAccount userAccount, String adresse) {
		this.userAccount = userAccount;
		this.setAdresse(adresse);
	}

	public long getId() {
		return kundennummer;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getEmail() {
		return userAccount.getEmail();
	}

	public void setEmail(String email) {
		userAccount.setEmail(email);
	}

	public String getVorname() {
		return userAccount.getFirstname();
	}

	public void setVorname(String vorname) {
		userAccount.setFirstname(vorname);
	}

	public String getNachname() {
		return userAccount.getLastname();
	}

	public void setNachname(String nachname) {
		userAccount.setLastname(nachname);
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public String getUsername() {
		return userAccount.getUsername();
	}

}