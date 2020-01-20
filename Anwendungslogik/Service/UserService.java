package de.othr.sw.DreamSchufa.Anwendungslogik.Service;

import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.ActivityDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Service.Interface.IUserService;
import de.othr.sw.DreamSchufa.Persistenz.Entity.Activity;
import de.othr.sw.DreamSchufa.Persistenz.Entity.Risk;
import de.othr.sw.DreamSchufa.Persistenz.Entity.User;
import de.othr.sw.DreamSchufa.Persistenz.Repository.ActivityRepository;
import de.othr.sw.DreamSchufa.Persistenz.Repository.RiskRepository;
import de.othr.sw.DreamSchufa.Persistenz.Repository.UserRepository;
import de.othr.sw.DreamSchufa.enums.Risikostufe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository _userRepo;

    @Autowired
    private ActivityRepository _activityRepo;

    @Autowired
    private RiskRepository _riskRepo;

    @Override
    @Transactional
    public boolean UpdateUser(ActivityDto activity) {
        Activity newActivity = new Activity();
        newActivity.name = activity.name;
        newActivity.art = activity.art;
        newActivity.betrag = activity.betrag;
        _activityRepo.save(newActivity);

        List<User> userList = _userRepo.findByNameAndGeburtsdatum(activity.nameKunde, activity.geburtsdatum);

        if(userList.size() == 0){
            User newUser = new User();
            newUser.activities.add(newActivity);
            newUser.geburtsdatum = activity.geburtsdatum;
            newUser.name = activity.nameKunde;
            newUser = GetDetailInformation(newUser, newActivity);

            _userRepo.save(newUser);

            return true;
        }

        if(userList.size() > 1)
            return false;

        User user  = userList.get(0);

        user = GetDetailInformation(user, newActivity);

        int size = user.activities.size();
        user.activities.add(newActivity);
        List<Activity> newList  = user.activities;

        user.activities.set(size, newList.get(size));

        return true;
    }

    @Override
    @Transactional
    public User GetDetailInformation(User user, Activity activity){
        switch(activity.art){
            case NEUESEINKOMMEN:
                user.avgEinnahme = activity.betrag;
                break;
            case BESCHAEFTIGUNGWECHSEL:
                user.avgEinnahme = activity.betrag;
                user.beruf = activity.name;
                break;
            case KREDITAUFGENOMMEN:
            case ZAHLUNGABGELEHNT:
                Risk middleRisk = new Risk();
                middleRisk.risikostufe = Risikostufe.MITTLERESRISIKO;
                middleRisk.zeitpunkt = new Date();
                _riskRepo.save(middleRisk);
                user.risks.add(middleRisk);
                break;
            case KREDITABBEZAHLT:
            case ZAHLUNGERFOLGREICH:
                Risk lowRisk = new Risk();
                lowRisk.risikostufe = Risikostufe.KEINRISIKO;
                lowRisk.zeitpunkt = new Date();
                _riskRepo.save(lowRisk);
                user.risks.add(lowRisk);
                break;
        }

        return user;
    }
}
