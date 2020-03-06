/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author luish
 */
public class Compilador
{

    //identificadoe - lexema - (se id tipo) - variavel(escopo, tipo e valor)
    private String code;
    private List<Object> erros_avisos;
    private List<Object> matchs;
    private Sintatico al_sintatico;

    public Compilador()
    {
    }

    public Compilador(String code)
    {
        this.code = code;
    }

    public void analisar()
    {
        erros_avisos = new ArrayList<>();
        al_sintatico = new Sintatico();
        al_sintatico.analise(new Lexema(code, 0, 0));
        erros_avisos = new ArrayList<>(al_sintatico.getErros());
        matchs = new ArrayList<>(al_sintatico.getLexemas_tokens_correspondidos());
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public List<Object> getErros_avisos()
    {
        return erros_avisos;
    }

    public void setErros_avisos(List<Object> erros_avisos)
    {
        this.erros_avisos = erros_avisos;
    }

    public List<Object> getMatchs()
    {
        return matchs;
    }

    public void setMatchs(List<Object> matchs)
    {
        this.matchs = matchs;
    }

}
