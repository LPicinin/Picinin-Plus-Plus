/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Controle;

import Classes.Lexema;
import Classes.Token;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
    public final static String escIni = "toinicioEscopo";
    public final static String escFim = "tofimEscopo";
    
    public static int goto_aux;

    public static List<InstrucaoIntermediaria> toinicioEscopo(Instrucao intr)
    {
        List<InstrucaoIntermediaria> result = new ArrayList<>();
        result.add(new InstrucaoIntermediaria(intr.getCadeia_elementos(), escIni, intr.getEscopo()));
        
        return result;
    }
    
    public static List<InstrucaoIntermediaria> tofimEscopo(Instrucao intr)
    {
        List<InstrucaoIntermediaria> result = new ArrayList<>();
        result.add(new InstrucaoIntermediaria(intr.getCadeia_elementos(), escFim, intr.getEscopo()));
        
        return result;
    }
    
    public static List<InstrucaoIntermediaria> toCI_declaracao(Instrucao intr)
    {
        List<Match> listPrincipal = intr.getCadeia_elementos();
        List<InstrucaoIntermediaria> result = new ArrayList<>();
        List<Match> lInstr = new ArrayList<>();
        retiraPontoVirgula(listPrincipal);

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
        Match variavel = intr.getCadeia_elementos().get(0);
        List<Match> listPrincipal = new ArrayList<>(intr.getCadeia_elementos());
        if (listPrincipal.get(0).getToken().equals(Token.tIdentificador)
                && listPrincipal.get(1).getToken().equals(Token.tIgual))
        {
            listPrincipal.remove(0);
            listPrincipal.remove(0);
        }
        retiraPontoVirgula(listPrincipal);
        List<InstrucaoIntermediaria> result = new ArrayList<>();
        HashMap map = new HashMap();
        HashMap map_oper = new HashMap();
        map.put('&', "and");
        map.put('|', "or");
        map.put('>', ">");
        map.put('<', "<");
        map.put('•', ">=");
        map.put('○', "<=");
        map.put('♦', "!=");
        map.put('♥', "==");
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext())
        {
            char next = (char) iterator.next();
            map_oper.put(map.get(next), next);
        }

        List<Match> auxlist = new ArrayList<>();

        if (listPrincipal.size() <= 3)//atribuicao simples
        {
            listPrincipal.add(0, new Match(new Lexema("=", 0, 0), Token.tIgual));
            listPrincipal.add(0, variavel);

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
                            //aux = (char) map.get(key);
                            aux = key;
                        }
                        maux.getLexema().setPalavra(aux + "");
                    } else if (Token.tOpRelacional.contains(in.getToken()) || Token.tOpLogicos.contains(in.getToken()))//operador lógico
                    {
                        maux.getLexema().setPalavra(map_oper.get(in.getLexema().getPalavra()).toString());
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
            String exp = exp_aux.toString().replaceAll("\\)|\\(", "");

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
            result.get(result.size() - 1).getCadeia_elementos().set(0, variavel);
        }

        return result;
    }

    public static List<InstrucaoIntermediaria> toCI_for(Instrucao intr)
    {
        List<InstrucaoIntermediaria> result = new ArrayList<>();
        List<Match> p1 = new ArrayList<>();
        List<Match> p2 = new ArrayList<>();
        List<Match> p3 = new ArrayList<>();
        List<Match> laux = new ArrayList<>();
        
        List<Match> lprincipal = new ArrayList<>(intr.getCadeia_elementos());

        lprincipal.remove(0);//retira for
        lprincipal.remove(0);//retira (

        int i = 0;

        for (; i < lprincipal.size()
                && !lprincipal.get(i).getToken().equals(Token.tPontoVirgula); i++)
        {
            p1.add(lprincipal.get(i));
        }
        i++;
        for (; i < lprincipal.size()
                && !lprincipal.get(i).getToken().equals(Token.tPontoVirgula); i++)
        {
            p2.add(lprincipal.get(i));
        }
        i++;
        for (; i < lprincipal.size(); i++)
        {
            p3.add(lprincipal.get(i));
        }

        Instrucao int1 = null;
        Instrucao int2 = new Instrucao(atribuicao);
        Instrucao int3 = new Instrucao(atribuicao);

        //declaração / atribuicao
        if (Token.tTipos.contains(p1.get(0).getToken()))
        {
            int1 = new Instrucao(declaracao);
        } else if (p1.get(0).getToken().equals(Token.tIdentificador))
        {
            int1 = new Instrucao(atribuicao);
        }
        if (int1 != null)
        {
            int1.setCadeia_elementos(p1);
            result.addAll(int1.toCodigoIntermediario());
        }
        
        //condição
        p2.add(0, new Match(new Lexema("=", 0, 0), Token.tIgual));
        p2.add(0, new Match(new Lexema("vFor", 0, 0), Token.tIdentificador));
        
        
        int2.setCadeia_elementos(p2);
        
        
        result.add(new InstrucaoIntermediaria(Arrays.asList(new Match(new Lexema("markJMP" + goto_aux, 0, 0), Token.tgoto_mark)), "", 0));
        
        result.addAll(int2.toCodigoIntermediario());
        
        
        laux.add(new Match(new Lexema("loop", 0, 0), Token.tFor));
        laux.add(new Match(new Lexema("vFor", 0, 0), Token.tIdentificador));
        laux.add(new Match(new Lexema("markJMPA" + goto_aux, 0, 0), Token.tgoto));
        InstrucaoIntermediaria ii = new InstrucaoIntermediaria(laux, For, intr.getEscopo());
        result.add(ii);
        
        result.add(new InstrucaoIntermediaria(Arrays.asList(new Match(new Lexema("goto markJMPEND" + goto_aux, 0, 0), Token.tgoto)), "", 0));
        result.add(new InstrucaoIntermediaria(Arrays.asList(new Match(new Lexema("markJMPA" + goto_aux, 0, 0), Token.tgoto_mark)), "", 0));
        //escopo/trecho dentro do for
        
        
        //atribuição
        int3.setCadeia_elementos(p3);
        if(!p3.isEmpty())
        {
            result.add(null);
            result.addAll(int3.toCodigoIntermediario());
        }
        return result;
    }

    public static List<InstrucaoIntermediaria> toCI_while(Instrucao intr)
    {
        List<InstrucaoIntermediaria> result = new ArrayList<>();
        List<Match> lprincipal = new ArrayList<>(intr.getCadeia_elementos());
        List<Match> laux = new ArrayList<>();
        lprincipal.remove(0);//tira o while
        if (lprincipal.get(0).getToken().equals(Token.tParenteses_abre))
            lprincipal.remove(0);//tira o (

        lprincipal.add(0, new Match(new Lexema("=", 0, 0), Token.tIgual));
        lprincipal.add(0, new Match(new Lexema("vWhile", 0, 0), Token.tIdentificador));
        Instrucao int_aux = new Instrucao(atribuicao);
        int_aux.setCadeia_elementos(lprincipal);

        result.addAll(toCI_atribuicao(int_aux));
        laux.add(new Match(new Lexema("loop", 0, 0), Token.tWhile));
        laux.add(new Match(new Lexema("vWhile", 0, 0), Token.tIdentificador));
        laux.add(new Match(new Lexema("markJMPA" + goto_aux, 0, 0), Token.tgoto));

        InstrucaoIntermediaria ii = new InstrucaoIntermediaria(laux, While, intr.getEscopo());

        result.add(ii);
        return result;
    }

    public static List<InstrucaoIntermediaria> toCI_if(Instrucao intr)
    {

        List<InstrucaoIntermediaria> result = new ArrayList<>();
        List<Match> lprincipal = new ArrayList<>(intr.getCadeia_elementos());
        List<Match> laux = new ArrayList<>();
        lprincipal.remove(0);//tira o if
        if (lprincipal.get(0).getToken().equals(Token.tParenteses_abre))
            lprincipal.remove(0);//tira o (

        lprincipal.add(0, new Match(new Lexema("=", 0, 0), Token.tIgual));
        lprincipal.add(0, new Match(new Lexema("vif", 0, 0), Token.tIdentificador));
        Instrucao int_aux = new Instrucao(atribuicao);
        int_aux.setCadeia_elementos(lprincipal);

        result.addAll(toCI_atribuicao(int_aux));
        laux.add(new Match(new Lexema("if", 0, 0), Token.tIf));
        laux.add(new Match(new Lexema("vif", 0, 0), Token.tIdentificador));
        laux.add(new Match(new Lexema("goto" + goto_aux, 0, 0), Token.tgoto));

        InstrucaoIntermediaria ii = new InstrucaoIntermediaria(laux, If, intr.getEscopo());

        result.add(ii);
        return result;

    }

    public static List<InstrucaoIntermediaria> toCI_else(Instrucao intr)
    {
        List<InstrucaoIntermediaria> result = new ArrayList<>();
        result.add(new InstrucaoIntermediaria(intr.getCadeia_elementos(), Else, intr.getEscopo()));
        
        return result;
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
        List<Token> remover = Arrays.asList(Token.tPontoVirgula, Token.tParenteses_abre, Token.tParenteses_fecha);

        listPrincipal.forEach(in ->
        {
            if (remover.contains(in.getToken()))
                mremocao.add(in);
        });
        listPrincipal.removeAll(mremocao);
    }

}
