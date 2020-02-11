/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author luish
 */
public class Util
{

    /**
     * Expressão regular que descreve nomes de variaveis
     */
    public static final String regex_identificador = "([A-Za-z])+(\\d)*([A-Za-z])*";
    
    /**
     * Expressão regular que descreve cadeias de caracteres entre aspas
     */
    public static final String regex_string = "\\\"(.*?)\\\"";
    
    /**
     * Expressão regular que descreve números inteiros
     */
    public static final String regex_inteiros = "\\d+";
    public static final String regex_inteiros_com_Sinal = "(-|\\+)*\\d+";
    
    /**
     * Expressão regular que descreve números flutuantes
     */
    public static final String regex_float = "(\\d)+(\\.){1}(\\d)+";
    
    
}
