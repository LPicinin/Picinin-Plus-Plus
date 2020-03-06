/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Aluno
 */
public abstract class Constantes
{
    protected static int pos;
    protected static int posParagrafo;
    protected static int posLinha;
    protected static char[] code;
    protected static final List<Character> caracteresIgnorados = Arrays.asList('\n', ' ', '\t');
    protected static final List<Character> caracteresEspeciais = Arrays.asList('(', ')', ';', '{', '}', '\"');//caracteres que separam tokens e não sao os especiais
}
