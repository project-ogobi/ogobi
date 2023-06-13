package site.ogobi.ogobi.boundedContext.chatMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ogobi.ogobi.boundedContext.chatRoom.ChatRoom;
import site.ogobi.ogobi.boundedContext.chatRoom.ChatRoomService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(String content, String nickName, Long roomId) {

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoomId(roomId)
                .sender(nickName)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();

        return chatMessageRepository.save(chatMessage);
    }


    public List<ChatMessage> getByChatRoomIdAndMemberIdAndFromId(Long roomId, Long memberId, Long fromId) {
        ChatRoom chatRoom = chatRoomService.findById(roomId);

        chatRoom.getChatUsers().stream()
                .filter(chatUser -> chatUser.getMember().getId().equals(memberId))
                .findFirst()
                .orElseThrow();

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(roomId);

        List<ChatMessage> list = chatMessages.stream()
                .filter(chatMessage -> chatMessage.getId() > fromId)
                .sorted(Comparator.comparing(ChatMessage::getId))
                .toList();

        return list;
    }

    public List<ChatMessage> findAllMessagesByChatRoomId(Long chatRoomId) {
        //TODO: 메시지 가져오기
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }
}
