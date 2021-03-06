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

    public static String mongoDBDatabase() {
        return _singleInstance._mongoDBDatabase;
    }

    public static String GetMongoClientUri() {
        return _singleInstance._mongoClientUri;
    }
    private final String _mongoClientUri;

    
    private WebXmlConfiguraciones(Servlet s) {
       _redisIpConfig =  s.getServletConfig().getServletContext().getInitParameter("RedisIpConf");
       _healthRequestUri = s.getServletConfig().getServletContext().getInitParameter("HealthRequestUri");
       _apiMLUrl = s.getServletConfig().getServletContext().getInitParameter("apiMLUrl");
       _keyListaErrores = s.getServletConfig().getServletContext().getInitParameter("keyListaErrores");
       _keyListaOk = s.getServletConfig().getServletContext().getInitParameter("keyListaOk");
       _keyListaBloqueados = s.getServletConfig().getServletContext().getInitParameter("keyListaBloqueados");
       _keyListaLimitados = s.getServletConfig().getServletContext().getInitParameter("keyListaLimitados");
       _usarRedis = s.getServletConfig().getServletContext().getInitParameter("usarRedis");
       _lastDefaultConfig = s.getServletConfig().getServletContext().getInitParameter("lastDefaultConfig");
       _mongoClientUri = s.getServletConfig().getServletContext().getInitParameter("mongoClientUri");
       _guardarEstadisticasDeUso = s.getServletConfig().getServletContext().getInitParameter("guardarEstadisticasDeUso");
       _mongoDBDatabase = s.getServletConfig().getServletContext().getInitParameter("mongoDBDatabase");
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
    public static Boolean UsarRedis() {
     return _singleInstance._usarRedis.equals("1");   
    }
    public static String LastDefaultConfig() {
        return _singleInstance._lastDefaultConfig;
    }
    public WebXmlConfiguraciones getInstance() {
        return _singleInstance;
    }
    public static String GetMongoHosts() {
        return _singleInstance._mongoDBHosts;
    }
    public static boolean GuardarEstadisticasDeUso() {
        return _singleInstance._guardarEstadisticasDeUso.equals("1");
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
    private String _lastDefaultConfig;
    private String _mongoDBHosts;
    private String _guardarEstadisticasDeUso;
    private String _mongoDBDatabase;
    public static String AsString()
    {
        try{
        return "{ \"redisIp\":\"" + RedisIpConfig() + "\", "+
                "\"apiMl\":\"" + ApiMLUrl()+ "\", "+
                "\"healthRequestPath\":\"" + HealthRequestUri()+ "\", "+
                "\"keyListaErrores\":\"" + KeyListaErrores()+ "\", "+
                "\"keyListaOK\":\"" + KeyListaOK()+ "\", "+
                "\"keyListaLimitados\":\"" + KeyListaLimitados()+ "\", "+
                "\"UsarRedis\":\"" + UsarRedis().toString()  + "\", "+
                "\"LastDefaultConfig\":\"" + LastDefaultConfig() + "\", "+
                "\"GuardarEstadisticasDeUso\":\"" + GuardarEstadisticasDeUso() + "\" "+
                "}";
        }
        catch(Exception e)
        {
            return "error";
        }
    }
    
    
    
    
    
    
}
