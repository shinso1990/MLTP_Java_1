/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import Proxy.Config.WebXmlConfiguraciones;
import Proxy.Model.RequestResponseInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    
    public static void obtenerYRetornar( RequestResponseInfo info ) {
        try
        {
            HttpResponse response = sendGetToMLAPI(info.URI);
            
            info.setStatus(response.getStatusLine().getStatusCode());
            
            setResponseServletContentType( response, info  );
            setResponseServletContent(response, info.getWriter());
        }
        catch(IOException e)
        {
            info.closeWriter();
            try
            {
                responseServlet.sendError((int)HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
                ErrorTracker.logError(e);
            }
            catch(IOException e1)
            { 
                ErrorTracker.logError(e1);
            }
        }
    }
    
    private static String getResponseContent( HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(
                  new InputStreamReader(response.getEntity().getContent()));
       StringBuffer result = new StringBuffer();
       String line = "";
       while ((line = rd.readLine()) != null) {
               result.append(line);
       }
       return result.toString();
    }

    private static void setResponseServletContentType(HttpResponse response, RequestResponseInfo info) {
        if(response.getHeaders("Content-Type").length > 0 ){
            String gv = response.getHeaders("Content-Type")[0].getValue();
            info.setContentType( gv );
        }
        else
            info.setContentType( "text/json" );
    }

    private static void setResponseServletContent(HttpResponse response, PrintWriter writter ) throws IOException  {
        String responseContent;
        responseContent = getResponseContent(response);  
        writter.write( responseContent );
    }

    private static void setResponseServletStatus(HttpResponse response, HttpServletResponse responseServlet) {
        responseServlet.setStatus( response.getStatusLine().getStatusCode() );
    }

    private static HttpResponse sendGetToMLAPI(String urn) throws IOException {
        String url = WebXmlConfiguraciones.ApiMLUrl() + urn ;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        return client.execute(request);
    }

    
}
