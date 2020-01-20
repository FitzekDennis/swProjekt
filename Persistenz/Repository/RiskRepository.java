package de.othr.sw.DreamSchufa.Persistenz.Repository;

import de.othr.sw.DreamSchufa.Persistenz.Entity.Customer;
import de.othr.sw.DreamSchufa.Persistenz.Entity.Risk;
import org.springframework.data.repository.CrudRepository;

public interface RiskRepository extends CrudRepository<Risk, Integer> {
}
