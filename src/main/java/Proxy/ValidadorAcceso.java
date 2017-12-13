/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import Proxy.Model.RedisConfig;
import Proxy.Model.Validacion;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author alancastro
 */
public class ValidadorAcceso {
    
    public static Validacion tieneAcceso( HttpServletRequest request )
    {
        Validacion res = new Validacion();
        Calendar cal = Calendar.getInstance();
        
        String requestIp = request.getHeader("X-FORWARDED-FOR");  
            if (requestIp == null) {  
              requestIp = request.getRemoteAddr();  
        }
        
        //String requestIp = request.getRemoteAddr();
        String requestUrl = request.getRequestURI();
        String ipUrl = requestIp + requestUrl;
        ComunicadorRedis cr = null;
        Boolean noSeEncuentraBloqueado = true;
        
        try
        {
            cr = new ComunicadorRedis();

            RedisConfig rcIp = cr.getRedisConfig(requestIp);
            RedisConfig rcUrl = cr.getRedisConfig(requestUrl);
            RedisConfig rcIpUrl = cr.getRedisConfig( ipUrl );
            
            if( rcIp.bloqueado() || rcUrl.bloqueado() || rcIpUrl.bloqueado()){
                ComunicadorEstadisticas.guardarInformacionRequestBloqueado( requestIp, requestUrl, rcIp.bloqueado(), rcUrl.bloqueado(), rcIpUrl.bloqueado(), cal, cr);
                res = Validacion.Bloqueado();
            }
            else
            {
                String redisKeyIp       = rcIp.getCalendarForThisKey(cal) + "_" + requestIp;
                String redisKeyUrl      = rcUrl.getCalendarForThisKey(cal) + "_"+ requestUrl;
                String redisKeyIpUrl    = rcIpUrl.getCalendarForThisKey(cal) + "_" + ipUrl;
                
                end_try: {
                    if( cr.Incr( redisKeyIp ) > rcIp.cantMaxReq() )
                    {
                        cr.Decr( redisKeyIp );
                        ComunicadorEstadisticas.guardarCantMaxReq(cal, requestIp, requestUrl, "IP", "MAXREQCOUNTEXC", cr);
                        res = Validacion.SeSuperoLaCantMaximaDeRequestIp(requestIp);
                        break end_try;
                    }
                    if( cr.Incr( redisKeyUrl  ) > rcUrl.cantMaxReq())
                    {
                        cr.Decr( redisKeyIp );
                        cr.Decr( redisKeyUrl );
                        ComunicadorEstadisticas.guardarCantMaxReq(cal, requestIp, requestUrl, "URL", "MAXREQCOUNTEXC", cr);
                        res = Validacion.SeSuperoLaCantMaximaDeRequestUrl(requestUrl);
                        break end_try;
                    }
                    if( cr.Incr( redisKeyIpUrl ) > rcIpUrl.cantMaxReq())
                    {
                        cr.Decr( redisKeyIp );
                        cr.Decr( redisKeyUrl );
                        cr.Decr( redisKeyIpUrl );
                        ComunicadorEstadisticas.guardarCantMaxReq(cal, requestIp, requestUrl, "IPURL", "MAXREQCOUNTEXC", cr);
                        res = Validacion.SeSuperoLaCantMaximaDeRequestIpUrl(ipUrl);
                        break end_try;
                    }
                    ComunicadorEstadisticas.guardarInformacionRequestOK( requestIp, requestUrl, cal, cr);
                    res = Validacion.Ok();
                }
            }
        }
        catch(Exception e)
        {
            //TODO: mejorar
            new ErrorTracker().logError(e);
            res = Validacion.ErrorDesconocido();
        }
        finally
        {
            if(cr != null)
                cr.End();
            return res;
        }
       
    }
    
    
}
