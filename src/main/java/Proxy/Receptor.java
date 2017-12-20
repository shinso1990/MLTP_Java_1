/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import Proxy.Config.WebXmlConfiguraciones;
import Proxy.Model.RequestResponseInfo;
import Proxy.Model.Validacion;
import java.io.IOException;
import java.io.PrintWriter;
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
        
        RequestResponseInfo info = new RequestResponseInfo(request, response);
        
        if(info.URI.equals( WebXmlConfiguraciones.HealthRequestUri() ) )
        {
            info.setStatus(HttpServletResponse.SC_OK);
            PrintWriter pw =  info.getWriter();
            pw.write( WebXmlConfiguraciones.AsString() );
            info.closeWriter();
        }
        else
        {
            if(!WebXmlConfiguraciones.UsarRedis())
                ComunicadorApiML.obtenerYRetornar(info);
            else
            {
                ValidadorAcceso.VerificarAcceso(info);
                
                if( info.TieneAcceso )
                    ComunicadorApiML.obtenerYRetornar(info);
            }
        }
        info.sendResponse();
        if(WebXmlConfiguraciones.GuardarEstadisticasDeUso()  ) 
            new ComunicadorMongoDB().guardarEstadisticasDeUso( info.toMongoDocument() );
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
