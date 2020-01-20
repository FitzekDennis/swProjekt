package de.othr.sw.DreamSchufa.Anwendungslogik.Service.Interface;

import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.CustomerDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.TransaktionDto;
import de.othr.sw.DreamSchufa.Persistenz.Entity.Customer;

import java.util.List;

public interface ICustomerService {
    String NewCustomer(CustomerDto cust);
    List<TransaktionDto> GetTransactions(int custId);
    Customer GetCustomer(String customername);
}
