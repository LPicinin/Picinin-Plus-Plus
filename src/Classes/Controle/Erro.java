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
public class Erro extends Controle
{

    public static final Erro tokenNaoEncontrado = new Erro(404, "Não foi possível encontrar um token correspondente");
    public static final Erro tokenFinalDeCadeiaInesperada = new Erro(405, "character final esperado");

    public Erro(int codigo, String mensagem)
    {
        super(codigo, mensagem);
    }

    public Erro(int codigo, String mensagem, Lexema lexema)
    {
        super(codigo, mensagem, lexema);
    }

    @Override
    public String toString()
    {
        return "Erro{" + codigo + " - " + mensagem + "}";
    }
}
