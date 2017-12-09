/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy.Config;

import javax.servlet.Servlet;

/**
 *
 * @author alancastro
 */
public class WebXmlConfiguraciones {
    
    private WebXmlConfiguraciones(Servlet s) {
       _redisIpConfig =  s.getServletConfig().getServletContext().getInitParameter("RedisIpConf");
       _healthRequestUri = s.getServletConfig().getServletContext().getInitParameter("HealthRequestUri");
       _apiMLUrl = s.getServletConfig().getServletContext().getInitParameter("apiMLUrl");
    }
    
    public static void Inicializar(Servlet s) {
       WebXmlConfiguraciones.setInstance(  new WebXmlConfiguraciones(s) );
    }
    private static WebXmlConfiguraciones _singleInstance;
    public static void setInstance(WebXmlConfiguraciones nuevaInstancia ) {
        _singleInstance = nuevaInstancia;
    }
    
    public static String RedisIpConfig() {
        return _singleInstance._redisIpConfig;
    }
    public static String HealthRequestUri() {
        return _singleInstance._healthRequestUri;
    }
    public static String ApiMLUrl() {
        return _singleInstance._apiMLUrl;
    }
    
    public WebXmlConfiguraciones getInstance() {
        return _singleInstance;
    }
    
    private String _redisIpConfig;
    private String _healthRequestUri;
    private String _apiMLUrl;

    //public String RedisIpConfig(){
        //if(_redisIpConfig == null)
        //    _redisIpConfig = s.getServletConfig().getServletContext().getInitParameter("RedisIpConf");
    //    return _redisIpConfig;
    //}
    
    
    
    
    
}
