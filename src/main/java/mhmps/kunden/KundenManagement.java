package mhmps.kunden;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import mhmps.mitarbeiter.EditForm;

@Service
@Transactional
public class KundenManagement {

	public static final Role CUSTOMER_ROLE = Role.of("CUSTOMER");

	private final KundenRepository kunden;
	private final UserAccountManagement userAccounts;

	KundenManagement(KundenRepository kunden, UserAccountManagement userAccounts) {

		Assert.notNull(kunden, "KundenRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManagement must not be null!");

		this.kunden = kunden;
		this.userAccounts = userAccounts;
	}

//Verarbeitung der Daten die auf der Html eingegeben wurden und Erzeugung eines neuen Kunden
	public Kunde createKunde(KundenForm form) {
		var userAccount = userAccounts.create(form.getEmail(), UnencryptedPassword.of("123"), CUSTOMER_ROLE);
		userAccount.setFirstname(form.getVorname());
		userAccount.setLastname(form.getNachname());
		userAccount.setEmail(form.getEmail());
		userAccount.setEnabled(false);
		return kunden.save(new Kunde(userAccount, form.getAdresse()));
	}

//Löschen eines Kunden und dessen UserAccount aus der DB
	public void deleteKunde(Long kundennummer) {
		UserAccount userAccount = getKundeById(kundennummer).getUserAccount();
		userAccounts.delete(userAccount);
		kunden.deleteById(kundennummer);
		return;
	}

//Kunden mit Id finden
	public Kunde getKundeById(Long id) {
		return kunden.findById(id).get();
	}

//Kundendaten manipulieren anhand dessen was in der html eingegeben wurde
	public void editKunde(EditForm form, Long id) {
		Kunde kunde = getKundeById(id);
		if (form.getVorname() != "")
			kunde.setVorname(form.getVorname());
		if (form.getNachname() != "")
			kunde.setNachname(form.getNachname());
		if (form.getEmail() != "")
			kunde.setEmail(form.getEmail());
		if (form.getAdresse() != "")
			kunde.setAdresse(form.getAdresse());
		return;
	}

	public Streamable<Kunde> findAll() {
		return kunden.findAll();
	}

	public long getKundenCount() {
		return kunden.count();
	}
}