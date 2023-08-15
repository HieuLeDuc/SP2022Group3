package mhmps.mitarbeiter;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import mhmps.mitarbeiter.Mitarbeiter.MitarbeiterIdentifier;

// Ein Repository-Interface zur Arbeit mit den Mitarbeiterdaten in der Datenbank

interface MitarbeiterRepository extends CrudRepository<Mitarbeiter, MitarbeiterIdentifier> {

	// Re-declared {@link CrudRepository#findAll()} to return a {@link Streamable}
	// instead of {@link Iterable}.

	@Override
	Streamable<Mitarbeiter> findAll();

}
