import javax.websocket.OnMessage
import javax.websocket.Session
import javax.websocket.server.ServerEndpoint

@Grapes([
        @Grab(group = 'org.jboss.logging', module = 'jboss-logging', version = '3.2.1.Final'),
        @Grab(group = 'io.undertow', module = 'undertow-core', version = '1.2.7.Final'),
        @Grab(group = 'io.undertow', module = 'undertow-servlet', version = '1.2.7.Final'),
        @Grab(group = 'io.undertow', module = 'undertow-websockets-jsr', version = '1.2.7.Final')
])

// https://github.com/undertow-io/undertow/blob/master/examples/src/main/java/io/undertow/examples/jsrwebsockets/JsrChatWebSocketEndpoint.java
@ServerEndpoint("/myapp")
public class JsrChatWebSocketEndpoint {

    @OnMessage
    public void message(String message, Session session) {
        for (Session s : session.getOpenSessions()) {
            s.getAsyncRemote().sendText(message);
        }
    }

}