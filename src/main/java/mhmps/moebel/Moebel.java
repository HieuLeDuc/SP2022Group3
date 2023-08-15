package mhmps.moebel;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import static org.salespointframework.core.Currencies.EURO;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

@Entity
public class Moebel extends Product {

	@SuppressWarnings({ "deprecation" })
	Moebel() {
	};

	@Column(name = "farbe")
	private @NotEmpty(message = "Bitte Farbe eingeben!") String farbe;
	@Column(name = "material")
	private @NotEmpty(message = "Bitte Material eingeben!") String material;
	@Column(name = "masse")
	private @NotNull(message = "Bitte Masse eingeben!") float masse;
	@Column(name = "hersteller")
	private @NotEmpty(message = "Bitte Hersteller eingeben!") String hersteller;
	@Column(name = "category")
	private @NotNull(message = "Bitte Kategorie eingeben!") Category type;
	@Column(name = "annotation")
	private Annotation annotation;
	@Column(name = "stock")
	private @NotNull(message = "Bitte Menge eingeben!") int stock;

	public Moebel(String name, Money price, String farbe, String material, float masse, String hersteller,
			Annotation annotation, Category type, int stock) {

		super(name, Money.zero(EURO));
		this.farbe = farbe;
		this.material = material;
		this.masse = masse;
		this.hersteller = hersteller;
		this.annotation = annotation;
		this.type = type;
		this.stock = stock;
		// conditionally set value
		if (price != null) {
			this.setPrice(price);
		}
		// Auto set annotation for pregenerated items, disable if necessary
		if (getAnnotation() == null) {
			setAnnotation(Annotation.Neu);
		}
	}

	// Getters

	public String getFarbe() {
		return farbe;
	}

	public String getMaterial() {
		return material;
	}

	public float getMasse() {
		return masse;
	}

	public String getHersteller() {
		return hersteller;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public int getStock() {
		return stock;
	}

	public Category getCategory() {
		return type;
	}
	// Setters

	public void setFarbe(String farbe) {
		this.farbe = farbe;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public void setMasse(float masse) {
		this.masse = masse;
	}

	public void setHersteller(String hersteller) {
		this.hersteller = hersteller;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setCategory(Category category) {
		this.type = category;
	}

	// Sanitize input
	public String sanitizer(String name) {
		name = name.replaceAll("[^a-zA-Z0-9]", "");
		return name;
	}
}
