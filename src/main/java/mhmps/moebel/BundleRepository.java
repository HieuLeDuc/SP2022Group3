package mhmps.moebel;

import org.salespointframework.catalog.Catalog;

import org.springframework.data.util.Streamable;

public interface BundleRepository extends Catalog<Bundle> {

	@Override
	Streamable<Bundle> findAll();
}