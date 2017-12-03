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
        //cal.set(Calendar.HOUR_OF_DAY, 0);
        //cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String calStr = cal.toString();
        
        String requestIp = request.getServerName();
        String requestUrl = request.getRequestURI();
        String ipUrl = requestIp + requestUrl;
        ComunicadorRedis cr = new ComunicadorRedis();
        
        RedisConfig rcIp = cr.getRedisConfig(requestIp);
        RedisConfig rcUrl = cr.getRedisConfig(requestUrl);
        RedisConfig rcIpUrl = cr.getRedisConfig( ipUrl );
        
        if( rcIp.bloqueado() || rcUrl.bloqueado() || rcIpUrl.bloqueado()){
            ComunicadorEstadisticas.guardarInformacionRequest( requestIp, requestUrl, rcIp.bloqueado(), rcUrl.bloqueado(), rcIpUrl.bloqueado(), cal);
            return false;
        }
        else
        {
            if( cr.Incr( requestIp + calStr ) > rcIp.cantMaxReq() )
            {
                cr.Decr(requestIp + calStr);
                ComunicadorEstadisticas.guardarCantMaxReq(cal, requestIp, requestUrl, "IP", "MAXREQCOUNTEXC");
                return false;
            }
            if( cr.Incr(requestUrl + calStr ) > rcUrl.cantMaxReq())
            {
                cr.Decr(requestIp + calStr );
                cr.Decr(requestUrl + calStr );
                ComunicadorEstadisticas.guardarCantMaxReq(cal, requestIp, requestUrl, "URL", "MAXREQCOUNTEXC");
                return false;
            }
            if( cr.Incr(ipUrl + calStr ) > rcIpUrl.cantMaxReq())
            {
                cr.Decr(requestIp + calStr );
                cr.Decr(requestUrl + calStr );
                cr.Decr(ipUrl + calStr );
                ComunicadorEstadisticas.guardarCantMaxReq(cal, requestIp, requestUrl, "IPURL", "MAXREQCOUNTEXC");
                return false;
            }
            
            cr.End();
            return true;
        }
       
    }
    
    
}
