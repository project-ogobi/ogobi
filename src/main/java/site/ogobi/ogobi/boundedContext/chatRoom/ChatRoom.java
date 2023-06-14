package site.ogobi.ogobi.boundedContext.chatRoom;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;
import site.ogobi.ogobi.boundedContext.chatUser.ChatUser;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class ChatRoom extends BaseEntity {

    private String name;
    private String content;

    @ManyToOne(fetch = LAZY)
    private Member owner;

    @OneToMany(mappedBy = "chatRoom", cascade = PERSIST)
    @Builder.Default
    private Set<ChatUser> chatUsers = new HashSet<>();

    public void addChatUser(Member member) {
        ChatUser chatUser = ChatUser.builder()
                .member(member)
                .chatRoom(this)
                .build();
        chatUsers.add(chatUser);
    }
}
