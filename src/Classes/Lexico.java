/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.Controle.Erro;
import Classes.Controle.Match;

/**
 *
 * @author luish
 */
public class Lexico implements Analisador
{

    @Override
    public Object analise(Lexema lex)
    {
        String palavra = lex.getLexema();
        //System.out.println(palavra);
        boolean naoAchou = true;
        int i;
        for (i = 0; i < Token.tokens.size() && naoAchou; i++)
        {
            naoAchou = !palavra.matches(Token.tokens.get(i).getRegex());
        }
        if (!naoAchou)
        {
            return new Match(lex, Token.tokens.get(i - 1));
        } else
        {
            Erro e = new Erro(404, "Token não encontrado", lex);
            return e;
        }
    }

}
