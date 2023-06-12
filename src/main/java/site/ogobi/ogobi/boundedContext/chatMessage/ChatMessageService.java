package site.ogobi.ogobi.boundedContext.chatMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;

    public ChatMessage save(String content, String nickName, Long roomId) {

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoomId(String.valueOf(roomId))
                .sender(nickName)
                .content(content)
                .timestamp(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findAllMessagesByChatRoomIdAndFromId(Long chatRoomId, Long fromId) {

        return getLatestMessages(50);
    }

    //TODO: 메시지 가져오기
    public List<ChatMessage> getLatestMessages(int limit) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(limit);
        return mongoTemplate.find(query, ChatMessage.class);
    }


}
