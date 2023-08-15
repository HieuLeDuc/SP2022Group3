package mhmps.mitarbeiter;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(10)
class MitarbeiterInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(MitarbeiterInitializer.class);

	private final MitarbeiterManagement mitarbeiterManagement;

	MitarbeiterInitializer(MitarbeiterManagement mitarbeiterManagement) {
		Assert.notNull(mitarbeiterManagement, "MitarbeiterRepository must not be null!");

		this.mitarbeiterManagement = mitarbeiterManagement;
	}

	@Override
	public void initialize() {

		// Sollten schon Mitarbeiter in der Datenbank stehen wird kein weiterer initialisiert
		if (mitarbeiterManagement.getMitarbeiterCount() > 0) {
			return;
		}
//anlegen des boss und eines mitarbeiters
		LOG.info("Creating BOSS & first COWORKER");

		mitarbeiterManagement.createMitarbeiter(
				new MitarbeiterForm("Chef", "Boss", "ChefBoss@MöbelHier.de", "boss", "password"), Role.of("BOSS"));
		mitarbeiterManagement.createMitarbeiter(
				new MitarbeiterForm("Dwight", "Schrute", "DwightSchrute@MöbelHier.de", "dwightyBoy", "1234"));
		return;
	}
}
