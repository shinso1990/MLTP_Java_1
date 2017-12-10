/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

/**
 *
 * @author alancastro
 */
public class ErrorTracker {
    
    public static void logError(Exception e )
    {
        StringBuilder sb;
        sb = new StringBuilder();
        
        sb.append("{ Mensaje: '"); 
        sb.append(e.getMessage()  ); 
        sb.append("', StackTrace: '");
        sb.append(e.getStackTrace());
        sb.append("'}");
        
        new ComunicadorRedis().guardarError(sb.toString());
    }
    
    public static void logError(String error )
    {
        StringBuilder sb;
        sb = new StringBuilder();
        
        sb.append("{ Mensaje: '"); 
        sb.append(error); 
        sb.append("' }");
        
        new ComunicadorRedis().guardarError(sb.toString());
    }
    
}
