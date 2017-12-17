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
import java.util.ArrayList;
import org.bson.Document;


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
            String mongoClientUri = WebXmlConfiguraciones.GetMongoClientUri();
             mc = new MongoClient( new MongoClientURI( mongoClientUri ) );
            mc.getDatabase( WebXmlConfiguraciones.mongoDBDatabase() ).getCollection("InfoReqRes").insertOne(doc);
        }
        catch(MongoTimeoutException mte )
        {
            int j = 4;
        }
        catch(Exception e)
        {
            int i = 5;
        }
        finally
        {
            
            try
            {            
                if(mc != null)
                mc.close();
            }
            catch(Exception e){}
        }
        
    }
    
}
