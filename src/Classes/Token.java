/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author luish
 */
public class Token
{

    /**
     * números Decimais, exemplo 666
     */
    public static final Token tValor_Decimal = new Token("tNum", "(-|\\+)*\\d+");
    /**
     * números Hexadecimais, exemplo xA45F6 ou XA45F6
     */
    public static final Token tValor_HexaDecimal = new Token("tNumHexa", "(x|X)[0-9a-fA-F]+");
    /**
     * números Octadecimais, exemplo o4526 ou O4526
     */
    public static final Token tValor_OctaDecimal = new Token("tNumOcta", "(o|O)[0-7]+");
    /**
     * sequencia de caracteres dentreo de ", exemplo "isso é uma string"
     */
    public static final Token tValor_String = new Token("tString", "\\\"(.*?)\\\"");
    /**
     * qualquer caracter dentro de ', exemplo 'a'
     */
    public static final Token tValor_Char = new Token("tChar", "\'(.?)\'");
    /**
     * true, false
     */
    public static final Token tValor_Bool = new Token("tBool", "true|false");

    /**
     * null
     */
    public static final Token tValor_Null = new Token("tNull", "null");
    
    /**
     * valor generico - pode ser um Valor inteiro, decimal, booleano, hexadecimal
     * nulo, octadecimal, string, etc..
     * Valores VERDADEIROS
     */
    public static final Token tValor_Generic = new Token("tValor", 
            "("+tValor_Bool.regex+")|("
                    +tValor_Char.regex+")|("
                    +tValor_Decimal.regex+")|("
                    +tValor_HexaDecimal.regex+")|("
                    +tValor_Null.regex+")|("
                    +tValor_OctaDecimal.regex+")|("
                    +tValor_String.regex+")");
    
    
    public static final Token tIdentificador = new Token("tId", "([A-Za-z])+(\\d)*([A-Za-z])*");
    
    /**
     * and e or
     */
    public static final Token tOpLogico = new Token("tOperadorLogico", "or|and");
    
    /**
     * Operadores relacionais: > , >= , < , <= , == e !=
     */
    public static final Token tOpRelacional = new Token("tRelacional", ">|>=|<|<=|==|!=");

    /**
     * Operadores Aritmeticos: + , - , * e /
     */
    public static final Token tOperadores = new Token("tOperadores", "+|-|*|/");
    
    /**
     * Todas as palavras reservadas da linguagem: bool, char, double, else, 
     * false, for, if, int, null, program, string, true, while, and e or
     */
    public static final Token tPalavraReservada = new Token("tPalReserv", "bool|char|double|else|false|for|if|int|null|program|string|true|while|and|or");
    public static final Token tComentario = new Token("tComentario", "//[^\n]*");
    
    public static final List<Token> tokens = new ArrayList<Token>(
            Arrays.asList(tValor_Generic, 
                    tIdentificador, 
                    tOpLogico, 
                    tOpRelacional, 
                    tOperadores, 
                    tPalavraReservada, 
                    tComentario));
    
    private String idToken;
    private String regex;

    public Token(String idToken, String regex)
    {
        this.idToken = idToken;
        this.regex = regex;
    }

    public String getIdToken()
    {
        return idToken;
    }

    public void setIdToken(String idToken)
    {
        this.idToken = idToken;
    }

    public String getRegex()
    {
        return regex;
    }

    public void setRegex(String regex)
    {
        this.regex = regex;
    }

    @Override
    public String toString()
    {
        return idToken;
    }

}
