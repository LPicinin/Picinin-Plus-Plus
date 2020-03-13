/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Controle;

import Classes.Lexema;
import Classes.Token;

/**
 *
 * @author luish
 */
public class Match
{

    private Lexema lexema;
    private Token token;
    private int posLista;

    public Match(Lexema lexema, Token token)
    {
        this.lexema = lexema;
        this.token = token;
    }

    public Lexema getLexema()
    {
        return lexema;
    }

    public void setLexema(Lexema lexema)
    {
        this.lexema = lexema;
    }

    public Token getToken()
    {
        return token;
    }

    public void setToken(Token token)
    {
        this.token = token;
    }

    public int getPosLista()
    {
        return posLista;
    }

    public void setPosLista(int posLista)
    {
        this.posLista = posLista;
    }

    @Override
    public String toString()
    {
        return "Match{" + "lexema=" + lexema + ", token=" + token + "}\n";
    }

}
