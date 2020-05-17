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
    private String nome_conversor;
    private Integer escopo;
    
    public Instrucao(String metodoConversor)
    {
        cadeia_elementos = new ArrayList<>();
        this.nome_conversor = metodoConversor;
        try
        {
            conversor = Conversor.class.getDeclaredMethod(metodoConversor, Instrucao.class);

        } catch (NoSuchMethodException | SecurityException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void addCadeia_elementos(Match match)
    {
        cadeia_elementos.add(match);
        if(escopo == null)
            escopo = match.getEscopo();
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

    public String getNome_conversor()
    {
        return nome_conversor;
    }

    public void setNome_conversor(String nome_conversor)
    {
        this.nome_conversor = nome_conversor;
    }

    public Integer getEscopo()
    {
        return escopo;
    }

    public void setEscopo(Integer escopo)
    {
        this.escopo = escopo;
    }

    public void setCadeia_elementos(List<Match> cadeia_elementos)
    {
        this.cadeia_elementos = cadeia_elementos;
        if(escopo == null && !this.cadeia_elementos.isEmpty())
            escopo = cadeia_elementos.get(0).getEscopo();
    }

    public List<InstrucaoIntermediaria> toCodigoIntermediario()
    {
        List<InstrucaoIntermediaria> ret = null;
        try
        {
            ret = (List<InstrucaoIntermediaria>) conversor.invoke(Conversor.class, this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    @Override
    public String toString()
    {
        return "Instrucao{" + cadeia_elementos.toString() + "}, { Método = " + conversor.getName()+"} Escopo = "+escopo;
    }

}
