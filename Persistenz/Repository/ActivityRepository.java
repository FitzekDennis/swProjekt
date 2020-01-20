package de.othr.sw.DreamSchufa.Persistenz.Repository;

import de.othr.sw.DreamSchufa.Persistenz.Entity.Activity;
import de.othr.sw.DreamSchufa.Persistenz.Entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends CrudRepository<Activity, Integer> {
}
