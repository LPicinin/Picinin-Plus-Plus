/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import static Classes.Constantes.erros;
import static Classes.Constantes.lexemas_tokens_correspondidos;
import Classes.Controle.Simbolo;
import java.util.ArrayList;
import java.util.List;

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
    private List<Simbolo> tabela_Simbolos;

    public Compilador()
    {
    }

    public Compilador(String code)
    {
        this.code = code;
    }

    public void analisar()
    {
        limpaConstantes();
        al_sintatico = new Sintatico(code);
        tabela_Simbolos = al_sintatico.analise();
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

    public List<Simbolo> getTabela_Simbolos()
    {
        return tabela_Simbolos;
    }

    public void setTabela_Simbolos(List<Simbolo> tabela_Simbolos)
    {
        this.tabela_Simbolos = tabela_Simbolos;
    }

    private void limpaConstantes()
    {
        lexemas_tokens_correspondidos.clear();
        erros.clear();
    }

}
