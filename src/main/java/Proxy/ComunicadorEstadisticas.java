/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import Proxy.Config.WebXmlConfiguraciones;
import Proxy.Model.RequestResponseInfo;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;
import java.util.ArrayList;


/**
 *
 * @author alancastro
 */
public class ComunicadorEstadisticas {
    
    public static void guardarInformacionRequestBloqueado( String ip, String url, Boolean ipBloqueada, 
            Boolean urlBloqueada, Boolean ipUrlBloqueada, Calendar cal, ComunicadorRedis cr ) {
        StringBuilder sb;
        sb = new StringBuilder();
        
        sb.append("{ ");
        sb.append("IP: '");
        sb.append(ip);
        sb.append("',");
        sb.append("URL: '");
        sb.append(url); 
        sb.append("',");
        sb.append("Fecha: '"); 
        sb.append(CalendarToString(cal));
        sb.append("',");
        sb.append("Bloqueada: '");
        if(ipBloqueada)
                sb.append("IP'");
        else if(urlBloqueada)
                sb.append("URL'");
        else if(ipUrlBloqueada)
                sb.append("IPURL'");   
        else
            sb.append("NONE");
        sb.append("}");
        
       cr.guardarBloqueo(sb.toString());
    }

    static void guardarCantMaxReq(Calendar cal, String requestIp, String requestUrl, String quienSupero, String maxreqcountexc, ComunicadorRedis cr) {
        StringBuilder sb;
        sb = new StringBuilder();
        
        sb.append("{ ");
        sb.append("IP: '");
        sb.append(requestIp);
        sb.append("',");
        sb.append("URL: '");
        sb.append(requestUrl); 
        sb.append("',");
        sb.append("Fecha: '"); 
        sb.append(CalendarToString(cal));
        sb.append("',");
        sb.append("QuienSuperoMRC: '");
        sb.append(quienSupero);
        sb.append("'");
        sb.append("}");
        
       cr.guardarRequestLimiteSuperado(sb.toString());
    }

    static void guardarInformacionRequestOK(String requestIp, String requestUrl, Calendar cal, ComunicadorRedis cr) {
        StringBuilder sb;
        sb = new StringBuilder();
        
        sb.append("{ ");
        sb.append("IP: '");
        sb.append(requestIp);
        sb.append("',");
        sb.append("URL: '");
        sb.append(requestUrl); 
        sb.append("',");
        sb.append("Fecha: '"); 
        sb.append( CalendarToString(cal) );
        sb.append("'}");
        
       cr.guardarBloqueo(sb.toString());
    }
    
    private static String CalendarToString(Calendar cal)
    {
        SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmss");
        return formato.format(cal.getTime());
    }



    private MongoClient _mongoClient;
    private MongoClient getMongoInstance()
    {
        if(_mongoClient == null)
        {
            String hosts = WebXmlConfiguraciones.GetMongoHosts();
            String[] hostAndPortArray = hosts.split(",");
            
            ArrayList<ServerAddress> sal = new ArrayList<ServerAddress>();
            for(int i = 0; i< hostAndPortArray.length; i++ )
            {
                String[] hostAndPort = hostAndPortArray[i].split(":");
                if(hostAndPort.length >1  )
                    sal.add( new ServerAddress( hostAndPort[0], Integer.parseInt( hostAndPort[1] ) ) );
                else
                    sal.add( new ServerAddress( hostAndPort[0], 27017 ) );
            }
            _mongoClient = new MongoClient(sal);
        }
        return _mongoClient;
    }
    private void closeMongoInstance()
    {
        _mongoClient.close();
        _mongoClient = null;
    }
    
    
    public void guardarEstadisticasDeUso(RequestResponseInfo irr)
    {
        try
        {
            MongoClient mc = getMongoInstance();
            mc.getDatabase("admin").getCollection("InfoReqRes").insertOne(irr.toMongoDocument() );
        }
        finally
        {
            closeMongoInstance();
        }
        
    }
    
    
    
}
