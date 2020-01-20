package de.othr.sw.DreamSchufa.Persistenz.Repository;

import de.othr.sw.DreamSchufa.Persistenz.Entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    List<Customer> findByApiKey(String apikey);
    Customer findByName(String name);
}
