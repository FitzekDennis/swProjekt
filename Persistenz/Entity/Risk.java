package de.othr.sw.DreamSchufa.Persistenz.Entity;

import de.othr.sw.DreamSchufa.enums.Risikostufe;

import javax.persistence.*;
import java.util.Date;

@Entity
@Embeddable
public class Risk {
    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    public int riskId;
    public Date zeitpunkt;
    public Risikostufe risikostufe;
}



