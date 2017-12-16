/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy.Model;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;

/**
 *
 * @author alancastro
 */
public class RequestResponseInfo {
    
    public final String IP;
    public final String URI;
    public final String IPURI;
    
    public int ResponseStatus;
    private String FullRequest;
    public Calendar Fecha;
    
    public int Permitido;
    public int Bloqueado;
    public int Limitado;
    
    public String CausaLimiteOBloqueo;
    public String MensajeError;
    public Boolean HayError;
    public Boolean TieneAcceso; 
    
    public RedisConfig RedisConfigIP;
    public RedisConfig RedisConfigURI;
    public RedisConfig RedisConfigIPURI;
    
    private String FechaTipoContadorIP;
    private String FechaTipoContadorURI;
    private String FechaTipoContadorIPURI;
    
    private HttpServletRequest Request;
    private HttpServletResponse Response;
    
    public RequestResponseInfo( HttpServletRequest request, HttpServletResponse response )
    {
        Request = request;
        Response = response;
        HayError = false;
        TieneAcceso = false;
        MensajeError = "";
        String requestIp = request.getHeader("X-FORWARDED-FOR");  
        if (requestIp == null) {  
            requestIp = request.getRemoteAddr();  
        }
        IP = requestIp;  
        URI = Request.getRequestURI();
        Fecha = Calendar.getInstance();
        IPURI = IP + URI;
    }
    
    public Integer ResponseStatus(){
        if(_ResponseStatus == null) //no debería suceder ya que esto
            return HttpServletResponse.SC_OK;
        return _ResponseStatus;
    }
    
    public Document toMongoDocument()
    {
        Document res =  new Document();
        res.append("IP",IP);
        res.append("URI",URI);
        res.append("ResponseStatus",ResponseStatus);
        //res.append("FullRequest",FullRequest);
        res.append("Fecha",Fecha);
        //res.append("TipoContador",TipoContador);
        //res.append("FechaContador",FechaContador);
        res.append("Permitido",Permitido);
        res.append("Bloqueado",Bloqueado);
        res.append("Limitado",Limitado);
        return res;
    }
    
    
    private PrintWriter Writer;
    public PrintWriter getWriter() throws IOException
    {
        Writer = Response.getWriter();
        
        return Writer ;
    }
    
    public void sendResponse() throws IOException
    {
        Response.setStatus(this.ResponseStatus);
        if(Writer == null )
            Response.flushBuffer();
        else
            Writer.flush();
    }

    public void setStatus(int status) {
        this.ResponseStatus = status;
    }

    public void setContentType(String ct) {
        Response.setContentType(ct);
    }

    public void closeWriter() {
        if(Writer != null)
            Writer.close();
    }

    public String getRedisKeyIP() {
        return this.RedisConfigIP.getCalendarForThisKey(this.Fecha) + "_" + this.IP ;
    }
    public String getRedisKeyURI(){
        return this.RedisConfigURI.getCalendarForThisKey(this.Fecha) + "_" + this.URI ;
    }
    public String getRedisKeyIPURI(){
        return this.RedisConfigIPURI.getCalendarForThisKey(this.Fecha) + "_" + this.IP + this.URI ;
    }
} 
