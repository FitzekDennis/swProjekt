package de.othr.sw.DreamSchufa.Präsentation;

import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.RiskDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.RiskResponseDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Service.Interface.IRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiskServiceController {

    @Autowired
    private IRiskService _riskService;

    @RequestMapping(value="/restapi/risk", method = RequestMethod.POST)
    public RiskResponseDto RiskEstimation(@RequestBody RiskDto risk){
        return _riskService.RiskEstimation(risk);
    }
}
