package de.othr.sw.DreamSchufa.Persistenz.Entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Recht {
    @Id
    @GeneratedValue( strategy=GenerationType.AUTO)
    private int rechtId;
    private String rechteName;

    @OneToMany(mappedBy ="recht", cascade= CascadeType.ALL, fetch= FetchType.LAZY)
    private Set<KundenRecht> kundenRechte = new HashSet<>();
}
