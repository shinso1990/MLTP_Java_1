/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy;

import Proxy.Config.WebXmlConfiguraciones;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;


/**
 *
 * @author alancastro
 */
public class ComunicadorMongoDB {
    
    private MongoClient _mongoClient;
    

    private MongoClient getMongoInstance()    {
        if(_mongoClient == null)
        {
            String mongoClientUri = WebXmlConfiguraciones.GetMongoClientUri();
            
            _mongoClient = new MongoClient(
                new MongoClientURI( mongoClientUri )
            );
        }
        return _mongoClient;
    }
    
    private void closeMongoInstance()    {
        try
        {
            _mongoClient.close();
            _mongoClient = null;
        }
        finally{}
    }
    
    public void guardarEstadisticasDeUso(Document doc)    {
        MongoClient mc = null;
        try
        {
            mc = getMongoInstance();
            //mc = getMongoInstance();
            mc.getDatabase( WebXmlConfiguraciones.mongoDBDatabase() ).getCollection("InfoReqRes").insertOne(doc);
        }
        finally
        {
            try
            {            
                if(mc != null)
                    closeMongoInstance();
            }
            catch(Exception e){}
        }
        
    }
    
    public String getConfigOrDefault(String key)
    {
        String res = "";
        MongoClient mc = null;
        try
        {
            mc = getMongoInstance();
            //mc = getMongoInstance();
            Bson filter = Filters.eq("Key", key);
            List<Document> docs = mc.getDatabase( WebXmlConfiguraciones.mongoDBDatabase() )
                    .getCollection("ConfiguracionesProxy")
                    .find( filter ).into(new ArrayList<Document>() );
            Document configElem = null;
            if(docs.size()> 0 )
            {
                configElem = docs.get(0);
            }
            else
            {
                docs = mc.getDatabase( WebXmlConfiguraciones.mongoDBDatabase() )
                    .getCollection("ConfiguracionesProxy")
                    .find( Filters.eq("Key", "DEFAULT_CONFIG") ).into(new ArrayList<Document>() );
                configElem = docs.get(0);
                
            }
            String blo="";
            Boolean bloquada = configElem.getBoolean("Bloqueada");
            if( bloquada )
                blo = "1";
            else{
                blo = "0";
            }

            res = blo + "," + configElem.getInteger("CantidadTope").toString()  + "," + configElem.getInteger("TipoContador").toString();
            
        }
        finally
        {
            try
            {            
                if(mc != null)
                    closeMongoInstance();
            }
            catch(Exception e){}
            return res;
        }
    }
    
    
    
    
    
}
