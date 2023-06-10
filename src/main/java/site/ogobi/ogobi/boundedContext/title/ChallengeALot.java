package site.ogobi.ogobi.boundedContext.title;

import lombok.Getter;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

import java.util.List;


public class ChallengeALot implements Title {

    @Getter
    String titleName= "명예 도전자";

    @Override
    public Boolean condition(Member member) {
        List<Challenge> challengeList = member.getChallenge();
        int count = 10;

        if (challengeList.size() >= count){
            return true;
        }

        return false;
    }
}
