package site.ogobi.ogobi.boundedContext.chatMessage;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.ogobi.ogobi.base.security.PrincipalDetails;

import java.util.List;

import static site.ogobi.ogobi.boundedContext.chatMessage.SignalType.NEW_MESSAGE;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chats/{roomId}/sendMessage")
    @SendTo("/topic/chats/{roomId}")
    public SignalResponse sendMessage(@DestinationVariable Long roomId, ChatMessageRequest chatMessageRequest, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ChatMessage chatMessage = chatMessageService.save(chatMessageRequest.getContent(), principalDetails.getMember().getNickname(), roomId);

        //simpMessagingTemplate.convertAndSend("/topic/chats/"+roomId, chatMessage);

        return SignalResponse.builder()
                .type(NEW_MESSAGE)
                .build();
    }

    @GetMapping("/chatrooms/{roomId}/messages")
    @ResponseBody
    public List<ChatMessage> findAll(
            @PathVariable Long roomId, @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(defaultValue = "") Long fromId) {

        List<ChatMessage> chatMessages =
                chatMessageService.findAllMessagesByChatRoomIdAndFromId(roomId, fromId);

        return chatMessages;
    }
}
