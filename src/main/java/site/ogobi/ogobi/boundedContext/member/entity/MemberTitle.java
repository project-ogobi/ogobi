package site.ogobi.ogobi.boundedContext.member.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.ogobi.ogobi.boundedContext.title.Title;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MemberTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    private Title title;

}
