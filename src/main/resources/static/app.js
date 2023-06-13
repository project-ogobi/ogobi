let stompClient = null;
let fromId = 0;
let ChatMessageUl = null;

function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);

    const headers = {
        'X-CSRF-TOKEN': token,
    };

    stompClient.connect(headers, function (frame) {
        console.log('Connected: ' + frame);

        onConnected();
    });
}

function onConnected() {
    stompClient.subscribe(`/topic/chats/${chatRoomId}`, function (data) {

        getChatMessages();
    });
}

function getChatMessages() {

    console.log("fromId : " + fromId);
    fetch(`/chatrooms/${chatRoomId}/messages?fromId=${fromId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        }})
        .then(response => response.json())
        .then(body => {
            const { messages, memberId } = body;
            drawMessages(messages, memberId);
        })
        .catch(error => {
            console.error(error);
        });
}

function drawMessages(messages, memberId) {

    let isOwnChat = true;

    if (messages.length > 0) {
        fromId = messages[messages.length - 1].id;
    }

    messages.forEach((message) => {
        let isOwnChat = message.senderId === memberId;

        addChatBubble(isOwnChat, `${message.sender}`, `${message.content}`);
    });
}

function chatWriteMessage(form) {
    form.content.value = form.content.value.trim();

    stompClient.send(`/app/chats/${chatRoomId}/sendMessage`, {}, JSON.stringify({content: form.content.value}));

    form.content.value = '';
    form.content.focus();
}

document.addEventListener("DOMContentLoaded", function() {
    ChatMessageUl = document.querySelector('.chat__message-ul');
    getChatMessages();
    connect();
});

function addChatBubble(isOwnChat, sender, message) {
    const chatContainer = document.createElement("div");
    chatContainer.classList.add("chat", isOwnChat ? "chat-end" : "chat-start");

    const chatHeader = document.createElement("div");
    chatHeader.classList.add("chat-header");
    chatHeader.textContent = sender;

    const chatBubble = document.createElement("div");
    chatBubble.classList.add("chat-bubble");
    chatBubble.textContent = message;

    chatContainer.appendChild(chatHeader);
    chatContainer.appendChild(chatBubble);
    ChatMessageUl.appendChild(chatContainer);
}