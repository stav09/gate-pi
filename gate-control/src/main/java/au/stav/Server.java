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
        SslContextFactory sslContextFactory = new SslContextFactory(KEYSTORE_LOCATION);
        sslContextFactory.setKeyStorePassword(KEYSTORE_PASSWORD);
        sslContextFactory.setTrustStorePath(TRUSTSTORE_LOCATION);
        sslContextFactory.setTrustStorePassword(TRUSTSTORE_PASSWORD);
        sslContextFactory.setNeedClientAuth(true);
 
        // create a https connector
        ServerConnector connector = new ServerConnector(server, sslContextFactory);
        connector.setPort(8443);
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
