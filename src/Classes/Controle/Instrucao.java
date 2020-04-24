/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Controle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luish
 */
public class Instrucao
{
    private List<Match> cadeia_elementos;
    private Method conversor;

    public Instrucao(String metodoConversor)
    {
        cadeia_elementos = new ArrayList<>();
        try
        {
            conversor = Conversor.class.getDeclaredMethod(metodoConversor, ArrayList.class);
            
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public List<Match> getCadeia_elementos()
    {
        return cadeia_elementos;
    }

    public void setCadeia_elementos(List<Match> cadeia_elementos)
    {
        this.cadeia_elementos = cadeia_elementos;
    }
    
    public List<String> toCodigoIntermediario()
    {
        List<String> ret = null;
        try
        {
            ret = (List<String>) conversor.invoke(Conversor.class, cadeia_elementos);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            System.out.println(e.getMessage());
        }
        return ret;
    }
}
