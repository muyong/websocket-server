import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/chat", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class MyServerEndpoint {

    static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(String.format("%s joined the chat room.", session.getId()));
        peers.add(session);
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException {
        //broadcast the message
        System.out.println("got message from " + message.getSender() + " : " + message.getContent());
        for (Session peer : peers) {
            if (!session.getId().equals(peer.getId())) { // do not resend the message to its sender
                peer.getBasicRemote().sendObject(message);
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        System.out.println(String.format("%s left the chat room.", session.getId()));
        peers.remove(session);
        //notify peers about leaving the chat room
        for (Session peer : peers) {
            Message message = new Message();
            message.setSender("Server");
            message.setContent(String.format("%s left the chat room", session.getId()));
            message.setReceived(new Date());
            peer.getBasicRemote().sendObject(message);
        }
    }

}
