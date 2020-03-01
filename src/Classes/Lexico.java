/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.Controle.Erro;

/**
 *
 * @author luish
 */
public class Lexico implements Analisador
{

    @Override
    public Object analise(String palavra)
    {
        Token r;
        boolean naoAchou = true;
        int i;
        for (i = 0; i < Token.tokens.size() && naoAchou; i++)
        {
            naoAchou = palavra.matches(Token.tokens.get(i).getRegex());
        }
        if(!naoAchou)
        {
            r = Token.tokens.get(i);
        }
        else
            return Erro.tokenNaoEncontrado;
        return r;
    }
    
}
