/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy.Model;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alancastro
 */
public class Validacion {

    public static void ErrorDesconocido(RequestResponseInfo info, Exception e)    {
        info.MensajeError = "Se produjo un error interno";
        info.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        info.HayError = true;
        info.Exception = e;
    }
    
    public static void Ok(RequestResponseInfo info)    {
       info.setStatus(HttpServletResponse.SC_OK);
       info.Permitido = 1;
       info.TieneAcceso = true;
    }
    
    public static void Limitado(RequestResponseInfo info, String causa) {
        info.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        info.CausaLimiteOBloqueo = causa;
        info.TieneAcceso = false;
        info.MensajeError = "Se supero el límite permitido de request para " + causa;
        info.Limitado = 1;
    }
    
    public static void Bloqueado(RequestResponseInfo info, String causa) {
        info.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        info.CausaLimiteOBloqueo = causa;
        info.TieneAcceso = false;
        info.MensajeError = "Se produjo un bloqueo por " + causa;
        info.Bloqueado = 1;
    }

    public static void ErrorAlComunicarseConLaAPI(RequestResponseInfo info, IOException e) {
        info.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        info.CausaLimiteOBloqueo = e.getMessage();
        info.TieneAcceso = true;
        info.MensajeError = "Se produjo un error al comunicarse con la API" ;
        info.HayError = true;
    }
}
