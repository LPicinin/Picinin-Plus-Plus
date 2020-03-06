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

    private String palavra;
    private int posParagrafo;
    private int posLinha;

    public Lexema(String palavra, int posParagrafo, int posLinha)
    {
        this.palavra = palavra;
        this.posParagrafo = posParagrafo;
        this.posLinha = posLinha;
    }

    public String getPalavra()
    {
        return palavra;
    }

    public void setPalavra(String palavra)
    {
        this.palavra = palavra;
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
        return palavra;
    }

}
