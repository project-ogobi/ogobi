package site.ogobi.ogobi.boundedContext.chatUser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final ChatUserRepository chatRoomUserRepository;

    public ChatUser findById(Long chatRoomUserId) {
        return chatRoomUserRepository.findById(chatRoomUserId).orElseThrow();
    }
}