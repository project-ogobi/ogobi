package site.ogobi.ogobi.boundedContext.member.service;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.repository.ChallengeRepository;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member getMember(String username) {
        Optional<Member> member = this.memberRepository.findByUsername(username);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new NotFoundException("member not found");
        }

    }


    public List<Challenge> runningList(Member member){     //  진행 중인 챌린지 리스트
        List<Challenge> challengeList = challengeRepository.findByMember(member);
        List<Challenge> runningChallengeList = new ArrayList<>();

        if (challengeList.size()==0){
            return null;
        }

        for (Challenge ch: challengeList) {
            if (!ch.isSuccess()){
                runningChallengeList.add(ch);
            }
        }

        return challengeList;
    }


    public List<Challenge> successList(Member member){     //  완료한 챌린지 리스트
        List<Challenge> challengeList = challengeRepository.findByMember(member);
        List<Challenge> successList = new ArrayList<>();

        for (Challenge ch:challengeList) {
            if (ch.isSuccess()){
                successList.add(ch);
            }
        }

        if (successList.size()==0){
            return null;
        }

        return successList;
    }
}