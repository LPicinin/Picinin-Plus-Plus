/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Controle;

import Classes.Lexema;

/**
 *
 * @author luish
 */
public abstract class Controle implements Cloneable
{

    protected int codigo;
    protected String mensagem;
    private Lexema lexema;

    public Controle()
    {
    }

    public Controle(int codigo, String mensagem, Lexema lexema)
    {
        this.codigo = codigo;
        this.mensagem = mensagem;
        this.lexema = lexema;
    }

    public Controle(int codigo, String mensagem)
    {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
    }

    public String getMensagem()
    {
        return mensagem;
    }

    public void setMensagem(String mensagem)
    {
        this.mensagem = mensagem;
    }

    public Lexema getLexema()
    {
        return lexema;
    }

    public void setLexema(Lexema lexema)
    {
        this.lexema = lexema;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
