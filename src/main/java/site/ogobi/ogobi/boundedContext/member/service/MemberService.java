package site.ogobi.ogobi.boundedContext.member.service;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
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

    @Transactional
    public List<Challenge> runningList(Member member){     //  진행 중인 챌린지 리스트
        List<Challenge> challengeList = challengeRepository.findByMember(member);
        List<Challenge> runningChallengeList = new ArrayList<>();

        if (challengeList.size()==0){
            return null;
        }

        for (Challenge ch: challengeList) {
            if (!ch.isDone()){
                runningChallengeList.add(ch);
            }
        }

        return runningChallengeList;
    }

    @Transactional
    public List<Challenge> doneList(Member member){     //  완료한 챌린지 리스트
        List<Challenge> challengeList = challengeRepository.findByMember(member);
        List<Challenge> doneList = new ArrayList<>();

        for (Challenge ch:challengeList) {
            if (ch.isDone()){
                doneList.add(ch);
            }
        }

        if (doneList.size()==0){
            return null;
        }

        return doneList;
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

    public Member findById(String memberId) { // 비밀번호 찾기에서 사용됨
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

    public Member findById(Long memberId) {
        Optional<Member> foundMember = memberRepository.findById(memberId);
        if (foundMember.isPresent()) {
            return foundMember.get();
        } else {
            return null;
        }
    }

    public Member findbyNickname(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        if (optionalMember.isPresent()) {
            return optionalMember.get();
        } else {
            return null;
        }
    }

    @Transactional
    public void editNickname(Long memberId, String newNickname, String username) {
        Member member = findById(memberId);
        if (!member.getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        member.setNickname(newNickname);
        memberRepository.save(member);
    }
}