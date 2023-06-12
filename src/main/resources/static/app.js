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

        stompClient.subscribe(`/topic/chats/${chatRoomId}`, function (data) {
            getChatMessages();

        });
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
            console.log(body);
            drawMessages(body);
        })
        .catch(error => {
            console.error(error);
        });
}

function drawMessages(messages) {

    if (Array.isArray(messages)) {
        messages.forEach((message) => {
            const newItem = document.createElement("li");
            newItem.textContent = `${message.sender} : ${message.content}`;
            ChatMessageUl.appendChild(newItem);
        });
    }
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
