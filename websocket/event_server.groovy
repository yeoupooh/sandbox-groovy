import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

@Grapes([
        @Grab(group = 'javax.servlet', module = 'javax.servlet-api', version = '3.0.1'),
        @Grab(group = 'org.eclipse.jetty', module = 'jetty-server', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty', module = 'jetty-servlet', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty.websocket', module = 'websocket-common', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty.websocket', module = 'websocket-client', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty.websocket', module = 'websocket-server', version = '9.2.11.v20150529')
])

// https://github.com/jetty-project/embedded-jetty-websocket-examples/blob/master/native-jetty-websocket-example/src/main/java/org/eclipse/jetty/demo/EventServer.java

Server server = new Server();
ServerConnector connector = new ServerConnector(server);
connector.setPort(8080);
server.addConnector(connector);

// Setup the basic application "context" for this application at "/"
// This is also known as the handler tree (in jetty speak)
ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
context.setContextPath("/");
server.setHandler(context);

// Add a websocket to a specific path spec
ServletHolder holderEvents = new ServletHolder("ws-events", EventServlet.class);
context.addServlet(holderEvents, "/events/*");

try {
    server.start();
    server.dump(System.err);
    server.join();
}
catch (Throwable t) {
    t.printStackTrace(System.err);
}