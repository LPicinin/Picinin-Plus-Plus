/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Controle;

import Classes.Lexema;
import Classes.Token;
import java.util.Objects;

/**
 *
 * @author luish
 */
public class Match
{

    private Lexema lexema;
    private Token token;
    private int escopo;
    private int posLista;

    public Match(Lexema lexema, Token token)
    {
        this.lexema = lexema;
        this.token = token;
        escopo = 0;
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

    public int getEscopo()
    {
        return escopo;
    }

    public void setEscopo(int escopo)
    {
        this.escopo = escopo;
    }

    @Override
    public String toString()
    {
        return lexema.getPalavra();
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Match other = (Match) obj;
        if (!Objects.equals(this.lexema.getPalavra(), other.lexema.getPalavra()))
            return false;
        return true;
    }

}
