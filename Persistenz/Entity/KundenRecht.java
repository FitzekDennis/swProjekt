package de.othr.sw.DreamSchufa.Persistenz.Entity;

import javax.persistence.*;

@Entity
public class KundenRecht {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int kundenRechtId;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    private Recht recht;
}
