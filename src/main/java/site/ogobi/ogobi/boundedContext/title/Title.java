package site.ogobi.ogobi.boundedContext.title;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import site.ogobi.ogobi.boundedContext.member.entity.Member;


public interface Title {

    @OneToMany(mappedBy = "Title", cascade = {CascadeType.ALL})
    String titleName = null;

    Boolean condition(Member member);
}
