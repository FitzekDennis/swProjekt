package de.othr.sw.DreamSchufa.Persistenz.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Embeddable
public class User {
    @Id
    @GeneratedValue( strategy=GenerationType.AUTO)
    public int userId;
    public String name;
    public String vorname;
    public String nachname;
    public String geburtsdatum;
    public String geschlecht;
    public String mail;
    @ManyToOne
    public Adresse adresse;
    public int avgEinnahme;
    public String beruf;
    @OneToMany
    public List<Activity> activities = new ArrayList<>();
    @OneToMany
    public List<Risk> risks = new ArrayList<>();
}
