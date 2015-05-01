package au.stav;

import java.io.IOException;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;


public class Socket extends WebSocketAdapter
{
    private Logger log = Log.getLogger(Socket.class);
    
    @Override
    public void onWebSocketConnect(Session session)
    {
        super.onWebSocketConnect(session);
        log.debug("Socket Connected: " + session);
        
        Events.on(Action.NOTIFY, (msg) -> notifyClient(msg));
        Events.fire(Action.STATUS_UPDATE, null);
    }
    
    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        log.debug("Received TEXT message: " + message);
        
        String[] msgPart = message.split(":", 2);
        try {
            Events.fire(Action.valueOf(msgPart[0]), (msgPart.length > 1) ? msgPart[1] : null);
            
        } catch (IllegalArgumentException e) {
            notifyClient("Invalid action: " + msgPart[0]);
        }
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode,reason);
        log.debug("Socket Closed: [" + statusCode + "] " + reason);
    }
    
    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        log.warn(cause);
    }
    
    private void notifyClient(String message) {
        try {
            getSession().getRemote().sendString(message);
            
        } catch (IOException e) {
            log.ignore(e);
        }
    }
}
