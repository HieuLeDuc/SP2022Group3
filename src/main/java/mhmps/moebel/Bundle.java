package mhmps.moebel;

import static org.salespointframework.core.Currencies.EURO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

@Entity
public class Bundle extends Product {

	@SuppressWarnings({ "deprecation" })
	Bundle() {
	};

	@Column(name = "rabatt")
	private float rabatt;
	@Column(name = "annotation")
	private Annotation annotation;
	@OneToMany
	private List<Moebel> itemlist = new ArrayList<Moebel>();
	@Column(name = "stock")
	private int stock;

	public Bundle(String name, Money price, float rabatt, Annotation annotation, int stock) {
		super(name, Money.zero(EURO));
		this.rabatt = rabatt;
		this.annotation = annotation;
		this.stock = stock;
		if (price != null) {
			this.setPrice(price);
		}
		// Auto set annotation for pregenerated items, disable if necessary
		if (getAnnotation() == null) {
			setAnnotation(Annotation.Neu);
		}
	}

	// Getters

	public float getRabatt() {
		return rabatt;
	}

	public List<Moebel> getItemlist() {
		return itemlist;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public int getStock() {
		return stock;
	}

	public void setItemlist(ArrayList<Moebel> itemlist) {
		this.itemlist = itemlist;
	}

	public void setRabatt(float rabatt) {
		this.rabatt = rabatt;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	// Manipulation of the item list
	public void addMoebel(Moebel item) {
		itemlist.add(item);
	}

	public void removeMoebel(Moebel item) {
		if (!itemlist.isEmpty()) {
			itemlist.remove(item);
		}
	}

	// Search if a furniture is present
	public boolean findMoebel(Moebel item) {
		for (Moebel moebel : itemlist) {
			if (moebel.getName().equals(item.getName()))
				return true;
		}
		return false;
	}
	
	public float getMasse() {
		float masse=0;
		for(int i=0;i<itemlist.size();i++) {
			masse+=itemlist.get(i).getMasse();
		}
		return masse;
	}

}
