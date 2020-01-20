package de.othr.sw.DreamSchufa.Präsentation;

import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.ActivityDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Service.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class UserServiceController {

    @Autowired
    private IUserService _userService;

    @RequestMapping(value = "/restapi/user", method = POST)
    public boolean UpdateUser(@RequestBody ActivityDto activity) {
        return _userService.UpdateUser(activity);
    }
}