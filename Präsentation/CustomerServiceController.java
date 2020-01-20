package de.othr.sw.DreamSchufa.Präsentation;

import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.CustomerDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.TransaktionDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Service.Interface.ICustomerService;
import de.othr.sw.DreamSchufa.Persistenz.Entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Controller
public class CustomerServiceController {

    @Autowired
    private ICustomerService _custService;

    @RequestMapping("/")
    public String starten(){
        return "index";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value="/customer/login", method = RequestMethod.POST)
    public String LogInCustomer(@ModelAttribute("name") String name,
                                @ModelAttribute("password") String pw,
                                Model model){
        Customer customer = _custService.GetCustomer(name);

        model.addAttribute("apiKey", customer.apiKey);

        return "customerpage";
    }

    @RequestMapping(value="/restapi/customer/new", method = RequestMethod.POST)
    public String NewCustomer( @ModelAttribute("name") String name,
                               @ModelAttribute("password") String pw,
                               @ModelAttribute("iban") String iban,
                               @ModelAttribute("senderKey") String senderKey,
                              Model model) {
        CustomerDto customer = new CustomerDto();
        customer.iban = iban;
        customer.name = name;
        customer.password = pw;
        customer.senderKey = senderKey;
        String apiKey = _custService.NewCustomer(customer);
        if (apiKey != null){
            model.addAttribute("apiKey", apiKey);
            return "customerpage";
        }
        else {
            model.addAttribute("fail", true);
            return "register";
        }


    }

    @RequestMapping(value="/restapi/customer/transactions/{custId}", method = RequestMethod.GET)
    public List<TransaktionDto> GetTransaktions(@PathParam("custId") int custId) {
        return _custService.GetTransactions(custId);
    }
}
