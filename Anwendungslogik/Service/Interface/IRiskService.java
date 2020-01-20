package de.othr.sw.DreamSchufa.Anwendungslogik.Service.Interface;

import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.RiskDto;
import de.othr.sw.DreamSchufa.Anwendungslogik.Dto.RiskResponseDto;

public interface IRiskService {
    RiskResponseDto RiskEstimation(RiskDto risk);
}
