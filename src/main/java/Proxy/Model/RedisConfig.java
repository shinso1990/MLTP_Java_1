/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxy.Model;

import java.util.Calendar;

/**
 *
 * @author alancastro
 */
public class RedisConfig {
    
    
    
    private Boolean _bloqueado;
    private Long _cantMaxReq;
    private Integer _unidadMedidaTiempo;
    
    public RedisConfig( Boolean bloqueado, Long cantMaxReq, Integer unidMedida  )
    {
        _bloqueado = bloqueado;
        _cantMaxReq = cantMaxReq;
        _unidadMedidaTiempo = unidMedida;
    }
    
    public RedisConfig(String redisValue)
    {
        String[] configs = redisValue.split(",");
        _bloqueado = Boolean.parseBoolean(configs[0]);
        _cantMaxReq = Long.parseLong(configs[1]);
        _unidadMedidaTiempo = Integer.parseInt(configs[2]);
    }
    
    public String asRedisValue()
    {
        String res = "";
        if(_bloqueado)
            res = "1," ;
        else res= "0,";
        res += _cantMaxReq.toString() + "," + _unidadMedidaTiempo.toString();
        return res;
        /*
        return "{ bloqueado: " + _bloqueado.toString() + ", " + 
                "cantMaxReq: " + _cantMaxReq.toString() + ", " +
                "unidadMedidaTiempo: " + _unidadMedidaTiempo.toString() + "}";
        */
    }
    
    public Boolean bloqueado()
    {
        return _bloqueado;
    }
    
    public Long cantMaxReq(){
        return _cantMaxReq;
    }

    public String getCalendarForThisKey( Calendar cal )
    {
        String anio = String.valueOf( cal.get(Calendar.YEAR) );
        String mes = Compleatar2DigitosConCeroIzq( String.valueOf( cal.get(Calendar.MONTH) ));
        String dia = Compleatar2DigitosConCeroIzq( String.valueOf( cal.get(Calendar.DAY_OF_MONTH) ));
        String hora = Compleatar2DigitosConCeroIzq( String.valueOf( cal.get(Calendar.HOUR_OF_DAY) ));
        String minuto = Compleatar2DigitosConCeroIzq( String.valueOf( cal.get(Calendar.MINUTE) ));
        
        switch(_unidadMedidaTiempo)
        {
            case TipoContador.MES:
                return anio + mes ;
            case TipoContador.DIA: 
                return anio + mes + dia;
            case TipoContador.HORA:
                return anio + mes + dia + hora;
            case TipoContador.MINUTO:
                return anio + mes + dia + hora + minuto;
            default:
                return "";
        }
    }
    
    private String Compleatar2DigitosConCeroIzq(String textoACompletar)
    {
        switch(textoACompletar.length())
        {
            case 0:
                return "00";
            case 1:
                return "0" + textoACompletar;
            default:
                return textoACompletar;
        }
    }

}
