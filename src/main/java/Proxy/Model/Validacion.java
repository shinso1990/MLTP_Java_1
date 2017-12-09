/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy.Model;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alancastro
 */
public class Validacion {

    
    public Boolean HuboError;
    public String MensajeError;
    public int HttpStatusCode;
    
    public Validacion()
    {
        this.HuboError = false;
        this.MensajeError = "" ;
        this.HttpStatusCode = 0;
    }
    
    public Validacion(Boolean huboError, String mensajeError, int httpStatusCode  )
    {
        this.HuboError = huboError;
        this.MensajeError = mensajeError;
        this.HttpStatusCode = httpStatusCode;
    }
    
    public static Validacion ErrorDesconocido()
    {
       return  new Validacion(true,"Error interno",HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
    }
    
    public static Validacion SeSuperoLaCantMaximaDeRequestIp(String ip )
    {
       return  new Validacion(true,"Se supero la cantidad máxima de request de la IP: " + ip ,HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
    }
    public static Validacion SeSuperoLaCantMaximaDeRequestUrl(String url )
    {
       return  new Validacion(true,"Se supero la cantidad máxima de request a la URL: " + url ,HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
    }
    public static Validacion SeSuperoLaCantMaximaDeRequestIpUrl(String ipUrl )
    {
       return  new Validacion(true,"Se supero la cantidad máxima de request a la IpUrl: " + ipUrl ,HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
    }
    public static Validacion Ok()
    {
       return  new Validacion(false,""  ,HttpServletResponse.SC_OK);
    }
    public static Validacion Bloqueado() {
        return  new Validacion(true,"Su solicitud fue bloqueada", HttpServletResponse.SC_BAD_REQUEST );
    }
}
