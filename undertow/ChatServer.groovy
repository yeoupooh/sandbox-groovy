import io.undertow.*
import io.undertow.server.*
import io.undertow.server.handlers.resource.*
import io.undertow.util.*
import io.undertow.websockets.*
import io.undertow.websockets.core.*
import io.undertow.websockets.spi.*

import static io.undertow.Handlers.*

@Grapes([
        @Grab(group = 'org.jboss.logging', module = 'jboss-logging', version = '3.2.1.Final'),
        @Grab(group = 'org.jboss.spec.javax.servlet', module = 'jboss-servlet-api_3.1_spec', version = '1.0.0.Final'),
        @Grab(group = 'org.jboss.logging', module = 'jboss-logging-processor', version = '1.2.0.Final'),
        @Grab(group = 'org.jboss.xnio', module = 'xnio-api', version = '3.3.1.Final'),
        @Grab(group = 'org.jboss.xnio', module = 'xnio-nio', version = '3.3.1.Final'),
        @Grab(group = 'io.undertow', module = 'undertow-core', version = '1.2.7.Final'),
        @Grab(group = 'io.undertow', module = 'undertow-servlet', version = '1.2.7.Final'),
        @Grab(group = 'io.undertow', module = 'undertow-websockets-jsr', version = '1.2.7.Final')
])

// https://github.com/undertow-io/undertow/blob/master/examples/src/main/java/io/undertow/examples/chat/ChatServer.java
public class ChatServer {

    public static void main(final String[] args) {

        System.out.println("To see chat in action is to open two different browsers and point them at http://localhost:8080");

        println "package=" + ChatServer.class.getPackage()

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(
                path()
                        .addPrefixPath("/myapp", websocket(
                        new WebSocketConnectionCallback() {

                            @Override
                            public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
                                println "onconnect"
                                channel.getReceiveSetter().set(ChatServer.createReceiveListener());
                                channel.resumeReceives();
                            } // onConnect
                        } // new WebSocketConnectionCallback()
                )) // addPrefixPath
                        .addPrefixPath("/", resource(new ClassPathResourceManager(ChatServer.class.getClassLoader(),
                        // NOTE: package name is null, so "" is used instead.
                        ""/*ChatServer.class.getPackage()*/))
                        .addWelcomeFiles("index.html"))
        ) // setHandler
                .build();

        server.start();
    }

    // NOTE: To avoid error "The current scope already contains a variable of the name channel", ARL should be
    // defined out of main method
    static AbstractReceiveListener createReceiveListener() {
        return new AbstractReceiveListener() {
            @Override
            protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
                final String messageData = message.getData();
                println "message=" + messageData
                for (WebSocketChannel session : channel.getPeerConnections()) {
                    WebSockets.sendText(messageData, session, null);
                }
            }
        }
    }
}
