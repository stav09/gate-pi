package au.stav;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ActionServlet extends HttpServlet {

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) 
            throws ServletException, IOException
    {
        String[] uriParts = request.getRequestURI().split("/");
        String action = uriParts[uriParts.length - 1];

        try {
            Events.fire(Action.valueOf(action), null);
            response.setStatus(HttpServletResponse.SC_OK);
            
        } catch (IllegalArgumentException e) {            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
