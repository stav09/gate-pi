package au.stav;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class Server {

    public static void main(String[] args)
    {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);
        
        // Static content 
        String webDir = Server.class.getProtectionDomain().getCodeSource().getLocation().toExternalForm(); 
        WebAppContext webappContext = new WebAppContext(webDir, "/");
        webappContext.setWelcomeFiles(new String[] { "index.html" });
        
        // Websocket servlet
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/ws");
        ServletHolder holderEvents = new ServletHolder("ws-events", Servlet.class);
        contextHandler.addServlet(holderEvents, "/events/*");
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { contextHandler, webappContext });
        server.setHandler(handlers);

        try
        {
            server.start();
            server.dump(System.err);
            server.join();
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }

}
