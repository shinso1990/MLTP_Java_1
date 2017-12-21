/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import Proxy.Config.WebXmlConfiguraciones;
import Proxy.Model.RedisConfig;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author alancastro
 */
public class ComunicadorRedis {
    JedisPool _pool;
    Jedis _jedis;
    
    public ComunicadorRedis() {
        //_pool = new JedisPool(new JedisPoolConfig(), WebXmlConfiguraciones.RedisIpConfig());
        //_jedis = null;
    }
    
    private Jedis getJedisInstance() {
        if(_jedis == null)
        //    _jedis = _pool.getResource();
            _jedis = new Jedis(WebXmlConfiguraciones.RedisIpConfig());
        return _jedis;
    }
    
    public RedisConfig getRedisConfig(String key) {
        String value = getJedisInstance().get( key );
        RedisConfig rc = null;
        if(value == null)
            rc = setAndGetDefaultRedisConfig(key);
        else
            rc = new RedisConfig(value);
        return rc;
    }

    private RedisConfig setAndGetDefaultRedisConfig(String key) {
        
        ComunicadorMongoDB cmdb = new ComunicadorMongoDB();
        String config = cmdb.getConfigOrDefault(key).trim();
        if(config.equals("") )
        {
            config = getJedisInstance().get("DEFAULT_CONFIG");
             if(config == null)
                config = WebXmlConfiguraciones.LastDefaultConfig();  //NO DEBER�A USARSE ESTA CONFIGURACI�N
        }
        
        getJedisInstance().set(key, config);

        return new RedisConfig(config);
    }

    Long Incr(String key) {
        return getJedisInstance().incr(key);
    }

    Long Decr(String key) {
        return getJedisInstance().decr(key);
    }

    public String Get(String key)
    {
        return getJedisInstance().get(key);
    } 
    public void Set(String key, String value)
    {
        getJedisInstance().set(key,value);
    } 
    public Set<String> Keys(String pattern)
    {
        return getJedisInstance().keys(pattern);
    } 
    
     public void End() {
        _jedis.close();
        //_pool.destroy();
    }

    void guardarBloqueo(String value) {
        String key = WebXmlConfiguraciones.KeyListaBloqueados();
        getJedisInstance().lpush(key, value);
    }
    
    void guardarRequestOK(String value) {
        String key = WebXmlConfiguraciones.KeyListaOK();
        getJedisInstance().lpush(key, value);
    }
    
    void guardarRequestLimiteSuperado(String value) {
        String key = WebXmlConfiguraciones.KeyListaLimitados();
        getJedisInstance().lpush(key, value);
    }

    void guardarError(String value) {
        String key = WebXmlConfiguraciones.KeyListaErrores();
        getJedisInstance().lpush(key, value);
    }
}
