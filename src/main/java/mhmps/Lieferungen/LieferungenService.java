package mhmps.Lieferungen;

import org.springframework.stereotype.Service;

@Service
public class LieferungenService implements LieferungenData {

	private LieferungenRepository lieferungenRepository;

	public LieferungenService(LieferungenRepository lieferungenRepository) {
		super();
		this.lieferungenRepository = lieferungenRepository;

	}

	@Override
	public Iterable<Lieferungen> getAllLieferungens() {
		return lieferungenRepository.findAll();
	}

	@Override
	public Lieferungen saveLieferungen(Lieferungen lieferungen) {
		return lieferungenRepository.save(lieferungen);
	}

	@Override
	public Lieferungen getLieferungenById(Long id) {
		return lieferungenRepository.findById(id).get();
	}

	@Override
	public Lieferungen updateLieferungen(Lieferungen lieferungen) {
		return lieferungenRepository.save(lieferungen);
	}

	@Override
	public void deleteLieferungenById(Long id) {
		lieferungenRepository.deleteById(id);
	}

}
