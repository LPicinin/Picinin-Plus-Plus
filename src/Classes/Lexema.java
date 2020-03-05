/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author luish
 */
public class Lexema
{

    private String lexema;
    private int posParagrafo;
    private int posLinha;

    public Lexema(String lexema, int posParagrafo, int posLinha)
    {
        this.lexema = lexema;
        this.posParagrafo = posParagrafo;
        this.posLinha = posLinha;
    }

    public String getLexema()
    {
        return lexema;
    }

    public void setLexema(String lexema)
    {
        this.lexema = lexema;
    }

    public int getPosParagrafo()
    {
        return posParagrafo;
    }

    public void setPosParagrafo(int posParagrafo)
    {
        this.posParagrafo = posParagrafo;
    }

    public int getPosLinha()
    {
        return posLinha;
    }

    public void setPosLinha(int posLinha)
    {
        this.posLinha = posLinha;
    }

    @Override
    public String toString()
    {
        return lexema;
    }

}
