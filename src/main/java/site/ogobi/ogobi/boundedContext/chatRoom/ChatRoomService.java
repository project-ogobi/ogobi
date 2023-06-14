package site.ogobi.ogobi.boundedContext.chatRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.chatMessage.ChatMessage;
import site.ogobi.ogobi.boundedContext.chatMessage.ChatMessageRepository;
import site.ogobi.ogobi.boundedContext.chatUser.ChatUser;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public void createChatRoom(String name, String content, Member member){

        ChatRoom newChatRoom = ChatRoom.builder()
                .name(name)
                .content(content)
                .owner(member)
                .chatUsers(new HashSet<>())
                .build();

        chatRoomRepository.save(newChatRoom);

        newChatRoom.addChatUser(member);
    }

    public List<ChatRoom> findAllChatRooms(){
        return chatRoomRepository.findAll();
    }


    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow();
    }

    public void addChatUserWithChatRoom(ChatRoom chatRoom, Member member) {

        chatRoom.addChatUser(member);
    }
}
