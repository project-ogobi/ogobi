package site.ogobi.ogobi.boundedContext.title;

import jakarta.persistence.*;

@Entity
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

}

