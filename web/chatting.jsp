<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pkg.vo.MessageVo" %>
<%@ page import="pkg.dao.ChatDao" %>
<%@ page import="java.sql.SQLException" %>
<%
    ArrayList<MessageVo> list = new ArrayList<>();

    try {
        list = new ChatDao().getChatList(session.getAttribute("name").toString());
    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chatting</title>
    <style>
        .container {
            position: absolute;
            top: calc(50% - 300px);
            left: calc(50% - 250px);

            width: 500px;
            height: 600px;
            background-color: #D3D3D3;

            border: black 1px dashed;
            border-radius: 8px;

            font-family: 'Barlow', sans-serif;
        }

        #chat_list {
            margin: 10px;
            padding: 10px;
            resize: none;

            width: 480px;
            height: 480px;
            background-color: #fff;

            box-sizing: border-box;
            border: black 1px dashed;
            border-radius: 8px;

            overflow-y: scroll;
            overflow-wrap: break-word;
        }

        #chat_input {
            margin: 0 10px;
            padding: 0 10px;

            width: 400px;
            height: 88px;
        }

        .send_btn {
            margin: 0;
            padding: 0;

            width: 64px;
            height: 88px;
        }

        textarea:focus, input:focus {
            outline: 0;
        }
        .from_chatbot {
            color: red;
        }
        .whisper {
            color: blue;
        }
    </style>
</head>
<body>
<div class="container">
    <div id="chat_list">
        <%
            for (MessageVo messageVo : list) {
                String className = "";
                if (messageVo.getFrom().equals("CHATBOT")) className = "class=\"from_chatbot\"";
                else if (messageVo.getTo() != null) className = "class=\"whisper\"";

                out.println("<p " + className + ">[" + messageVo.getFrom() +
                        (messageVo.getTo() != null ? " ▶ " + messageVo.getTo() : "") +
                        "] " + messageVo.getContent() + "</p>");
            }
        %>
    </div>
    <input id="chat_input" type="text" placeholder="Text ..." onkeyup="enterEvent();">
    <input class="send_btn" type="submit" value="SEND" onclick="send();">
</div>
</body>
<script>
    let name = '<%=session.getAttribute("name")%>';
    let chat_list = document.getElementById('chat_list');
    let chat_input = document.getElementById('chat_input');
    let ws = new WebSocket('ws://' +
        '<%=request.getServerName()+":"+request.getServerPort()+request.getContextPath()%>' +
        '/chat/' + name);

    const enterEvent = () => {
        if (window.event.keyCode === 13) send();
    }

    const send = () => {
        if (chat_input.value === '') return null;
        const regex = /^\/[a-zA-Z0-9ㄱ-ㅎ가-힣]/;

        let message = {};
        message.from = name;
        message.to = '';
        message.content = chat_input.value;

        if (regex.test(message.content)) {
            message.to = message.content.split(' ')[0].slice(1);
            message.content = chat_input.value.substr(chat_input.value.indexOf(' ') + 1);
        } if (message.from === message.to) {
            chat_list.innerHTML += '<p class=\"from_chatbot\">[CHATBOT] --- You can\'t whisper to yourself ---</p>';
            chat_input.value = '';
            chat_input.focus();
            chat_list.scrollTop = chat_list.scrollHeight;
            return null;
        }

        let className;
        if (message.from === 'CHATBOT') className = 'class=\"from_chatbot\"';
        else if (message.to !== '') className = 'class=\"whisper\"';
        else className = '';

        ws.send(JSON.stringify(message));
        chat_list.innerHTML += '<p ' + className + '>[' + message.from + (message.to !== '' ? ' ▶ ' + message.to : '') +
            '] ' + message.content + '</p>';
        chat_input.value = '';
        chat_input.focus();
        chat_list.scrollTop = chat_list.scrollHeight;
    }

    ws.onerror = (e) => console.log(e.data);
    ws.onopen = () => chat_input.focus();
    ws.onmessage = (e) => {
        let message = JSON.parse(e.data);
        let className;
        if (message.from === 'CHATBOT') className = 'class=\"from_chatbot\"';
        else if (message.to !== '') className = 'class=\"whisper\"';
        else className = '';
        chat_list.innerHTML += '<p ' + className + '>[' + message.from +
            (message.to !== '' ? ' ▶ ' + message.to : '') + '] ' + message.content + '</p>';
        chat_list.scrollTop = chat_list.scrollHeight;
    };
</script>
</html>
