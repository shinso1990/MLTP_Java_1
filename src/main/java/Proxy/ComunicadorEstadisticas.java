/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import java.util.Calendar;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author alancastro
 */
public class ComunicadorEstadisticas {
    
    public static void guardarInformacionRequestBloqueado( String ip, String url, Boolean ipBloqueada, 
            Boolean urlBloqueada, Boolean ipUrlBloqueada, Calendar fechaYHora )
    {
       // throw new NotImplementedException();
    }

    static void guardarCantMaxReq(Calendar cal, String requestIp, String requestUrl, String url, String maxreqcountexc) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static void guardarInformacionRequestOK(String requestIp, String requestUrl, Calendar cal) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
