package de.othr.sw.DreamSchufa.Persistenz.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaktion {
    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    public int transaktionId;
    public Date datum;
    public int betrag;
    @ManyToOne
    public User user;

}
