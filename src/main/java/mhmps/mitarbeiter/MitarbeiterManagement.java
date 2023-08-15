package mhmps.mitarbeiter;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import mhmps.mitarbeiter.Mitarbeiter.MitarbeiterIdentifier;

@Service
@Transactional
public class MitarbeiterManagement {

	public static final Role COWORKER_ROLE = Role.of("COWORKER");

	private final MitarbeiterRepository mitarbeiter;
	private final UserAccountManagement userAccounts;

	MitarbeiterManagement(MitarbeiterRepository mitarbeiter, UserAccountManagement userAccounts) {

		Assert.notNull(mitarbeiter, "MitarbeiterRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManagement must not be null!");

		this.mitarbeiter = mitarbeiter;
		this.userAccounts = userAccounts;
	}

//Anlegen eines neuen Mitarbeiters anhand eines Forms
	public Mitarbeiter createMitarbeiter(MitarbeiterForm form) {
		var userAccount = userAccounts.create(form.getNutzername(), UnencryptedPassword.of(form.getPassword()),
				COWORKER_ROLE);
		userAccount.setFirstname(form.getVorname());
		userAccount.setLastname(form.getNachname());
		userAccount.setEmail(form.getEmail());
		return mitarbeiter.save(new Mitarbeiter(userAccount));
	}

	public Mitarbeiter createMitarbeiter(MitarbeiterForm form, Role role) {
		var userAccount = userAccounts.create(form.getNutzername(), UnencryptedPassword.of(form.getPassword()), role);
		userAccount.setFirstname(form.getVorname());
		userAccount.setLastname(form.getNachname());
		userAccount.setEmail(form.getEmail());
		return mitarbeiter.save(new Mitarbeiter(userAccount));
	}

//löschen eines Mitarbeiters
	public void deleteMitarbeiter(MitarbeiterIdentifier mitarbeiternummer) {
		UserAccount userAccount = getMitarbeiterById(mitarbeiternummer).getUserAccount();
		userAccounts.delete(userAccount);
		mitarbeiter.deleteById(mitarbeiternummer);
		return;
	}

	// Anpassen von Mitarbeiterdaten, falls diese in der Html neu eingegeben werden
	public void editMitarbeiter(EditForm form, MitarbeiterIdentifier id) {
		Mitarbeiter mitarbeiter = getMitarbeiterById(id);
		if (form.getVorname() != "")
			mitarbeiter.setFirstname(form.getVorname());
		if (form.getNachname() != "")
			mitarbeiter.setLastname(form.getNachname());
		if (form.getEmail() != "")
			mitarbeiter.setEmail(form.getEmail());
		if (form.getEnabled().equals("true"))
			mitarbeiter.setEnabled(true);
		else
			mitarbeiter.setEnabled(false);
		return;
	}

//Mitarbeiter anhand der Id finden
	public Mitarbeiter getMitarbeiterById(MitarbeiterIdentifier id) {
		return mitarbeiter.findById(id).get();
	}

	
// rückgabe aller Mitarbeiter
	public Streamable<Mitarbeiter> findAll() {
		return mitarbeiter.findAll();
	}

//Rückgabe der Anzahl der Mitarbeiter in der DB
	public long getMitarbeiterCount() {
		return mitarbeiter.count();
	}

}