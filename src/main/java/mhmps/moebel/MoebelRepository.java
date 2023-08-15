package mhmps.moebel;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.util.Streamable;
import org.springframework.data.domain.Sort;

public interface MoebelRepository extends Catalog<Moebel> {

	// find products of a certain type
	Streamable<Moebel> findByType(Category type, Sort sort);

	@Override
	Streamable<Moebel> findAll();
}