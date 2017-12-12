/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import Proxy.Config.WebXmlConfiguraciones;
import Proxy.Model.Validacion;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
  

/**
 *
 * @author alancastro
 */
public class Receptor extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        CargarConfiguraciones();
        
        if(request.getRequestURI().equals( WebXmlConfiguraciones.HealthRequestUri() ) )
        {
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter pw =  response.getWriter();
            pw.write( WebXmlConfiguraciones.AsString() );
            pw.flush();
            //response.flushBuffer();
        }
        else if(request.getRequestURI().startsWith("/REDIS/") )
        {
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter pw =  response.getWriter();
            
            
            String[] items = request.getRequestURI().split("/");
            if(items[1] =="GET")
                pw.write(new ComunicadorRedis().Get(items[2]));
            else if(items[1]=="SET")
                new ComunicadorRedis().Set(items[2],items[3] );
            else if(items[1]=="KEYS")
            {
                Set<String> set = new ComunicadorRedis().Keys(items[2]);
                Object[] arr = set.toArray();
                for(int i = 0; i< set.size();i++){
                    pw.println(arr[i]);
                }
            }
            pw.flush();

        }
        else
        {
            if(!WebXmlConfiguraciones.UsarRedis())
                ComunicadorApiML.obtenerYRetornar(request.getRequestURI() , response);
            else
            {
                Validacion v = ValidadorAcceso.tieneAcceso(request);
                if( !v.HuboError )
                    ComunicadorApiML.obtenerYRetornar(request.getRequestURI() , response);
                else
                    response.sendError(v.HttpStatusCode, v.MensajeError );
            }
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    
    private void CargarConfiguraciones() {
        WebXmlConfiguraciones.Inicializar(this);
    }
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
  /*
    @Override
    public String getServletInfo() {
        return "Proxy mltp";
    }
*/
}
