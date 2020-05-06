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
public class Aviso extends Controle
{

    public static Aviso perca_De_Precisao = new Aviso(152, "Possivel perca de precisão");
    public static Aviso nunca_utilizado = new Aviso(153, "Instrução nunca é utilizada");
    public static Aviso constante_em_potencial = new Aviso(154, "Instrução é uma constante em potencial");

    public Aviso(int codigo, String mensagem)
    {
        super(codigo, mensagem);
    }

    public Aviso(int codigo, String mensagem, Lexema lexema)
    {
        super(codigo, mensagem, lexema);
    }

    public static Aviso getAviso(Aviso ab, Lexema lex)
    {
        return new Aviso(ab.codigo, ab.mensagem, lex);
    }

    @Override
    public String toString()
    {
        return "Aviso próximo à \'" + lexema.getPalavra() + "\' na Linha: " + (lexema.getPosParagrafo() + 1) + " - Aviso(" + codigo + ") - " + mensagem + "";
    }
}
