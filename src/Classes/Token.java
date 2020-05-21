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
    public static final Token tValor_Cientific = new Token("tCientific", "[+-]?\\d(\\.\\d+)?[Ee][+-]?\\d+");

    /**
     * null
     */
    public static final Token tValor_Null = new Token("tNull", "null|NULL");
    public static final Token tgoto = new Token("tGoto", "goto \\d*");
    public static final Token tgoto_mark = new Token("tMarcadorGoto", "markJMP\\d*");

    /**
     * valor generico - pode ser um Valor inteiro, decimal, booleano,
     * hexadecimal nulo, octadecimal, string, etc.. Valores VERDADEIROS
     */
    public static final List<Token> tValores = new ArrayList<>(
            Arrays.asList(tValor_Bool,
                    tValor_Cientific,
                    tValor_Char, 
                    tValor_Inteiro, 
                    tValor_Decimal, 
                    tValor_HexaDecimal, 
                    tValor_Null, 
                    tValor_OctaDecimal, 
                    tValor_String));
/*
    public static final List<Token> tValorGeneric = new ArrayList<Token>(
            Arrays.asList(tValor_Bool,
                    tValor_Char,
                    tValor_Inteiro,
                    tValor_Decimal,
                    tValor_HexaDecimal,
                    tValor_Null,
                    tValor_OctaDecimal,
                    tValor_String));
    */
    public static final Token tIdentificador = new Token("tId", "([A-Za-z])+(\\d)*([A-Za-z])*");

    /**
     * Tipo de variaveis suportadas pela linguagem: int, double, bool, string e
     * char
     */
    public static final Token tTipo = new Token("tTipo", "int|double|bool|string|char");
    public static final Token tINT = new Token("tINT", "int", tValor_Inteiro, tValor_Decimal, tValor_HexaDecimal, tValor_OctaDecimal);
    public static final Token tBool = new Token("tBOOL", "bool", tValor_Bool);
    public static final Token tString = new Token("tSTRING", "string", tValor_String);
    public static final Token tChar = new Token("tCHAR", "char", tValor_Char);
    public static final Token tDouble = new Token("tDOUBLE", "double", tValor_Decimal, tValor_Inteiro, tValor_Cientific, tValor_HexaDecimal, tValor_OctaDecimal);
    public static final List<Token> tTipos = new ArrayList<>(Arrays.asList(tINT, tDouble, tBool, tString, tChar));

    /**
     * and e or
     */
    public static final Token tOpLogOR = new Token("tOperOR", "or");
    public static final Token tOpLogAND = new Token("tOperAND", "or|and");
    public static final List<Token> tOpLogicos = new ArrayList<>(Arrays.asList(tOpLogAND, tOpLogOR));

    /**
     * Operadores relacionais: > , >= , < , <= , == e !=
     */
    //public static final Token tOpRelacional = new Token("tRelacional", ">|>=|<|<=|==|!=");
    public static final Token tOpDiferente = new Token("tOpDiferente", "!=");
    public static final Token tOpIgual = new Token("tOpIgual", "==");
    public static final Token tOpMenor = new Token("tOpMenor", "<");
    public static final Token tOpMaior = new Token("tOpMaior", ">");
    public static final Token tOpMenorE = new Token("tOpMenorE", "<=");
    public static final Token tOpMaiorE = new Token("tOpMaiorE", ">=");
    public static final List<Token> tOpRelacional = new ArrayList<>(
            Arrays.asList(tOpDiferente, tOpIgual, tOpMenor, tOpMaior, tOpMenorE, tOpMaiorE));
    /**
     * Operadores Aritmeticos: + , - , * e /
     */
    public static final Token tOper_soma = new Token("tOper+", "\\+");
    public static final Token tOper_menos = new Token("tOper-", "-");
    public static final Token tOper_multiplicacao = new Token("tOper*", "\\*");
    public static final Token tOper_divisao = new Token("tOper/", "/");
    public static final List<Token> tOperadores = new ArrayList<>(Arrays.asList(tOper_soma, tOper_menos, tOper_multiplicacao, tOper_divisao, tOpLogOR, tOpLogAND, tOpDiferente, tOpIgual, tOpMenor, tOpMaior, tOpMenorE, tOpMaiorE));

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

    //repeticao e condicao
    public static final Token tIf = new Token("tIf", "if");
    public static final Token tElse = new Token("tElse", "else");
    public static final Token tWhile = new Token("tWhile", "while");
    public static final Token tFor = new Token("tFor", "for");
    
    //tokens de controle do compilador
    public static final Token tOR_contole = new Token("|", "");
    public static final Token tVazio = new Token("vazio", "§");
    public static final Token tNaoReconhecido = new Token("tInexistente", "");
    
    public static final List<Token> tokens = new ArrayList<Token>(
            Arrays.asList(tInicio_Linguagem,
                    tValor_Bool,
                    tValor_Cientific,
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
                    tOpLogOR,
                    tOpLogAND,
                    tOpDiferente, 
                    tOpIgual, 
                    tOpMenorE, 
                    tOpMaiorE,
                    tOpMenor, 
                    tOpMaior, 
                    tOper_soma, 
                    tOper_menos, 
                    tOper_multiplicacao, 
                    tOper_divisao,
                    tINT,
                    tDouble,
                    tBool,
                    tString,
                    tChar,
                    tIf,
                    tElse,
                    tFor,
                    tWhile,
                    tPalavraReservada,
                    tIdentificador,
                    /*apartir daqui são tokens de controle*/
                    tComentario));

    private String idToken;
    private String regex;
    private Pattern pattern;
    private Token []gerados;

    public Token(String idToken, String regex)
    {
        this.idToken = idToken;
        this.regex = regex;
        pattern = Pattern.compile(this.regex);
    }
    
    public Token(String idToken, String regex, Token ...gerados)
    {
        this.idToken = idToken;
        this.regex = regex;
        this.gerados = gerados;
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

    public Token[] getGerados()
    {
        return gerados;
    }

    public void setGerados(Token[] gerados)
    {
        this.gerados = gerados;
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
