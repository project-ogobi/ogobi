package site.ogobi.ogobi.boundedContext.member.service;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.repository.ChallengeRepository;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.entity.MemberTitle;
import site.ogobi.ogobi.boundedContext.member.repository.MemberRepository;
import site.ogobi.ogobi.boundedContext.member.repository.MemberTitleRepository;
import site.ogobi.ogobi.boundedContext.title.Title;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberTitleRepository memberTitleRepository;
    private final PasswordEncoder passwordEncoder;

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

    public Member findByEmail(String email) {
        Optional<Member> foundMember = memberRepository.findByEmail(email);
        if (foundMember.isPresent()) {
            return foundMember.get();
        } else {
            return null;
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

    public List<Title> titleList(Member member) {
        List<MemberTitle> memberTitles = memberTitleRepository.findByMember(member);

        if (memberTitles.isEmpty()) {
            return Collections.emptyList();
        }

        List<Title> titles = new ArrayList<>();

        for (MemberTitle memberTitle : memberTitles) {
            titles.add(memberTitle.getTitle());
        }

        return titles;
    }

    public void setTitle(Member member, String title) {
        member.setTitle(title);
        memberRepository.save(member);
    }
    public Member findByResetToken(String resetToken) {
        Optional<Member> foundMember = memberRepository.findByResetToken(resetToken);
        if (foundMember.isPresent()) {
            return foundMember.get();
        } else {
            return null;
        }

    }

    public Member findById(String memberId) {
        Optional<Member> foundMember = memberRepository.findById(Long.parseLong(memberId));
        if (foundMember.isPresent()) {
            return foundMember.get();
        } else {
            return null;
        }
    }

    @Transactional
    public void updateNewPassword(Member member, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.setPassword(encodedPassword);
    }
}