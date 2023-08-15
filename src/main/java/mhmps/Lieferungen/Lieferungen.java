package mhmps.Lieferungen;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "lieferungens")
public class Lieferungen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "datum")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate datum;

	@Column(name = "fahrzeug")
	private String fahrzeug;

	@Column(name = "lieferadresse")
	private String lieferadresse;

	@Column(name = "gemietet")
	private String gemietet;

	@Column(name = "uhrzeit")
	private String uhrzeit;

	public Lieferungen() {
		super();
	}

	public Lieferungen(LocalDate datum, String fahrzeug, String lieferadresse, String gemietet, String uhrzeit) {
		super();

		this.datum = datum;
		this.fahrzeug = fahrzeug;
		this.lieferadresse = lieferadresse;
		this.gemietet = gemietet;
		this.uhrzeit = uhrzeit;
	}

//Getter und Setter
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	public String getFahrzeug() {
		return fahrzeug;
	}

	public void setFahrzeug(String fahrzeug) {
		this.fahrzeug = fahrzeug;
	}

	public String getLieferadresse() {
		return lieferadresse;
	}

	public void setLieferadresse(String lieferadresse) {
		this.lieferadresse = lieferadresse;
	}

	public String getGemietet() {
		return gemietet;
	}

	public void setGemietet(String gemietet) {
		this.gemietet = gemietet;
	}

	public String getUhrzeit() {
		return uhrzeit;
	}

	public void setUhrzeit(String uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

}
