/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.Controle.Erro;
import Classes.Controle.Match;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Aluno
 */
public abstract class Constantes
{
    //protected static int pos;
    protected static int posParagrafo;
    protected static int posLinha;
    protected static char[] code;
    protected static final List<Character> caracteresIgnorados = Arrays.asList('\n', ' ', '\t');
    protected static final List<Character> caracteresEspeciais = Arrays.asList('(', ')', ';', '{', '}', '\"', '+', '-', '*', '/', '=');//caracteres que separam tokens e não sao os especiais
    protected static final List<Token> token_De_Conexao = Arrays.asList(Token.tPontoVirgula, Token.tChave_abre, Token.tChave_fecha);
    protected static List<Match> lexemas_tokens_correspondidos = new ArrayList<>();
    protected static List<Erro> erros = new ArrayList<>();
}
