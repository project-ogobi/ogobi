package site.ogobi.ogobi.boundedContext.chatMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.ogobi.ogobi.base.security.PrincipalDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static site.ogobi.ogobi.boundedContext.chatMessage.SignalType.NEW_MESSAGE;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chats/{roomId}/sendMessage")
    @SendTo("/topic/chats/{roomId}")
    public SignalResponse sendMessage(@DestinationVariable Long roomId, ChatMessageRequest chatMessageRequest, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ChatMessage chatMessage = chatMessageService.save(chatMessageRequest.getContent(), principalDetails.getMember().getNickname(), principalDetails.getMember().getId(), roomId);

        return SignalResponse.builder()
                .type(NEW_MESSAGE)
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chatrooms/{roomId}/messages")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> findAll(
            @PathVariable Long roomId, @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(defaultValue = "0") Long fromId) {

        List<ChatMessage> chatMessages =
                chatMessageService.getByChatRoomIdAndMemberIdAndFromId(roomId, principalDetails.getMember().getId(), fromId);

        Map<String, Object> response = new HashMap<>();
        response.put("memberId", principalDetails.getMember().getId());
        response.put("messages", chatMessages);

        return ResponseEntity.ok(response);
    }
}
