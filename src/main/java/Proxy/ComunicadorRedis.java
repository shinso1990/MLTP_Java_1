/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import Proxy.Config.WebXmlConfiguraciones;
import Proxy.Model.RedisConfig;
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
    
    
    
    //TODO: MEJORAR GET CONFIG
    public ComunicadorRedis()
    {
        _pool = new JedisPool(new JedisPoolConfig(), WebXmlConfiguraciones.RedisIpConfig());
        _jedis = null;
    }
    
    private Jedis getJedisInstance()
    {
        if(_jedis == null)
            _jedis = _pool.getResource();
        return _jedis;
    }
    
    public RedisConfig getRedisConfig(String key)
    {
        String value = getJedisInstance().get( key );
        RedisConfig rc = null;
        if(value == null)
            rc = setAndGetDefaultRedisConfig(key);
        else
            rc = new RedisConfig(value);
        return rc;
    }

    private RedisConfig setAndGetDefaultRedisConfig(String key) {
        String drc = getJedisInstance().get( "REDIS_DEFAULT_CONFIG");
        getJedisInstance().set(key, drc);
                
                
        return new RedisConfig(drc);
    }

    Long Incr(String key) {
        return getJedisInstance().incr(key);
    }

    Long Decr(String key) {
        return getJedisInstance().decr(key);
    }
    
     public void End()
    {
        _jedis.close();
        _pool.destroy();
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
