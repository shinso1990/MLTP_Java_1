/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy.Config;

import java.util.Calendar;
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
       _keyListaErrores = s.getServletConfig().getServletContext().getInitParameter("keyListaErrores");
       _keyListaOk = s.getServletConfig().getServletContext().getInitParameter("keyListaOk");
       _keyListaBloqueados = s.getServletConfig().getServletContext().getInitParameter("keyListaBloqueados");
       _keyListaLimitados = s.getServletConfig().getServletContext().getInitParameter("keyListaLimitados");
       _usarRedis = s.getServletConfig().getServletContext().getInitParameter("usarRedis");
       _lastUpdate = Calendar.getInstance();
    }
    
    public static void Inicializar(Servlet s) {
        if(_singleInstance == null)
            WebXmlConfiguraciones.setInstance(  new WebXmlConfiguraciones(s) );
        else
        {
            //CADA 3 MINUTOS RECARGO por si modificaron las configuraciones
            //Esto es para no ir todo el tiempo al archivo web.xml
            Calendar nowm5 = Calendar.getInstance();
            nowm5.add( Calendar.MINUTE, -3 );
            if( nowm5.after( _singleInstance._lastUpdate ) )
                WebXmlConfiguraciones.setInstance(new WebXmlConfiguraciones(s));
        }
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
    public static String KeyListaOK() {
        return _singleInstance._keyListaOk;
    }
    public static String KeyListaBloqueados() {
        return _singleInstance._keyListaBloqueados;
    }
    public static String KeyListaErrores() {
        return _singleInstance._keyListaErrores;
    }
    public static String KeyListaLimitados() {
        return _singleInstance._keyListaLimitados;
    }
    public static Calendar LastUpdate() {
        return _singleInstance._lastUpdate;
    }
    public static Boolean UsarRedis()
    {
     return _singleInstance._usarRedis == "1";   
    }
    
    public WebXmlConfiguraciones getInstance() {
        return _singleInstance;
    }
    
    private String _redisIpConfig;
    private String _healthRequestUri;
    private String _apiMLUrl;
    private String _keyListaErrores;
    private String _keyListaOk;
    private String _keyListaBloqueados;
    private String _keyListaLimitados;
    private Calendar _lastUpdate;
    private String _usarRedis;
    //public String RedisIpConfig(){
        //if(_redisIpConfig == null)
        //    _redisIpConfig = s.getServletConfig().getServletContext().getInitParameter("RedisIpConf");
    //    return _redisIpConfig;
    //}
    
    
    
    
    
}
