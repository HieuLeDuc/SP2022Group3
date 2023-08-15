package mhmps.bestellung;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;

import mhmps.Lieferungen.Lieferungen;

@Entity
public class Bestellung extends Order{
	
	private Lieferart lieferart;
	@OneToOne private Lieferungen lieferung;
	@SuppressWarnings("deprecation")
	Bestellung() {
		super();
	}
	Bestellung(UserAccount userAccount, Lieferart lieferart) {
		super(userAccount);
		this.lieferart=lieferart;
	}
	Bestellung(UserAccount userAccount, PaymentMethod paymentMethod, Lieferart lieferart) {
		super(userAccount, paymentMethod);
		this.lieferart=lieferart;
	}
	
	public Lieferart getLieferart() {
		return lieferart;
	}
	
	void setLieferart(Lieferart lieferart) {
		this.lieferart=lieferart;
	}
	
	public Lieferungen getLieferung() {
		return lieferung;
	}
	
	void setLieferung(Lieferungen lieferung) {
		this.lieferung=lieferung;
	}
}
