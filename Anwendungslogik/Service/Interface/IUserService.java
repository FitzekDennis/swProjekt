package de.othr.sw.DreamSchufa.Anwendungslogik.Service.Interface;

import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.ActivityDto;
import de.othr.sw.DreamSchufa.Persistenz.Entity.Activity;
import de.othr.sw.DreamSchufa.Persistenz.Entity.User;

public interface IUserService {
    boolean UpdateUser (ActivityDto activity);
    User GetDetailInformation(User user, Activity activity);
}
