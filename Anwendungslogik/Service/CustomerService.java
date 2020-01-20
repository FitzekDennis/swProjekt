package de.othr.sw.DreamSchufa.Anwendungslogik.Service;

import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.CustomerDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.TransaktionDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Service.Interface.ICustomerService;
import de.othr.sw.DreamSchufa.MySecurityUtilities;
import de.othr.sw.DreamSchufa.Persistenz.Entity.Customer;
import de.othr.sw.DreamSchufa.Persistenz.Entity.Transaktion;
import de.othr.sw.DreamSchufa.Persistenz.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Qualifier("labresources")
public class CustomerService implements ICustomerService, UserDetailsService {

    @Autowired
    private CustomerRepository _customerRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private MySecurityUtilities security;

    @Override
    public String NewCustomer(CustomerDto cust) {
        Customer newCustomer = new Customer();
        newCustomer.name = cust.name;
        newCustomer.iban = cust.iban;
        newCustomer.senderKey = cust.senderKey;
        UUID apikeyUUID = UUID.randomUUID();
        newCustomer.apiKey = apikeyUUID.toString();
        newCustomer.setPassword(passwordEncoder.encode(cust.password));

        _customerRepo.save(newCustomer);
        return newCustomer.apiKey;
    }

    @Override
    public List<TransaktionDto> GetTransactions(int custId) throws IllegalArgumentException {
        Optional<Customer> customer = _customerRepo.findById(custId);

        if (!customer.isPresent())
            return null;

        Customer currCustomer = customer.get();

        List<TransaktionDto> transaktionListe = new ArrayList<>();

        for(Transaktion transaktion : currCustomer.transaktions){
            TransaktionDto transak  = new TransaktionDto();
            transak.betrag = transaktion.betrag;

            transak.datum = transaktion.datum;
            transak.user = transaktion.user;
            transak.transaktionId = transaktion.transaktionId;

            transaktionListe.add(transak);
        }

        return transaktionListe;
    }

    public Customer GetCustomer(String name) {
        Customer customer = _customerRepo.findByName(name);

        return customer;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Customer customer = _customerRepo.findByName(name);

        return customer;
    }
}
