/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Controle;

import Classes.Token;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author luish
 */
public class TipoAnalise
{
    public final static int a_chaves = 0;
    public final static int a_parenteses = 1;
    public final static int ca_for = 2;
    public final static int ca_while = 3;
    public final static int ca_atribuicao = 4;
    public final static int a_inicioPrograma = 5;
    public final static int ca_declaracao = 6;
    public final static int ca_naoReconhecido = 7;
    public final static int ca_If = 8;
    public final static int ca_else = 9;
    
    private static final TipoAnalise a_for = new TipoAnalise(Token.tFor, ca_for);
    private static final TipoAnalise a_while = new TipoAnalise(Token.tWhile, ca_while);
    private static final TipoAnalise a_declaracao = new TipoAnalise(Token.tTipo, ca_declaracao);
    //public static final TipoAnalise a_atribuicao = new TipoAnalise(Token.tIdentificador, ca_atribuicao);
    private static final TipoAnalise a_naoReconhecido = new TipoAnalise(Token.tNaoReconhecido, ca_naoReconhecido);
    private static final TipoAnalise a_If = new TipoAnalise(Token.tIf, ca_If);
    private static final TipoAnalise a_else = new TipoAnalise(Token.tElse, ca_else);
   
    
    public final static List<TipoAnalise> listaAnalises = Arrays.asList(a_for, a_while, a_declaracao/*, a_atribuicao*/, a_naoReconhecido, a_If, a_else);
    private Token first;
    private int codigo;

    public TipoAnalise(Token first, int codigo)
    {
        this.first = first;
        this.codigo = codigo;
    }

    public Token getFirst()
    {
        return first;
    }

    public void setFirst(Token first)
    {
        this.first = first;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
    }
    
}
