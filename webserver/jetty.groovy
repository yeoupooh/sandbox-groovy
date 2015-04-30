import groovy.servlet.GroovyServlet
import groovy.servlet.TemplateServlet
import org.eclipse.jetty.server.*
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.*
import org.eclipse.jetty.servlet.*

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Grapes([
        @Grab(group = 'org.eclipse.jetty.aggregate', module = 'jetty-server', version = '8.1.7.v20120910'),
        @Grab(group = 'org.eclipse.jetty.aggregate', module = 'jetty-servlet', version = '8.1.7.v20120910'),
        @Grab(group = 'javax.servlet', module = 'javax.servlet-api', version = '3.0.1')])

class HelloHandler extends AbstractHandler {
    public String greeting
    String body

    HelloHandler() {
        this("Hello World")
    }

    HelloHandler(msg) {
        this.greeting = msg;
    }

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException,
            ServletException {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        out.println("<h1>" + greeting + "</h1>");
        if (body != null) {
            out.println(body);
        }

        baseRequest.setHandled(true);
    }
}

def runServer() {
    Server server = new Server(8080)

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

    ServletContextHandler contextScripts = new ServletContextHandler(contexts, "/scripts", ServletContextHandler.SESSIONS);
    contextScripts.with {
        resourceBase = "scripts"
        addServlet(TemplateServlet, "*.gsp")
        addServlet(GroovyServlet, '*.gtpl')
        addServlet(GroovyServlet, '*.groovy')  // All files ending with .groovy will be served.
    }

    ContextHandler contextRoot = new ContextHandler(contexts, "/")
    contextRoot.setContextPath("/");
    contextRoot.setHandler(new HelloHandler("Root Hello"));

    ContextHandler contextFR = new ContextHandler(contexts, "/fr");
    contextFR.setHandler(new HelloHandler("Bonjoir"));

    ContextHandler contextIT = new ContextHandler(contexts, "/it");
    contextIT.setHandler(new HelloHandler("Bongiorno"));

    ContextHandler contextV = new ContextHandler(contexts, "/");
    contextV.setVirtualHosts("127.0.0.2");
    contextV.setHandler(new HelloHandler("Virtual Hello"));

    server.start()
}

runServer()
