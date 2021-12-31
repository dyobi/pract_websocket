package pkg.util;

import pkg.dao.ChatDao;
import pkg.vo.MessageVo;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@ServerEndpoint(
        value="/chat/{name}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class
)
public class Socket {

    private static List<Session> clientList = Collections.synchronizedList(new ArrayList<>());
    private static Map<String, String> nameList = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name)
            throws EncodeException, IOException, SQLException, ClassNotFoundException {
        clientList.add(session);
        nameList.put(name, session.getId());

        MessageVo messageVo = new MessageVo();

        messageVo.setFrom("CHATBOT");
        messageVo.setContent("--- [" + name + "] joined this chatroom ---");

        broadcast(session, messageVo);
    }

    @OnMessage
    public void onMessage(Session session, MessageVo messageVo)
            throws IOException, EncodeException, SQLException, ClassNotFoundException {
        broadcast(session, messageVo);
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @OnClose
    public void onClose(Session session)
            throws EncodeException, IOException, SQLException, ClassNotFoundException {
        MessageVo messageVo = new MessageVo();

        for (String key : nameList.keySet()) {
            String value = nameList.get(key);

            if (value.equals(session.getId())) {
                messageVo.setFrom("CHATBOT");
                messageVo.setContent("--- [" + key + "] has left this chatroom ---");
                break;
            }
        }
        if (!messageVo.getFrom().equals("")) {
            broadcast(session, messageVo);
        }

        clientList.remove(session);
    }

    private synchronized static void broadcast(Session self, MessageVo messageVo)
            throws EncodeException, IOException, SQLException, ClassNotFoundException {
        String toId = nameList.get(messageVo.getTo());

        for (Session session : clientList) {

            if (self.getId().equals(session.getId()) && !messageVo.getTo().equals("") && toId == null) {
                messageVo.setFrom("CHATBOT");
                messageVo.setContent("--- Can\'t find user name [" + messageVo.getTo() + "] ---");
                messageVo.setTo("");
                session.getBasicRemote().sendObject(messageVo);
                return;
            } else if (self.getId().equals(session.getId())) {
                continue;
            } else if (messageVo.getTo() == null || messageVo.getTo().equals("")) {
                session.getBasicRemote().sendObject(messageVo);
            } else {
                if (toId.equals(session.getId())) {
                    session.getBasicRemote().sendObject(messageVo);
                    break;
                }
            }
        }

        if (!messageVo.getFrom().equals("CHATBOT")) {
            boolean insert = new ChatDao().insertChat(messageVo);
        }
    }

}
