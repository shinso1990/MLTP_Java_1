/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import java.util.Calendar;

/**
 *
 * @author alancastro
 */
public class ComunicadorEstadisticas {
    
    public static void guardarInformacionRequestBloqueado( String ip, String url, Boolean ipBloqueada, 
            Boolean urlBloqueada, Boolean ipUrlBloqueada, Calendar fechaYHora, ComunicadorRedis cr ) {
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
        sb.append(fechaYHora.toString());
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
        sb.append(cal.toString());
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
        sb.append(cal.toString());
        sb.append("'}");
        
       cr.guardarBloqueo(sb.toString());
    }
    
}
