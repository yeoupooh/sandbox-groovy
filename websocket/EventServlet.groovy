import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@Grapes([
        @Grab(group = 'javax.servlet', module = 'javax.servlet-api', version = '3.0.1'),
        @Grab(group = 'org.eclipse.jetty', module = 'jetty-server', version = '9.2.11.v20150529'),
        @Grab(group = 'org.eclipse.jetty', module = 'jetty-servlet', version = '9.2.11.v20150529'),
])

@SuppressWarnings("serial")
public class EventServlet extends WebSocketServlet
{
    @Override
    public void configure(WebSocketServletFactory factory)
    {
        factory.register(EventSocket.class);
    }
}