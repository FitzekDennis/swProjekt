package de.othr.sw.DreamSchufa.Anwendungslogik.Service;

import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.BankDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.RiskDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.RiskResponseDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.TransactionReturnDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Service.Interface.IRiskService;
import de.othr.sw.DreamSchufa.Persistenz.Entity.*;
import de.othr.sw.DreamSchufa.Persistenz.Repository.CustomerRepository;
import de.othr.sw.DreamSchufa.Persistenz.Repository.TransaktionRepository;
import de.othr.sw.DreamSchufa.Persistenz.Repository.UserRepository;
import de.othr.sw.DreamSchufa.enums.Art;
import de.othr.sw.DreamSchufa.enums.Risikostufe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class RiskService implements IRiskService {

    @Autowired
    private UserRepository _userRepo;

    @Autowired
    private CustomerRepository _custRepo;

    @Autowired
    private TransaktionRepository _transRepo;

    @Autowired
    private RestTemplate _restServerClient;

    @Override
    public RiskResponseDto RiskEstimation(RiskDto risk) {
        List<User> userList = _userRepo.findByNameAndGeburtsdatum(risk.name, risk.geburtsdatum);

        if(userList.size() == 0)
            return null;

        if(userList.size() > 1)
            return null;

        User user = userList.get(0);

        //Risikoanalyse durchführen
        RiskResponseDto riskDto = Estimation(user.activities, user.risks, risk.betrag, user.avgEinnahme);

        TransactionReturnDto bankResponse = null;
        try {
            bankResponse = StartTransaction(risk.apiKey, user.userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bankResponse == null)
            return null;

        if (bankResponse.status == false)
            return null;

        return riskDto;
    }

    public RiskResponseDto Estimation(List<Activity>activities, List<Risk>risks, int betrag, int avgEinnahme){
        int riskCounter = 0;
        Risikostufe aktuelleRisikostufe = Risikostufe.KEINRISIKO;
        for(Risk recentRisk : risks){
            Risikostufe risikostufe = recentRisk.risikostufe;
            switch(recentRisk.risikostufe){
                case KEINRISIKO:
                    break;
                case MITTLERESRISIKO:
                    riskCounter++;
                    break;
                case HOHESRISIKO:
                    riskCounter = riskCounter + 5;
                    break;
                case EMBARGO:
                    aktuelleRisikostufe = Risikostufe.EMBARGO;
                    RiskResponseDto embargoResponse = new RiskResponseDto();
                    embargoResponse.riskikostufe = aktuelleRisikostufe;
                    return embargoResponse;
                default:
                    break;
            }
        }
        int kreditCounter = 0;

        int kreditBetragGesamt = 0;
        for(Activity recentActivity : activities){
            if (recentActivity.art == Art.KREDITAUFGENOMMEN){
                kreditCounter++;
                kreditBetragGesamt = kreditBetragGesamt + recentActivity.betrag;
            }
            if (recentActivity.art == Art.KREDITABBEZAHLT){
                kreditCounter--;
                kreditBetragGesamt = kreditBetragGesamt - recentActivity.betrag;
            }
        }

        float guthabenRatio = kreditBetragGesamt / avgEinnahme;
        if (guthabenRatio > 20)
            riskCounter = riskCounter + 10;
        if (guthabenRatio > 10)
            riskCounter = riskCounter + 5;
        if (guthabenRatio < 1)
            riskCounter = riskCounter - 5;

        float kreditRatio = betrag / avgEinnahme;

        riskCounter = riskCounter + (int)kreditRatio;

        if (riskCounter <= 5)
            aktuelleRisikostufe = Risikostufe.KEINRISIKO;
        if (riskCounter > 5 && riskCounter <= 10)
            aktuelleRisikostufe = Risikostufe.MITTLERESRISIKO;
        if (riskCounter > 10){
            aktuelleRisikostufe = Risikostufe.HOHESRISIKO;
        }

        RiskResponseDto riskResponse = new RiskResponseDto();
        riskResponse.riskikostufe = aktuelleRisikostufe;

        return riskResponse;
    }

    // Maxis Response Dto
    private TransactionReturnDto StartTransaction(String apikey, int userId) throws IOException
    {
        List<Customer> customerList = _custRepo.findByApiKey(apikey);

        if(customerList.size() == 0)
            return null;

        if(customerList.size() > 1)
            return null;

        Customer currentCustomer = customerList.get(0);

        //mein Sendekey "2b4105b9-112b-4c9c-8507-7806177b0ad2"
        BankDto bank = new BankDto();
        bank.amount = 10;
        bank.receiverIban = "DE26000000000000000056";
        bank.senderIban = currentCustomer.iban;
        bank.senderKey = currentCustomer.senderKey;

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<BankDto> request =
                new HttpEntity<BankDto>(bank, headers);

        ResponseEntity<TransactionReturnDto> bankResponseDto = _restServerClient.postForEntity("http://im-codd:8859/restapi/transaction", request, TransactionReturnDto.class);
        TransactionReturnDto trans = bankResponseDto.getBody();

        AddTransaktion(trans, currentCustomer.customerId, userId);

        return trans;
    }
    // Transaktion in Db schreiben
    @Transactional
    public void AddTransaktion(TransactionReturnDto trans, int custId, int userId) {
        Optional<User> userResp = _userRepo.findById(userId);
        Optional<Customer> customerResp = _custRepo.findById(custId);

        if (!userResp.isPresent() || !customerResp.isPresent())
            return;

        User user = userResp.get();

        Transaktion transaktion = new Transaktion();
        transaktion.betrag = 10;
        transaktion.user = user;
        transaktion.datum = new Date();

        _transRepo.save(transaktion);

        Customer customer = customerResp.get();

        customer.transaktions.add(transaktion);

        _custRepo.save(customer);

        return;
    }

}
