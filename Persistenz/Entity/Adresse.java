package de.othr.sw.DreamSchufa.Persistenz.Entity;

import javax.persistence.*;

@Entity
@Embeddable
public class Adresse {
    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    public int adresseId;
    public String strasse;
    public String hausnummer;
    public int plz;
    public String ort;
    public String land;
}
