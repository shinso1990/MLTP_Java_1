/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author alancastro
 */
public class ComunicadorApiML {
    
    //TODO: Sacar de la bd
    public String apiMLUrl = "https://api.mercadolibre.com";
    public ErrorTracker errorTracker = new ErrorTracker();
    public void obtenerYRetornar( String urn, HttpServletResponse responseServlet )
    {
        try
        {
            HttpResponse response = sendGetToMLAPI(urn); ;
            setResponseServletStatus(response, responseServlet );
            setResponseServletContentType( response, responseServlet );
            setResponseServletContent(response, responseServlet);
            try
            {
                if(!responseServlet.isCommitted())
                    responseServlet.flushBuffer();
            }
            catch(IOException e1)
            { 
                errorTracker.logError(e1);
            }
        }
        catch(IOException e)
        {
            try
            {
                responseServlet.sendError((int)HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
                errorTracker.logError(e);
            }
            catch(IOException e1)
            { 
                errorTracker.logError(e1);
            }
        }
    }
    
    private String getResponseContent( HttpResponse response) throws IOException 
    {
        BufferedReader rd = new BufferedReader(
                  new InputStreamReader(response.getEntity().getContent()));
       StringBuffer result = new StringBuffer();
       String line = "";
       while ((line = rd.readLine()) != null) {
               result.append(line);
       }
       return result.toString();
    }

    private void setResponseServletContentType(HttpResponse response, HttpServletResponse responseServlet) {
        if(response.getHeaders("Content-Type").length > 0 ){
            String gv = response.getHeaders("Content-Type")[0].getValue();
            responseServlet.setContentType( gv );
        }
        else {
            responseServlet.setContentType( "text/json" );
        }
    }

    private void setResponseServletContent(HttpResponse response, HttpServletResponse responseServlet)  {
        try
        {
            String responseContent = getResponseContent(response);
            responseServlet.getWriter().write( responseContent );
            responseServlet.setContentLength( Integer.parseInt( response.getHeaders("Content-Length")[0].toString() ) ); //responseContent.length() );
        }
        catch( IOException e )
        {
            try
            {
                responseServlet.sendError( (int)HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
                errorTracker.logError(e);
            }
            catch(IOException e1)
            {
                errorTracker.logError(e1);
            }
        }
    }

    private void setResponseServletStatus(HttpResponse response, HttpServletResponse responseServlet) {
        responseServlet.setStatus( response.getStatusLine().getStatusCode() );
    }

    private HttpResponse sendGetToMLAPI(String urn) throws IOException {
        String url = apiMLUrl + urn ;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        return client.execute(request);
    }
    
}
