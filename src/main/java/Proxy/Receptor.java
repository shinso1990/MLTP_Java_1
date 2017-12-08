/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import Proxy.Config.WebXmlConfiguraciones;
import java.io.IOException;
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
        
        ValidadorAcceso validador = new ValidadorAcceso();
        if(validador.tieneAcceso(request))
        {
            new ComunicadorApiML().obtenerYRetornar(request.getRequestURI() , response);
        }
        else
        {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    
    private void CargarConfiguraciones()
    {
        WebXmlConfiguraciones.Inicializar(this);
    }
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Proxy mltp";
    }

}
