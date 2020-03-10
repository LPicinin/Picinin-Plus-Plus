/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.RecursosTabelaSintatica;

import Classes.Token;
import static Classes.Token.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author luish
 */
public class Regra
{
    public static final Regra regBloco = new Regra("regBloco", Arrays.asList(), tChave_abre);
    /*
    public static final Regra regBloco_codigos = new Regra("regBloco-codigos", 
            Arrays.asList(tValor_Inteiro, tValor_Decimal, tValor_Bool, 
                    tValor_String, tChar, tIdentificador, tPontoVirgula, tIf, 
                    tWhile, tFor), null);
    
    public static final Regra regBloco_codigo = new Regra("regIf", 
            Arrays.asList(tValor_Inteiro, tValor_Decimal, tValor_Bool, 
                    tValor_String, tChar, tIdentificador, tPontoVirgula, tIf, 
                    tWhile, tFor), null);
    */
    
    /*
    public static final Regra regVariavel_codigo = new Regra("regIf", Arrays.asList(new Token[]{tIf}), tTipo, tIdentificador);
    
    public static final Regra regIF = new Regra("regIf", Arrays.asList(tIf), tIf);
    
    public static final Regra reg_declaracao_var_semAtribuicao = new Regra("Reg_Declaracao_variavel_sem_atrib", 
            Arrays.asList(new Token[]{tTipo, tIdentificador, tPontoVirgula}
            ), tTipo);
    
    public static final Regra reg_declaracao_var_comAtribuicao = new Regra("Reg_Declaracao_variavel", 
            new ArrayList<>(Arrays.asList(tTipo, tIdentificador, tIgual, tValorGeneric, tPontoVirgula)), tTipo);
    
    */
    
    
    private String regraId;
    private HashMap gera;
    private Token []first;
    private List<Token> follow;

    
    public Regra(String regraId, List<List<Token>> lista_possiveis_geracoes, Token ...first)
    {
        this.regraId = regraId;
        this.first = first;
        this.follow = follow;
        gera = new HashMap();
        ArrayList<Token> list_aux = new ArrayList<>();
        
        for (int i = 0; i < first.length; i++)
        {
            gera.put(lista_possiveis_geracoes.get(i), first[i]);
        }
    }

    public String getRegraId()
    {
        return regraId;
    }

    public void setRegraId(String regraId)
    {
        this.regraId = regraId;
    }

    public HashMap getGera()
    {
        return gera;
    }

    public void setGera(HashMap gera)
    {
        this.gera = gera;
    }

    public Token[] getFirst()
    {
        return first;
    }

    public void setFirst(Token[] first)
    {
        this.first = first;
    }
    
}
