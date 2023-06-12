package site.ogobi.ogobi.boundedContext.chatRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.ogobi.ogobi.boundedContext.chatMessage.ChatMessage;

import java.util.List;

@Controller
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    @GetMapping("/{chatRoomId}")
    public String showChatRoom(@PathVariable Long chatRoomId, Model model) {
        List<ChatMessage> messages = chatRoomService.findAllMessagesByChatRoomId(chatRoomId);
        ChatRoom chatroom = chatRoomService.findById(chatRoomId);
        model.addAttribute("chatRoom", chatroom);
        model.addAttribute("messages", messages);
        return "chat/roomDetail";
    }

    @GetMapping("/create")
    public String showChatRoomForm(Model model) {
        model.addAttribute("chatRoom", new ChatRoomDTO());
        return "chat/createRoom";
    }

    @PostMapping("/create")
    public String createChatRoom(@ModelAttribute("chatRoom") ChatRoomDTO chatRoomDto) {
        chatRoomService.createChatRoom(chatRoomDto.getName(), chatRoomDto.getContent());
        return "redirect:/chatrooms";
    }

    @GetMapping("")
    public String showChatRooms(Model model){
        //TODO: 채팅방목록보기 구현해야함.
        List<ChatRoom> chatRooms = chatRoomService.findAllChatRooms();
        model.addAttribute("chatRooms", chatRooms);
        return "chat/chatrooms";
    }

}