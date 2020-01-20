package de.othr.sw.DreamSchufa.Persistenz.Repository;

import de.othr.sw.DreamSchufa.Persistenz.Entity.Transaktion;
import org.springframework.data.repository.CrudRepository;

public interface TransaktionRepository extends CrudRepository<Transaktion, Integer> {
}
