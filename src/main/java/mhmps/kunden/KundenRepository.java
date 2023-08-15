package mhmps.kunden;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

interface KundenRepository extends CrudRepository<Kunde, Long> {

	@Override
	Streamable<Kunde> findAll();
}
