import groovy.json.JsonSlurper
import groovy.servlet.GroovyServlet
import groovy.servlet.TemplateServlet
import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.*
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.util.ssl.SslContextFactory

@Grapes([
        @Grab(group = 'javax.servlet', module = 'javax.servlet-api', version = '3.0.1'),
        @Grab(group = 'org.eclipse.jetty', module = 'jetty-server', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty', module = 'jetty-servlet', version = '9.2.11.v20150529'),
])

def config = new JsonSlurper().parseText(new File("conf/jetty9.config.json").text)

class EmbeddedServer {
}

Server server = new Server();

ContextHandlerCollection contexts = new ContextHandlerCollection();
server.setHandler(contexts);

// static files handler
ResourceHandler resourceHandler = new ResourceHandler()
resourceHandler.with {
    directoriesListed = true
    resourceBase = 'static'
}
ContextHandler contextStatic = new ContextHandler(contexts, "/static");
contextStatic.setHandler(resourceHandler);

// Servlet context handler
ServletContextHandler contextScripts = new ServletContextHandler(contexts, "/", ServletContextHandler.SESSIONS);
contextScripts.with {
    resourceBase = "."
    addServlet(GroovyServlet, '*.groovy')  // All files ending with .groovy will be served.
    addServlet(TemplateServlet, "*.gsp")
    addServlet(GroovyServlet, '*.gtpl')
}

// SSL Connector
// http://blog.anvard.org/articles/2013/10/05/jetty-ssl-server.html
// http://blog.anvard.org/articles/2013/09/18/webmvc-server-3.html
// http://java.dzone.com/articles/adding-ssl-support-embedded
// http://stackoverflow.com/questions/3997748/how-can-i-create-a-keystore
ServerConnector connector = new ServerConnector(server);
connector.setPort(config.http.port);

HttpConfiguration https = new HttpConfiguration();
https.addCustomizer(new SecureRequestCustomizer());

SslContextFactory sslContextFactory = new SslContextFactory();
sslContextFactory.setKeyStorePath(EmbeddedServer.class.getResource(
       config.ssl.keyStorePath).toExternalForm());
sslContextFactory.setKeyStorePassword(config.ssl.keyStorePassword);
sslContextFactory.setKeyManagerPassword(config.ssl.keyManagerPassword);

ServerConnector sslConnector = new ServerConnector(server,
        new SslConnectionFactory(sslContextFactory, "http/1.1"),
        new HttpConnectionFactory(https));
sslConnector.setPort(config.ssl.port);

Connector[] conns = [connector, sslConnector]
server.setConnectors(conns);

// Start
server.start()
