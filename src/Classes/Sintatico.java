/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.Controle.Erro;
import Classes.Controle.Match;
import Classes.Controle.Simbolo;
import static Classes.Token.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author luish
 */
public class Sintatico extends Constantes
{

    private Lexico al_lexica;
    private Stack<String> pilha_simbolos;

    public Sintatico(String codeString)
    {
        pilha_simbolos = new Stack<>();
        lexemas_tokens_correspondidos = new ArrayList<>();
        erros = new ArrayList<>();
        code = codeString.toCharArray();
    }

    public Sintatico()
    {
        pilha_simbolos = new Stack<>();
        lexemas_tokens_correspondidos = new ArrayList<>();
        erros = new ArrayList<>();
    }

    public Stack<String> getPilha_simbolos()
    {
        return pilha_simbolos;
    }

    public void setPilha_simbolos(Stack<String> pilha_simbolos)
    {
        this.pilha_simbolos = pilha_simbolos;
    }

    public List<Match> getLexemas_tokens_correspondidos()
    {
        return lexemas_tokens_correspondidos;
    }

    public void setLexemas_tokens_correspondidos(List<Match> lexemas_tokens_correspondidos)
    {
        Sintatico.lexemas_tokens_correspondidos = lexemas_tokens_correspondidos;
    }

    public List<Erro> getErros()
    {
        return erros;
    }

    public void setErros(List<Erro> erros)
    {
        Sintatico.erros = erros;
    }

    public List<Simbolo> analise()
    {

        al_lexica = new Lexico();
        al_lexica.analise();

        return geraTabelaSimbolos();
    }

    private List<Simbolo> geraTabelaSimbolos()
    {
        List<Simbolo> tabela_simbolos = new ArrayList<>();
        List<Match> lt = lexemas_tokens_correspondidos;
        Match aux;
        
        for (int i = 0; i < lt.size(); i++)
        {
            aux = lt.get(i);
            //System.out.println(aux.getLexema().getPalavra());
            if (!aux.getToken().equals(tIdentificador))
            {
                addSimbolo(tabela_simbolos, new Simbolo(aux, "", ""));
            } else
            {
                String val = "";
                String tipo = "";
                if (i - 1 >= 0 && lt.get(i - 1).getToken().equals(tInicio_Linguagem))
                {
                    tipo = lt.get(i - 1).getToken().getIdToken().replace("t", "");
                } else
                {
                    //o +2 é pra pular atribuição
                    if (i + 2 < lt.size() && tValores.contains(lt.get(i + 2).getToken()) && tIgual.equals(lt.get(i + 1).getToken()))
                        val = lt.get(i + 2).getLexema().getPalavra();
                    if (tTipos.contains(lt.get(i - 1).getToken()))
                        tipo = lt.get(i - 1).getToken().getIdToken().replace("t", "");
                }
                addSimbolo(tabela_simbolos, new Simbolo(aux, val, tipo));
            }
        }
        /*
        System.out.println("\n\n\n\n\n\n\n");
        for (Simbolo t : tabela_simbolos)
        {
            System.out.println(t.getCadeia()+" = "+t.getValor());
        }
        */
        return tabela_simbolos;
    }

    private static void addSimbolo(List<Simbolo> tabela_simbolos, Simbolo simbolo)
    {
        Simbolo aux;

        if (simbolo.getToken().equals(tIdentificador))
        {
            int index = tabela_simbolos.indexOf(simbolo);
            if (index != -1)
            {
                aux = tabela_simbolos.get(index);
                if (simbolo.getTipo().isEmpty())
                    tabela_simbolos.add(simbolo);
                else
                {
                    erros.add(Erro.getError(Erro.variavel_Ja_Declarada, simbolo.getMatch().getLexema()));
                }
            } else if (simbolo.getTipo().isEmpty())
            {
                erros.add(Erro.getError(Erro.variavelNaoDeclarada, simbolo.getMatch().getLexema()));
            } else
                tabela_simbolos.add(simbolo);
        } else
            tabela_simbolos.add(simbolo);
    }
}
