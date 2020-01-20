package de.othr.sw.DreamSchufa.Anwendungslogik.Dto;

import de.othr.sw.DreamSchufa.Persistenz.Entity.Customer;
import de.othr.sw.DreamSchufa.Persistenz.Entity.User;

import java.util.Date;

public class TransaktionDto {
    public int transaktionId;
    public Date datum;
    public int betrag;
    public User user;

}
