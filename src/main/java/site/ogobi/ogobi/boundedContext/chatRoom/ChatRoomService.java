package site.ogobi.ogobi.boundedContext.chatRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ogobi.ogobi.boundedContext.chatMessage.ChatMessage;
import site.ogobi.ogobi.boundedContext.chatMessage.ChatMessageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public void createChatRoom(String name, String content){
        ChatRoom newChatRoom = ChatRoom.builder()
                .name(name)
                .content(content)
                .build();

        chatRoomRepository.save(newChatRoom);
    }

    public List<ChatRoom> findAllChatRooms(){
        return chatRoomRepository.findAll();
    }

    public List<ChatMessage> findAllMessagesByChatRoomId(Long chatRoomId) {
        //TODO: 메시지 가져오기
        return chatMessageRepository.findByChatRoomId(String.valueOf(chatRoomId));
    }

    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow();
    }
}
