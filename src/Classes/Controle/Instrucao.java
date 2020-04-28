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
            conversor = Conversor.class.getDeclaredMethod(metodoConversor, List.class);

        } catch (NoSuchMethodException | SecurityException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void addCadeia_elementos(Match match)
    {
        cadeia_elementos.add(match);
    }

    public List<Match> getCadeia_elementos()
    {
        return cadeia_elementos;
    }

    public Method getConversor()
    {
        return conversor;
    }

    public void setConversor(Method conversor)
    {
        this.conversor = conversor;
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

    @Override
    public String toString()
    {
        return "Instrucao{" + cadeia_elementos.toString() + "} Método = " + conversor.getName();
    }

}
