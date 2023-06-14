package site.ogobi.ogobi.boundedContext.title;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Title(Long id, String name) {
        this.name = name;
    }

    public Title() {
    }
}

