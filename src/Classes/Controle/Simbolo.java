/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Controle;

import Classes.Token;

/**
 *
 * @author luish
 */
public class Simbolo
{
    private Match match;
    private String cadeia;
    private Token token;
    private String valor;
    private String tipo;

    public Simbolo(Match match, String valor, String tipo)
    {
        this.match = match;
        this.valor = valor;
        this.tipo = tipo;
        this.cadeia = match.getLexema().getPalavra();
        this.token = match.getToken();
    }

    public Match getMatch()
    {
        return match;
    }

    public void setMatch(Match match)
    {
        this.match = match;
    }

    public String getCadeia()
    {
        return cadeia;
    }

    public void setCadeia(String cadeia)
    {
        this.cadeia = cadeia;
    }

    public Token getToken()
    {
        return token;
    }

    public void setToken(Token token)
    {
        this.token = token;
    }

    public String getValor()
    {
        return valor;
    }

    public void setValor(String valor)
    {
        this.valor = valor;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }    

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Simbolo)
        {
            Simbolo s = (Simbolo) obj;
            return this.cadeia.equals(s.cadeia) && this.token.equals(s.token);
        }
        return false;
    }
    
}
