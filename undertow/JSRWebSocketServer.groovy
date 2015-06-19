import io.undertow.Handlers
import io.undertow.Undertow
import io.undertow.server.handlers.PathHandler
import io.undertow.server.handlers.resource.ClassPathResourceManager
import io.undertow.servlet.api.DeploymentInfo
import io.undertow.servlet.api.DeploymentManager
import io.undertow.servlet.api.ServletContainer
import io.undertow.websockets.jsr.WebSocketDeploymentInfo
import org.xnio.ByteBufferSlicePool

import javax.servlet.ServletException

@Grapes([
        // NOTE: To fix 'download fail' error, copying required files to be under '~/.m2/' folder.
        @Grab(group = 'org.jboss.logging', module = 'jboss-logging', version = '3.1.4.GA'),
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
        // NOTE: To fix error "java.lang.NoSuchMethodError: javax.servlet.ServletContext.getClassLoader()Ljava/lang/ClassLoader;",
        // Rmove servlet-api-2.4.jar from groovy lib folder. Because Undertow uses servlet api 3.x.
        println javax.servlet.ServletContext.getClassLoader()
        manager.deploy();
        try {
            path.addPrefixPath("/", manager.start());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}

