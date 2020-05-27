/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Assembly;

import Classes.Controle.InstrucaoIntermediaria;
import Classes.Controle.Match;
import Classes.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author luish
 */
public class ConversorAssembly
{

    private static final HashMap variaveis = new HashMap();
    private static List<String> registradores;

    public final static String DECLARACAO = "toAssembly_declaracao";
    public final static String ATRIBUICAO = "toAssembly_atribuicao";
    public final static String FOR = "toAssembly_for";
    public final static String WHILE = "toAssembly_while";
    public final static String ELSE = "toAssembly_else";
    public final static String IF = "toAssembly_if";

    public ConversorAssembly()
    {
        for (int i = 0; i < 15; i++)
        {
            registradores.add("R" + i);
        }
    }

    public static List<String> toAssembly_atribuicao(InstrucaoIntermediaria intr)
    {
        String a = "", b = "", c = "", d = "";
        String aux = "";
        List<String> result = new ArrayList<>();

        List<Match> auxlist = intr.getCadeia_elementos();

        a = getRegistrador(auxlist.get(0));

        //A = B
        if (auxlist.size() == 3)
        {
            b = getRegistrador(auxlist.get(1));
            //A = 8/ou outro valor direto
            if (auxlist.get(2).getToken().equals(Token.tIdentificador))
            {
                aux = "load " + a + ", " + b;
            } else//recebe variavel
            {
                aux = "MOVE " + a + ", " + b;
            }
        } else//A = B OP C
        {
            b = getRegistrador(auxlist.get(2));
            c = getRegistrador(auxlist.get(3));
            d = getRegistrador(auxlist.get(4));

            aux = "ADDI " + a + ", " + b + ", " + d;
        }

        return result;
    }

    public static List<String> toAssembly_declaracao(InstrucaoIntermediaria intr)
    {
        List<String> result = new ArrayList<>();

        return result;
    }

    public static List<String> toAssembly_if(InstrucaoIntermediaria intr)
    {
        List<String> result = new ArrayList<>();

        return result;
    }

    public static List<String> toAssembly_while(InstrucaoIntermediaria intr)
    {
        List<String> result = new ArrayList<>();

        return result;
    }

    public static List<String> toAssembly_for(InstrucaoIntermediaria intr)
    {
        List<String> result = new ArrayList<>();

        return result;
    }

    public static List<String> toAssembly_else(InstrucaoIntermediaria intr)
    {
        List<String> result = new ArrayList<>();

        return result;
    }

    private static String getRegistrador(Match m)
    {
        if (m.getToken().equals(Token.tIdentificador))
        {
            String key = m.getLexema().getPalavra();
            if (variaveis.containsKey(key))
            {
                return (String) variaveis.get(key);
            } else
            {
                String aux = registradores.get(0);
                variaveis.put(key, aux);
                registradores.remove(0);
                return aux;
            }
        }
        else
        {
            return m.getLexema().getPalavra();
        }
    }
}
