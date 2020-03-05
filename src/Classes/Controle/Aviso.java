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

    public Aviso(int codigo, String mensagem)
    {
        super(codigo, mensagem);
    }

    public Aviso(int codigo, String mensagem, Lexema lexema)
    {
        super(codigo, mensagem, lexema);
    }

}
