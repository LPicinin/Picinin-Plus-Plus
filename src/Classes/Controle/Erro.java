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
    public static final Erro variavelNaoDeclarada = new Erro(406, "Variavel não declarada");
    public static final Erro variavel_Ja_Declarada = new Erro(407, "Variavel já declarada");

    public Erro(int codigo, String mensagem)
    {
        super(codigo, mensagem);
    }

    public Erro(int codigo, String mensagem, Lexema lexema)
    {
        super(codigo, mensagem, lexema);
    }
    public static Erro getError(Erro e, Lexema lexema)
    {
        return new Erro(e.codigo, e.mensagem, lexema);
    }
    @Override
    public String toString()
    {
        return "Erro{" + codigo + " - " + mensagem + "}";
    }
}
