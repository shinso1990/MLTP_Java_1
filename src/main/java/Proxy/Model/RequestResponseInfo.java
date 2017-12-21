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
    
    public  String IP;
    public  String URI;
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
    
    public Exception Exception;
    
    private HttpServletRequest Request;
    private HttpServletResponse Response;
    
    
    public RequestResponseInfo( HttpServletRequest request, HttpServletResponse response )    {
        
        
        Permitido = 0;
        Bloqueado = 0;
        Limitado = 0;
        IP = "";
        URI = "";
        CausaLimiteOBloqueo = "";
        HayError = false;
        TieneAcceso = false;
        MensajeError = "";
        
        Request = request;
        Response = response;
        
        String requestIp = request.getHeader("X-FORWARDED-FOR");  
        if (requestIp == null) {  
            requestIp = request.getRemoteAddr();  
        }
        IP = requestIp;  
        URI = Request.getRequestURI();
        Fecha = Calendar.getInstance();
        IPURI = IP + URI;
        
        
    }
    
    
    public Document toMongoDocument()    {
        Document res =  new Document();
        
        res.append("IP",IP);
        res.append("URI",URI);
        res.append("ResponseStatus",ResponseStatus);
        
        if(Fecha != null)
            res.append("Fecha",Fecha.getTime());
        res.append("Permitido",Permitido);
        res.append("Bloqueado",Bloqueado);
        res.append("Limitado",Limitado);
        int error = 0;
        
        if(HayError)
            error = 1;
        else 
            error = 0;
        res.append("Errado",error);
        
        
        String tcip = "";
        if(RedisConfigIP != null )
            tcip = RedisConfigIP.getCalendarForThisKey(Fecha);
        res.append("TipooContadorIP", tcip );
        
        String tcuri = "";
        if(RedisConfigURI != null )
            tcuri = RedisConfigURI.getCalendarForThisKey(Fecha);
        res.append("TipooContadorURI", tcuri );
        
        String tcuipri = "";
        if(RedisConfigIPURI != null )
            tcuipri = RedisConfigURI.getCalendarForThisKey(Fecha);
        res.append("TipooContadorIPURI", tcuipri );
        
        res.append("HayError",HayError );
        res.append("TieneAcceso",TieneAcceso );
        
        res.append("CausaLimiteOBloqueo", CausaLimiteOBloqueo );
        res.append("MensajeError", MensajeError );
        
        //VER QUE HACER CON ESTO:
        //res.append("RedisConfigIP",RedisConfigIP );
        //res.append("RedisConfigURI",RedisConfigURI );
        //res.append("RedisConfigIPURI",RedisConfigIPURI );
        
        String exMessage = "";
        if(Exception != null)
            exMessage = Exception.getMessage();
        res.append("ExceptionMessage", exMessage );
        
        return res;
    }
    
    
    public PrintWriter getWriter() throws IOException    {
        Writer = Response.getWriter();
        
        return Writer ;
    }
    private PrintWriter Writer;

    public void sendResponse() throws IOException    {
        
        this.Response.setStatus(this.ResponseStatus);
        if(this.TieneAcceso && !this.HayError)
        {
            if(this.Writer == null )
                this.Response.flushBuffer();
            else
                this.Writer.flush();
        }
        else
            this.Response.sendError(this.ResponseStatus, this.MensajeError );
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
