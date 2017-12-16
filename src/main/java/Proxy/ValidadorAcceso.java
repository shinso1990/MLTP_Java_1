/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import Proxy.Model.RequestResponseInfo;
import Proxy.Model.Validacion;
import java.util.Calendar;

/**
 *
 * @author alancastro
 */
public class ValidadorAcceso {
    
    public static void VerificarAcceso( RequestResponseInfo info )
    {
        ComunicadorRedis cr = null;
        try
        {
            cr = new ComunicadorRedis();

            info.RedisConfigIP =  cr.getRedisConfig(info.IP);
            info.RedisConfigURI = cr.getRedisConfig(info.URI);
            info.RedisConfigIPURI = cr.getRedisConfig( info.IPURI );
            
            if( info.RedisConfigIP.bloqueado())
                Validacion.Bloqueado(info, "IP");
            else if (info.RedisConfigURI.bloqueado())
                Validacion.Bloqueado(info, "URI");
            else if (info.RedisConfigIPURI.bloqueado())
                Validacion.Bloqueado(info, "IPURI");
            else
            {
                String redisKeyIp       = info.getRedisKeyIP();  
                String redisKeyUri      = info.getRedisKeyURI();
                String redisKeyIpUrl    = info.getRedisKeyIPURI();
                
                end_try: {
                    if( cr.Incr( redisKeyIp ) > info.RedisConfigIP.cantMaxReq() )
                    {
                        cr.Decr( redisKeyIp );
                        Validacion.Limitado(info, "IP");
                        break end_try;
                    }
                    if( cr.Incr(redisKeyUri  ) > info.RedisConfigURI.cantMaxReq())
                    {
                        cr.Decr( redisKeyIp );
                        cr.Decr(redisKeyUri );
                        Validacion.Limitado(info, "URI");
                        break end_try;
                    }
                    if( cr.Incr( redisKeyIpUrl ) > info.RedisConfigIPURI.cantMaxReq())
                    {
                        cr.Decr( redisKeyIp );
                        cr.Decr(redisKeyUri );
                        cr.Decr( redisKeyIpUrl );
                        Validacion.Limitado(info, "IPURI");
                        break end_try;
                    }
                    Validacion.Ok(info);
                }
            }
        }
        catch(Exception e)
        {
            Validacion.ErrorDesconocido(info, e);
        }
        finally
        {
            if(cr != null)
                cr.End();
        }
    }
    
}
