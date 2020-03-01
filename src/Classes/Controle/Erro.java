/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Controle;

/**
 *
 * @author luish
 */
public class Erro extends Controle
{
    public static final Erro tokenNaoEncontrado = new Erro(404, "Não foi possível encontrar um token correspondente");
    public Erro(int codigo, String mensagem)
    {
        super(codigo, mensagem);
    }
    
}
