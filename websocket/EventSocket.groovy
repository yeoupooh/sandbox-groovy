import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.*

@Grapes([
        @Grab(group = 'javax.servlet', module = 'javax.servlet-api', version = '3.0.1'),
        @Grab(group = 'org.eclipse.jetty', module = 'jetty-server', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty', module = 'jetty-servlet', version = '9.2.11.v20150529'),
])

@WebSocket
public class EventSocket {

    @OnWebSocketConnect
    public void onWebSocketConnect(Session sess) {
        System.out.println("Socket Connected: " + sess);
    }

    @OnWebSocketMessage
    public void onWebSocketText(String message) {
        System.out.println("Received TEXT message: " + message);
    }

    @OnWebSocketClose
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }

    @OnWebSocketError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }
}