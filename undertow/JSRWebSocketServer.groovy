import io.undertow.*
import io.undertow.server.*
import io.undertow.server.handlers.*
import io.undertow.server.handlers.resource.*
import io.undertow.servlet.api.*
import io.undertow.servlet.spec.*
import io.undertow.util.*
import io.undertow.websockets.jsr.*
import org.xnio.*

import javax.servlet.*

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

// https://github.com/undertow-io/undertow/blob/master/examples/src/main/java/io/undertow/examples/jsrwebsockets/JSRWebSocketServer.java
public class JSRWebSocketServer {
    public static void main(final String[] args) {
        PathHandler path = Handlers.path();


        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(path)
                .build();
        server.start();

        final ServletContainer container = ServletContainer.Factory.newInstance();

        DeploymentInfo builder = new DeploymentInfo()
                .setClassLoader(JSRWebSocketServer.class.getClassLoader())
                .setContextPath("/")
                .addWelcomePage("index.html")
                .setResourceManager(new ClassPathResourceManager(JSRWebSocketServer.class.getClassLoader(),
                ""/*JSRWebSocketServer.class.getPackage()*/))
                .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME,
                new WebSocketDeploymentInfo()
                        .setBuffers(new ByteBufferSlicePool(100, 1000))
                        .addEndpoint(JsrChatWebSocketEndpoint.class)
        )
                .setDeploymentName("chat.war");

        DeploymentManager manager = container.addDeployment(builder);
        // FIXME Error here "java.lang.NoSuchMethodError: javax.servlet.ServletContext.getClassLoader()Ljava/lang/ClassLoader;"
        println javax.servlet.ServletContext.getClassLoader()
        manager.deploy();
        try {
            path.addPrefixPath("/", manager.start());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}

