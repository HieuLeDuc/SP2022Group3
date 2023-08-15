package mhmps.moebel;

import static org.salespointframework.core.Currencies.EURO;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(10)
class MoebelInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(MoebelInitializer.class);

	private final MoebelRepository moebel;
	private final BundleRepository bundles;

	MoebelInitializer(MoebelRepository moebel, BundleRepository bundles) {
		Assert.notNull(moebel, "MoebelRepository must not be null!");
		Assert.notNull(bundles, "BundleRepository must not be null!");

		this.moebel = moebel;
		this.bundles = bundles;
	}

	@Override
	public void initialize() {

		// Skip creation if database was already populated
		if (moebel.count() > 0 || bundles.count() > 0) {
			return;
		}

		LOG.info("Creating standard moebel and bundles");

		// initialize Moebeln

		moebel.save(new Moebel("Bett Helga", Money.of(122.5, EURO), "black", "wood", 12.44f, "ikea", Annotation.Neu,
				Category.Schlafzimmer, 14));
		moebel.save(new Moebel("Sofa Joachim", Money.of(242, EURO), "blue", "cotton", 22f, "möbelBoss",
				Annotation.Aktion, Category.Wohnzimmer, 12));
		moebel.save(new Moebel("Schreibtisch Werner", Money.of(122, EURO), "white", "wood", 15.23f, "mömax",
				Annotation.Bestseller, Category.Arbeitszimmer, 22));
		moebel.save(new Moebel("Esstisch Ursula", Money.of(1212, EURO), "red", "plastic", 15.23f, "ikea", null,
				Category.Küche, 0));
		moebel.save(new Moebel("Stuhl Steffen", Money.of(2, EURO), "black", "plastic", 153f, "ikea", null,
				Category.Küche, 300));

		// initialize a bundle, then add contents
		Bundle item = new Bundle("Tischgruppe Anton", Money.of(112, EURO), 0.1f, null, 12);
		item.addMoebel(moebel.findByName("Schreibtisch Werner").toList().get(0));
		item.addMoebel(moebel.findByName("Bett Helga").toList().get(0));
		item.addMoebel(moebel.findByName("Esstisch Ursula").toList().get(0));
		bundles.save(item);
		return;
	}
}
