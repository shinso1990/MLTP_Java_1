/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import Proxy.Model.RedisConfig;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author alancastro
 */
public class ValidadorAcceso {
    
    
    
    public Boolean tieneAcceso( HttpServletRequest request )
    {
        Calendar cal = Calendar.getInstance();
        
        String requestIp = request.getServerName();
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
                ComunicadorEstadisticas.guardarInformacionRequestBloqueado( requestIp, requestUrl, rcIp.bloqueado(), rcUrl.bloqueado(), rcIpUrl.bloqueado(), cal);
                noSeEncuentraBloqueado = false;
            }
            else
            {
                String redisKeyIp = requestIp + rcIp.getCalendarForThisKey(cal);
                String redisKeyUrl = requestUrl + rcUrl.getCalendarForThisKey(cal);
                String redisKeyIpUrl = ipUrl + rcIpUrl.getCalendarForThisKey(cal);
                
                end_try: {
                    if( cr.Incr( redisKeyIp ) > rcIp.cantMaxReq() )
                    {
                        cr.Decr( redisKeyIp );
                        ComunicadorEstadisticas.guardarCantMaxReq(cal, requestIp, requestUrl, "IP", "MAXREQCOUNTEXC");
                        noSeEncuentraBloqueado = false;
                        break end_try;
                    }
                    if( noSeEncuentraBloqueado && cr.Incr( redisKeyUrl  ) > rcUrl.cantMaxReq())
                    {
                        cr.Decr( redisKeyIp );
                        cr.Decr( redisKeyUrl );
                        ComunicadorEstadisticas.guardarCantMaxReq(cal, requestIp, requestUrl, "URL", "MAXREQCOUNTEXC");
                        noSeEncuentraBloqueado = false;
                        break end_try;
                    }
                    if( noSeEncuentraBloqueado && cr.Incr( redisKeyIpUrl ) > rcIpUrl.cantMaxReq())
                    {
                        cr.Decr( redisKeyIp );
                        cr.Decr( redisKeyUrl );
                        cr.Decr( redisKeyIpUrl );
                        ComunicadorEstadisticas.guardarCantMaxReq(cal, requestIp, requestUrl, "IPURL", "MAXREQCOUNTEXC");
                        noSeEncuentraBloqueado = false;
                        break end_try;
                    }
                    ComunicadorEstadisticas.guardarInformacionRequestOK( requestIp, requestUrl, cal);
                    noSeEncuentraBloqueado = true;
                }
            }
        }
        catch(Exception e)
        {
            //TODO: mejorar
            new ErrorTracker().logError(e);
        }
        finally
        {
            cr.End();
            return noSeEncuentraBloqueado;
        }
       
    }
    
    
}
