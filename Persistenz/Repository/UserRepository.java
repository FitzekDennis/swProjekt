package de.othr.sw.DreamSchufa.Persistenz.Repository;

import de.othr.sw.DreamSchufa.Persistenz.Entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByNameAndGeburtsdatum(String Name, String Geburtsdatum);
}
