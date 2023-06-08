package site.ogobi.ogobi.boundedContext.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChallengePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;

    @OneToOne(fetch = FetchType.LAZY)
    private Post post;
}
