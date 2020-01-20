package de.othr.sw.DreamSchufa.Persistenz.Entity;

import de.othr.sw.DreamSchufa.enums.Art;

import javax.persistence.*;

@Entity
@Embeddable
public class Activity {
    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    public int activityId;
    public String name;
    public Art art;
    public int betrag;
}

