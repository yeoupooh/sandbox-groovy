import groovy.json.JsonSlurper
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.client.WebSocketClient

import java.util.concurrent.Future

@Grapes([
        @Grab(group = 'javax.servlet', module = 'javax.servlet-api', version = '3.0.1'),
        @Grab(group = 'org.eclipse.jetty', module = 'jetty-server', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty', module = 'jetty-servlet', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty.websocket', module = 'websocket-common', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty.websocket', module = 'websocket-client', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty.websocket', module = 'websocket-server', version = '9.2.11.v20150529')
])

// https://github.com/jetty-project/embedded-jetty-websocket-examples/blob/master/native-jetty-websocket-example/src/main/java/org/eclipse/jetty/demo/EventClient.java

def config = new JsonSlurper().parseText(new File("./event.config.json").text)

URI uri = URI.create(config.url);

WebSocketClient client = new WebSocketClient();
try {
    try {
        client.start();
        // The socket that receives events
        EventSocket socket = new EventSocket();
        // Attempt Connect
        Future<Session> fut = client.connect(socket, uri);
        // Wait for Connect
        Session session = fut.get();
        // Send a message
        session.getRemote().sendString(config.message);
        // Close session
        session.close();
    }
    finally {
        client.stop();
    }
}
catch (Throwable t) {
    t.printStackTrace(System.err);
}