package au.stav;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class Servlet extends WebSocketServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    public void configure(WebSocketServletFactory factory)
    {
        factory.register(Socket.class);
    }
}