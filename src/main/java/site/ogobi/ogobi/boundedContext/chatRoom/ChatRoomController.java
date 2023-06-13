package site.ogobi.ogobi.boundedContext.chatRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.ogobi.ogobi.base.security.PrincipalDetails;
import site.ogobi.ogobi.boundedContext.chatMessage.ChatMessage;
import site.ogobi.ogobi.boundedContext.chatMessage.ChatMessageService;

import java.util.List;

@Controller
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;


    @GetMapping("/{chatRoomId}")
    public String showChatRoom(@PathVariable Long chatRoomId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<ChatMessage> messages = chatMessageService.findAllMessagesByChatRoomId(chatRoomId);
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
        chatRoom.getChatUsers().stream()
                .filter(i -> i.getMember().getId().equals(principalDetails.getMember().getId()))
                .findFirst()
                .ifPresentOrElse(
                        user -> {}, () -> chatRoomService.addChatUserWithChatRoom(chatRoom, principalDetails.getMember())
                );

        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("messages", messages);
        return "chat/roomDetail";
    }

    @GetMapping("/create")
    public String showChatRoomForm(Model model) {
        model.addAttribute("chatRoom", new ChatRoomDTO());
        return "chat/createRoom";
    }

    @PostMapping("/create")
    public String createChatRoom(@ModelAttribute("chatRoom") ChatRoomDTO chatRoomDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        chatRoomService.createChatRoom(chatRoomDto.getName(), chatRoomDto.getContent(), principalDetails.getMember());
        return "redirect:/chatrooms";
    }

    @GetMapping("")
    public String showChatRooms(Model model) {
        //TODO: 채팅방목록보기 구현해야함.
        List<ChatRoom> chatRooms = chatRoomService.findAllChatRooms();
        model.addAttribute("chatRooms", chatRooms);
        return "chat/chatrooms";
    }

}