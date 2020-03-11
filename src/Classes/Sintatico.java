/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.Controle.Controle;
import Classes.Controle.Erro;
import Classes.Controle.Match;
import Classes.Controle.Simbolo;
import Classes.Controle.TipoAnalise;
import static Classes.Token.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author luish
 */
public class Sintatico extends Constantes
{

    private Stack<Match> pilha_entrada;
    private Stack<Token> pilha;
    private Queue<Match> fila_sugestoes;
    //private List<Match> listaTokens;

    private Lexico al_lexica;
    private Stack<Match> pilha_simbolos;
    private int posToken;//para consumir os tokens extraidos pela analise lexica

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

    public Stack<Match> getPilha_simbolos()
    {
        return pilha_simbolos;
    }

    public void setPilha_simbolos(Stack<Match> pilha_simbolos)
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
        try
        {
            geraAnaliseSintatica();
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage() + "\n" + ex.getCause());
        }

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
        return tabela_simbolos;
    }

    private void addSimbolo(List<Simbolo> tabela_simbolos, Simbolo simbolo)
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

    private Stack<Match> geraPilhaEntrada()
    {
        pilha_entrada = new Stack<>();
        //pilha_simbolos.add(new Match(new Lexema("§", 0, 0), Token.tVazio));
        List<Match> l = lexemas_tokens_correspondidos;
        for (int i = l.size() - 1; i >= 0; i--)
        {
            pilha_entrada.push(l.get(i));
        }
        return pilha_entrada;
    }

    //Sem tabela
    private void geraAnaliseSintatica()
    {
        pilha_entrada = geraPilhaEntrada();
        //pilha = new Stack<>();
        //System.out.println(pilha_entrada.peek()+" - "+pilha_entrada.pop());
        fila_sugestoes = new LinkedList<>();
        int max = lexemas_tokens_correspondidos.size();
        posToken = 0;
        analisar(TipoAnalise.a_inicioPrograma);
        Token t;
        boolean flag = true;
        while (posToken < max && !pilha_entrada.isEmpty())
        {
            for (Token tv : Token.tTipos)
            {
                if (pilha_entrada.peek().getLexema().getPalavra().matches(tv.getRegex()))
                {
                    flag = false;
                    Controle retorno = analisar(TipoAnalise.ca_declaracao);
                    if (retorno != null && retorno instanceof Erro)
                    {
                        erros.add((Erro) retorno);
                    }
                    break;
                }
            }
            
            for (TipoAnalise ta : TipoAnalise.listaAnalises)
            {
                if (ta.getFirst().equals(pilha_entrada.peek().getToken()))
                {
                    flag = false;
                    Controle retorno = analisar(ta.getCodigo());
                    if (retorno != null && retorno instanceof Erro)
                    {
                        erros.add((Erro) retorno);
                    }
                    break;
                }
            }
            if (!flag)
            {
                System.out.println("Nenhuma regra para firs = " + pilha_entrada.peek().getLexema().getPalavra());
            }
        }
        //e aqui o show começa
    }

    private Controle analisar(int tipoAnalise)
    {
        Controle c = null;
        switch (tipoAnalise)
        {
            case TipoAnalise.a_inicioPrograma:
                c = al_inicioPrograma();
                break;
            case TipoAnalise.ca_declaracao:
                c = al_declaracao();
                break;
            case TipoAnalise.a_chaves:

                break;
            case TipoAnalise.ca_atribuicao:

                break;
            case TipoAnalise.ca_for:

                break;
            case TipoAnalise.ca_while:

                break;
            case TipoAnalise.a_parenteses:

                break;
            case TipoAnalise.ca_naoReconhecido:
                c = al_Token_naoReconhecido();
                break;
        }
        return c;
    }

    private Controle al_inicioPrograma()
    {
        if (pilha_entrada.size() - 2 < 0)
        {
            return regraNaoCompletada();
        }

        if (pilha_entrada.pop().getToken().equals(Token.tInicio_Linguagem)
                && pilha_entrada.pop().getToken().equals(Token.tIdentificador)
                && pilha_entrada.pop().getToken().equals(Token.tChave_abre))
        {
            return null;
        }
        return Erro.getError(Erro.Inicio_da_Linguagem, pilha_entrada.peek().getLexema());
    }

    private Controle al_Token_naoReconhecido()
    {
        posToken++;
        Erro error = Erro.getError(Erro.tokenNaoEncontrado, pilha_entrada.pop().getLexema());
        buscaTokenDeConexao();
        return error;
    }

    private void buscaTokenDeConexao()
    {
        while (!pilha_entrada.isEmpty() && token_De_Conexao.contains(pilha_entrada.pop()))
        {
            posToken++;
        }
    }

    private Controle al_declaracao()
    {

        if (pilha_entrada.size() - 3 < 0)
        {
            return regraNaoCompletada();
        } else
        {
            Token[] t = new Token[5];
            t[0] = pilha_entrada.pop().getToken();
            t[1] = pilha_entrada.pop().getToken();
            t[2] = pilha_entrada.pop().getToken();

            if (Token.tTipos.contains(t[0])
                    && t[1].equals(Token.tIdentificador)
                    && t[2].equals(Token.tPontoVirgula))
            {
                return null;
            } else if (pilha_entrada.size() - 2 > 0 && t[2].equals(Token.tIgual))
            {
                t[3] = pilha_entrada.pop().getToken();
                t[4] = pilha_entrada.pop().getToken();
                if (Token.tTipos.contains(t[0])
                        && t[1].equals(Token.tIdentificador)
                        && t[2].equals(Token.tIgual)
                        && Token.tValores.contains(t[3])
                        && t[4].equals(Token.tPontoVirgula))
                {
                    return null;
                }
            }
            return regraNaoCompletada();
        }
    }

    private Controle regraNaoCompletada()
    {
        Erro error = Erro.getError(Erro.naoCompletado, pilha_entrada.pop().getLexema());
        buscaTokenDeConexao();
        posToken++;
        return error;
    }

}
