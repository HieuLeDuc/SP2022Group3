package mhmps.mitarbeiter;

import java.util.UUID;
import javax.persistence.*;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.core.SalespointIdentifier;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

@Entity
public class Mitarbeiter extends AbstractAggregateRoot<mhmps.mitarbeiter.Mitarbeiter.MitarbeiterIdentifier> {

	@OneToOne
	private UserAccount userAccount;

	private @EmbeddedId MitarbeiterIdentifier mitarbeiternummer = new MitarbeiterIdentifier(
			UUID.randomUUID().toString());

	public Mitarbeiter() {
	}

	public Mitarbeiter(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

//Getter and Setter
	@Override
	public MitarbeiterIdentifier getId() {
		return mitarbeiternummer;
	}

	public String getFirstname() {
		return userAccount.getFirstname();
	}

	public void setFirstname(String firstname) {
		userAccount.setFirstname(firstname);
	}

	public String getLastname() {
		return userAccount.getLastname();
	}

	public void setLastname(String lastname) {
		userAccount.setLastname(lastname);
	}

	public String getEmail() {
		return userAccount.getEmail();
	}

	public void setEmail(String email) {
		userAccount.setEmail(email);
	}

	public boolean isEnabled() {
		return userAccount.isEnabled();
	}

	public void setEnabled(boolean enabled) {
		userAccount.setEnabled(enabled);
	}

	public String getUsername() {
		return userAccount.getUsername();
	}

	public boolean isBoss() {
		return userAccount.hasRole(Role.of("BOSS"));
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

//MitarbeiterId zur eindeutigen Bestimmung eines Mitarbeiters
	@Embeddable
	public static final class MitarbeiterIdentifier extends SalespointIdentifier {

		private static final long serialVersionUID = 7740660930809051850L;

		MitarbeiterIdentifier() {
			super();
		}

		MitarbeiterIdentifier(String mitarbeiterIdentifier) {
			super(mitarbeiterIdentifier);
		}
	}
}
