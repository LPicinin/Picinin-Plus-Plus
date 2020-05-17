/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Controle;

import Classes.Lexema;
import Classes.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luish
 */
public class Conversor
{
    
    public final static String declaracao = "toCI_declaracao";
    public final static String atribuicao = "toCI_atribuicao";
    public final static String For = "toCI_for";
    public final static String While = "toCI_while";
    public final static String Else = "toCI_else";
    public final static String If = "toCI_if";
    
    public static List<InstrucaoIntermediaria> toCI_declaracao(Instrucao intr)
    {
        List<Match> listPrincipal = intr.getCadeia_elementos();
        List<InstrucaoIntermediaria> result = new ArrayList<>();
        List<Match> lInstr = new ArrayList<>();
        lInstr.add(listPrincipal.get(0));//tipo
        lInstr.add(listPrincipal.get(1));//variavel
        
        result.add(new InstrucaoIntermediaria(lInstr, declaracao, intr.getEscopo()));
        
        if (listPrincipal.size() > 2)//possui atribuicao
        {
            List<Match> tmpLCE = new ArrayList<>(intr.getCadeia_elementos());
            tmpLCE.remove(0);//remove tipo pra ficar igual a atribuicao
            Instrucao in = new Instrucao(atribuicao);
            in.setCadeia_elementos(tmpLCE);
            result.addAll(toCI_atribuicao(in));
        }
        return result;
    }
    
    public static List<InstrucaoIntermediaria> toCI_atribuicao(Instrucao intr)
    {
        
        Token tipoOriginal = intr.getCadeia_elementos().get(0).getToken();
        List<Match> listPrincipal = intr.getCadeia_elementos();
        retiraPontoVirgula(listPrincipal);
        List<InstrucaoIntermediaria> result = new ArrayList<>();
        HashMap map = new HashMap();
        
        List<Match> auxlist = new ArrayList<>();
        
        if (listPrincipal.size() == 3)//atribuicao simples
        {
            result.add(new InstrucaoIntermediaria(new ArrayList<>(listPrincipal), atribuicao, intr.getEscopo()));
        } else
        {
            //mapeamento chave valor
            listPrincipal.forEach(in ->
            {
                try
                {
                    Match maux = (Match) in.clone();
                    if (in.getToken().equals(Token.tIdentificador)
                            || Token.tValores.contains(in.getToken()))
                    {
                        char aux;
                        if (!map.containsValue(in.getLexema().getPalavra()))
                        {
                            aux = (char) (65 + map.size());
                            map.put(aux, in.getLexema().getPalavra());
                        } else
                        {
                            char key = 0;
                            for (Object itemKey : map.keySet())
                            {
                                if (map.get(itemKey).equals(in.getLexema().getPalavra()))
                                {
                                    key = (char) itemKey;
                                    break;
                                }
                            }
                            aux = (char) map.get(key);
                        }
                        maux.getLexema().setPalavra(aux + "");
                    }
                    auxlist.add(maux);
                    
                } catch (CloneNotSupportedException ex)
                {
                    System.out.println(ex.getMessage());
                }
            });

            //resolveExpressao
            StringBuilder exp_aux = new StringBuilder("");
            auxlist.forEach(in ->
            {
                exp_aux.append(in.getLexema().getPalavra());
            });
            String exp = exp_aux.toString();
            
            ArvoreTresEnderecos arv = new ArvoreTresEnderecos();
            String aux;
            List<String> codigo_3_Enderecos = arv.build(exp);
            List<Match> ls_match_aux;
            
            for (String codigoIns : codigo_3_Enderecos)
            {
                ls_match_aux = new ArrayList<>();
                for (String key : codigoIns.split(" "))
                {
                    Match m;
                    if (key.length() == 1)
                    {
                        if (map.get(key.charAt(0)) != null)
                            aux = (String) map.get(key.charAt(0));
                        else
                            aux = key;
                        
                        m = getMatchPalavra(intr, aux);
                    } else//variavel temporaria criada pelo codigo de 3
                    {
                        m = new Match(new Lexema(key, 0, 0), tipoOriginal);
                    }
                    ls_match_aux.add(m);
                }
                result.add(new InstrucaoIntermediaria(ls_match_aux, atribuicao, intr.getEscopo()));
            }
            //faz a ultimalinha ser a variavel do codigo recebendo a ultima operacao
            result.get(result.size() - 1).getCadeia_elementos().set(0, listPrincipal.get(0));
        }
        
        return result;
    }
    
    public static List<InstrucaoIntermediaria> toCI_for(Instrucao intr)
    {
        return null;
    }
    
    public static List<InstrucaoIntermediaria> toCI_while(Instrucao intr)
    {
        return null;
    }
    
    public static List<InstrucaoIntermediaria> toCI_if(Instrucao intr)
    {
        return null;
    }
    
    public static List<InstrucaoIntermediaria> toCI_else(Instrucao intr)
    {
        return null;
    }
    
    private static Match getMatchPalavra(Instrucao intr, String palavra)
    {
        List<Match> ce = intr.getCadeia_elementos();
        int i;
        for (i = 0; i < ce.size() && !ce.get(i).getLexema().getPalavra().equals(palavra); i++)
        {
            
        }
        return i < ce.size() ? ce.get(i) : null;
    }
    
    private static void retiraPontoVirgula(List<Match> listPrincipal)
    {
        List<Match> mremocao = new ArrayList<>();
        
        listPrincipal.forEach(in ->
        {
            if(in.getToken().equals(Token.tPontoVirgula))
                mremocao.add(in);
        });
        listPrincipal.removeAll(mremocao);
    }
    
}
