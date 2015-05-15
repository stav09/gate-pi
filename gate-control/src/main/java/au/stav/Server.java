package au.stav;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

public class Server implements Runnable {

    public static final String KEYSTORE_LOCATION = System.getProperty("keystore.file", "server-keystore.jks");
    public static final String KEYSTORE_PASSWORD = System.getProperty("keystore.password", "password");
    public static final String TRUSTSTORE_LOCATION = System.getProperty("truststore.file", "client-truststore.jks");
    public static final String TRUSTSTORE_PASSWORD = System.getProperty("truststore.password", "password");
    
    public void run()
    {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server();

        //HTTP
        
        ServerConnector publicConnector = new ServerConnector(server);
        publicConnector.setPort(8080);
        publicConnector.setName("Public");
        server.addConnector(publicConnector);
        
        // HTTPS
        
        SslContextFactory sslContextFactory = new SslContextFactory(KEYSTORE_LOCATION);
        sslContextFactory.setKeyStorePassword(KEYSTORE_PASSWORD);
        sslContextFactory.setTrustStorePath(TRUSTSTORE_LOCATION);
        sslContextFactory.setTrustStorePassword(TRUSTSTORE_PASSWORD);
        sslContextFactory.setNeedClientAuth(true);
        
        ServerConnector secureConnector = new ServerConnector(server, sslContextFactory);
        secureConnector.setPort(8443);
        secureConnector.setName("Secure");
        server.addConnector(secureConnector);

        
        // Static content
        
        String webDir = Server.class.getProtectionDomain().getCodeSource().getLocation().toExternalForm(); 
        WebAppContext webappContext = new WebAppContext(webDir, "/");
        webappContext.setWelcomeFiles(new String[] { "index.html" });
        webappContext.setVirtualHosts(new String[] { "@Secure" });
        
        
        // Action servlet (submit actions)
        
        ServletContextHandler actionContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        actionContext.setContextPath("/action");
        ServletHolder holderActions = new ServletHolder("http-action", ActionServlet.class);
        actionContext.addServlet(holderActions, "/*");
        actionContext.setVirtualHosts(new String[] { "@Secure" });
        
        
        // Websocket servlet (listen for events)
        
        ServletContextHandler websocketContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        websocketContext.setContextPath("/ws");
        ServletHolder holderEvents = new ServletHolder("ws-events", EventServlet.class);
        websocketContext.addServlet(holderEvents, "/events/*");
        websocketContext.setVirtualHosts(new String[] { "@Public" });
        
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { websocketContext, webappContext });
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
