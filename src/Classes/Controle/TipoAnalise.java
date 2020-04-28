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

    public final static int A_CHAVES = 0;
    public final static int A_PARENTESE = 1;
    public final static int CA_FOR = 2;
    public final static int CA_WHILE = 3;
    public final static int CA_ATRIBUICAO = 4;
    public final static int A_INICIO_PROGRAMA = 5;
    public final static int CA_DECLARACAO = 6;
    public final static int CA_NAO_RECONHECIDO = 7;
    public final static int CA_IF = 8;
    public final static int CA_ELSE = 9;
    public final static int CA_CHAVE_ABRE = 10;
    public final static int CA_CHAVE_FECHA = 11;
    public final static int CA_CODIGO_FORA_DO_ESCOPO = 12;

    private static final TipoAnalise a_for = new TipoAnalise(Token.tFor, CA_FOR);
    private static final TipoAnalise a_while = new TipoAnalise(Token.tWhile, CA_WHILE);
    private static final TipoAnalise a_declaracao = new TipoAnalise(Token.tTipo, CA_DECLARACAO);
    private static final TipoAnalise a_atribuicao = new TipoAnalise(Token.tIdentificador, CA_ATRIBUICAO);
    private static final TipoAnalise a_naoReconhecido = new TipoAnalise(Token.tNaoReconhecido, CA_NAO_RECONHECIDO);
    private static final TipoAnalise a_If = new TipoAnalise(Token.tIf, CA_IF);
    private static final TipoAnalise a_else = new TipoAnalise(Token.tElse, CA_ELSE);
    private static final TipoAnalise a_chaveAbre = new TipoAnalise(Token.tChave_abre, CA_CHAVE_ABRE);
    private static final TipoAnalise a_chaveFecha = new TipoAnalise(Token.tChave_fecha, CA_CHAVE_FECHA);
    private static final TipoAnalise a_codigoForaDoEscopo = new TipoAnalise(null, CA_CODIGO_FORA_DO_ESCOPO);

    public final static List<TipoAnalise> listaAnalises
            = Arrays.asList(a_for,
                    a_while,
                    a_declaracao, 
                    a_atribuicao,
                    a_naoReconhecido,
                    a_If,
                    a_else,
                    a_chaveAbre,
                    a_chaveFecha);

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
