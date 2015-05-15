package au.stav;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class EventServlet extends WebSocketServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    public void configure(WebSocketServletFactory factory)
    {
        factory.register(WebSocket.class);
    }
}