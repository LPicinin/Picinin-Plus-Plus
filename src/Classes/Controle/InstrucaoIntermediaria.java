/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Controle;

import java.util.List;

/**
 *
 * @author luish
 */
public class InstrucaoIntermediaria
{
    private List<Match> cadeia_elementos;
    private String nome_conversor;
    private Integer escopo;

    public InstrucaoIntermediaria(List<Match> cadeia_elementos, String nome_conversor, Integer escopo)
    {
        this.cadeia_elementos = cadeia_elementos;
        this.nome_conversor = nome_conversor;
        this.escopo = escopo;
    }

    public List<Match> getCadeia_elementos()
    {
        return cadeia_elementos;
    }

    public void setCadeia_elementos(List<Match> cadeia_elementos)
    {
        this.cadeia_elementos = cadeia_elementos;
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
    
    
}
