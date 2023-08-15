package mhmps.Lieferungen;

//import java.util.List;

public interface LieferungenData {

	Iterable<Lieferungen> getAllLieferungens();

	Lieferungen saveLieferungen(Lieferungen lieferungen);

	Lieferungen getLieferungenById(Long id);

	Lieferungen updateLieferungen(Lieferungen lieferungen);

	void deleteLieferungenById(Long id);
}