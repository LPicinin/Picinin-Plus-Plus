/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 *
 * @author luish
 */
public class Token
{
    public static final Token tInicio_Linguagem = new Token("tIni", "main");
    /**
     * números Decimais, exemplo 666
     */
    public static final Token tValor_Inteiro = new Token("tInteiro", "(-|\\+)*\\d+");
    public static final Token tValor_Decimal = new Token("tDecimal", "(-|\\+)*\\d+\\.\\d+");
    
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
    public static final Token tValor_Null = new Token("tNull", "null|NULL");

    /**
     * valor generico - pode ser um Valor inteiro, decimal, booleano,
     * hexadecimal nulo, octadecimal, string, etc.. Valores VERDADEIROS
     */
    public static final List<Token> tValores = new ArrayList<>(
            Arrays.asList(tValor_Bool, 
                    tValor_Char, 
                    tValor_Inteiro, 
                    tValor_Decimal, 
                    tValor_HexaDecimal, 
                    tValor_Null, 
                    tValor_OctaDecimal, 
                    tValor_String));
    /*new Token("tValor",
            "(" + tValor_Bool.regex + ")|("
            + tValor_Char.regex + ")|("
            + tValor_Inteiro.regex + ")|("
            + tValor_Decimal.regex + ")|("
            + tValor_HexaDecimal.regex + ")|("
            + tValor_Null.regex + ")|("
            + tValor_OctaDecimal.regex + ")|("
            + tValor_String.regex + ")");
     */

    public static final Token tIdentificador = new Token("tId", "([A-Za-z])+(\\d)*([A-Za-z])*");

    /**
     * Tipo de variaveis suportadas pela linguagem: int, double, bool, string e
     * char
     */
    public static final Token tTipo = new Token("tTipo", "int|double|bool|string|char");
    public static final Token tINT = new Token("tINT", "int");
    public static final Token tDouble = new Token("tDOUBLE", "double");
    public static final Token tBool = new Token("tBOOL", "bool");
    public static final Token tString = new Token("tSTRING", "string");
    public static final Token tChar = new Token("tCHAR", "char");
    public static final List<Token> tTipos = new ArrayList<>(Arrays.asList(tINT, tDouble, tBool, tString, tChar));

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
    public static final Token tOperadores = new Token("tOperadores", "\\+|-|\\*|/");

    /**
     * Todas as palavras reservadas da linguagem: bool, char, double, else,
     * false, for, if, int, null, program, string, true, while, and e or
     */
    public static final Token tPalavraReservada = new Token("tPalReserv", "bool|char|double|else|false|for|if|int|null|program|string|true|while|and|or|main");
    public static final Token tComentario = new Token("tComentario", "//[^\n]*");

    //caracteres especiais
    public static final Token tParenteses_abre = new Token("tAbreParentese", "\\(");
    public static final Token tParenteses_fecha = new Token("tFechaParentese", "\\)");
    public static final Token tChave_abre = new Token("tAbreChave", "\\{");
    public static final Token tChave_fecha = new Token("tFechaChave", "\\}");
    public static final Token tIgual = new Token("tIgual", "=");
    public static final Token tPontoVirgula = new Token("tPontoVirgula", ";");

    /* SAVE DE SEGURANÇA
        public static final List<Token> tokens = new ArrayList<Token>(
            Arrays.asList(tValor_Generic,
                    tOpLogico, 
                    tOpRelacional, 
                    tOperadores, 
                    tPalavraReservada,
                    tIdentificador, 
                    tComentario));
     */
    public static final List<Token> tokens = new ArrayList<Token>(
            Arrays.asList(tInicio_Linguagem,
                    tValor_Bool,
                    tValor_Char,
                    tValor_Inteiro,
                    tValor_Decimal,
                    tValor_HexaDecimal,
                    tValor_Null,
                    tValor_OctaDecimal,
                    tValor_String,
                    tParenteses_abre,
                    tParenteses_fecha,
                    tChave_abre,
                    tChave_fecha,
                    tIgual,
                    tPontoVirgula,
                    tOpLogico,
                    tOpRelacional,
                    tOperadores,
                    tINT,
                    tDouble,
                    tBool,
                    tString,
                    tChar,
                    tPalavraReservada,
                    tIdentificador,
                    tComentario));

    private String idToken;
    private String regex;
    private Pattern pattern;

    public Token(String idToken, String regex)
    {
        this.idToken = idToken;
        this.regex = regex;
        pattern = Pattern.compile(this.regex);
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

    public Pattern getPattern()
    {
        return pattern;
    }

    public void setPattern(Pattern pattern)
    {
        this.pattern = pattern;
    }

    @Override
    public String toString()
    {
        return idToken;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Token other = (Token) obj;
        if (!Objects.equals(this.idToken, other.idToken))
            return false;
        return true;
    }

}
