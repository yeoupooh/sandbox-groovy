import io.undertow.*
import io.undertow.server.*
import io.undertow.util.*

@Grapes([
        @Grab(group = 'org.jboss.logging', module = 'jboss-logging', version = '3.2.1.Final'),
        @Grab(group = 'io.undertow', module = 'undertow-core', version = '1.2.7.Final'),
        @Grab(group = 'io.undertow', module = 'undertow-servlet', version = '1.2.7.Final'),
        @Grab(group = 'io.undertow', module = 'undertow-websockets-jsr', version = '1.2.7.Final')
])

// https://github.com/pokle/undertow-hello-world-server/blob/master/src/main/java/HelloWorldServer.java
Undertow server = Undertow.builder()
        .addListener(8080, "localhost")
        .setHandler(new HttpHandler() {
    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Hello World");
    }
}).build();
server.start();

System.out.println("Started server at http://127.1:8080/  Hit ^C to stop");