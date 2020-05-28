/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.Controle.Aviso;
import Classes.Controle.Controle;
import Classes.Controle.Conversor;
import Classes.Controle.Erro;
import Classes.Controle.Instrucao;
import Classes.Controle.InstrucaoIntermediaria;
import Classes.Controle.Match;
import Classes.Controle.Simbolo;
import Controladora.CtrCompilador;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import util.Util;

/**
 *
 * @author luish
 */
public class Semantico extends Constantes
{

    private List<Instrucao> instrucoes;
    private List<Controle> erros_avisos_semanticos;

    //para controle
    private List<Instrucao> declaracoes;
    private List<Instrucao> atribuicoes;
    private List<Instrucao> lacos;
    private List<Instrucao> condicoes;
    private List<Simbolo> listSimbolos;
    public static List<InstrucaoIntermediaria> lci;

    public Semantico()
    {
        instrucoes = new ArrayList<>();
        if (lci != null)
            lci.clear();
    }

    public static List<InstrucaoIntermediaria> getLci()
    {
        return lci;
    }

    public static void setLci(List<InstrucaoIntermediaria> lci)
    {
        Semantico.lci = lci;
    }

    public List<Instrucao> getInstrucoes()
    {
        return instrucoes;
    }

    public void setInstrucoes(List<Instrucao> instrucoes)
    {
        this.instrucoes = instrucoes;
    }

    public boolean addInstrucao(Instrucao ins)
    {
        return instrucoes.add(ins);
    }

    public List<Controle> getErros_avisos_semanticos()
    {
        return erros_avisos_semanticos;
    }

    public void setErros_avisos_semanticos(List<Controle> erros_avisos_semanticos)
    {
        this.erros_avisos_semanticos = erros_avisos_semanticos;
    }

    public List<Controle> extrair()
    {
        erros_avisos_semanticos = new ArrayList<>();

        fragmentar_Instrucoes();
        buscaErros_Avisos();
        if (countErros(erros_avisos_semanticos) == 0)
            conversaoCI();
        return erros_avisos_semanticos;
    }

    private void fragmentar_Instrucoes()
    {
        declaracoes = new ArrayList<>();
        atribuicoes = new ArrayList<>();
        lacos = new ArrayList<>();
        condicoes = new ArrayList<>();

        instrucoes.forEach((in) ->
        {
            switch (in.getNome_conversor())
            {
                case Conversor.atribuicao:
                    atribuicoes.add(in);
                    break;
                case Conversor.declaracao:
                    if (contain(Token.tIgual, in) == -1)
                        declaracoes.add(in);
                    else//quebra declaração com atribuição :)
                    {
                        List<Instrucao> da = quebraDeclaracao(in);//declaracao[0] e atribuicao[1]
                        declaracoes.add(da.get(0));
                        atribuicoes.add(da.get(1));
                    }
                    break;
                case Conversor.If:
                    condicoes.add(in);
                    break;
                case Conversor.Else:
                    condicoes.add(in);
                    break;
                case Conversor.For:
                    declaracoes.add(getdeclar(in));
                    lacos.add(in);
                    break;
                case Conversor.While:
                    lacos.add(in);
                    break;
            }
        });

    }

    private int contain(Token token, Instrucao in)
    {
        int i, size = in.getCadeia_elementos().size();
        boolean flag = true;
        for (i = 0; i < size && flag; i++)
        {
            flag = !in.getCadeia_elementos().get(i).getToken().equals(token);
        }
        return i == size ? -1 : i;
    }

    private int contain(Lexema lex, Instrucao in)
    {
        int i, size = in.getCadeia_elementos().size();
        boolean flag = true;
        for (i = 0; i < size && flag; i++)
        {
            flag = !in.getCadeia_elementos().get(i).getLexema().getPalavra().equals(lex.getPalavra());
        }
        return i == size ? -1 : i;
    }

    private List<Instrucao> quebraDeclaracao(Instrucao in)
    {
        Instrucao dec = new Instrucao(in.getNome_conversor());
        Instrucao atrib = new Instrucao(in.getNome_conversor());

        int i = 0;
        List<Match> list = in.getCadeia_elementos();

        for (i = 0; i < list.size() && !list.get(i).getToken().equals(Token.tIgual); i++)
        {
            dec.addCadeia_elementos(list.get(i));
        }
        i--;
        for (; i < list.size(); i++)
        {
            atrib.addCadeia_elementos(list.get(i));
        }

        ArrayList<Instrucao> aux_ret = new ArrayList<>();
        aux_ret.add(dec);
        aux_ret.add(atrib);

        return aux_ret;
    }

    private void buscaErros_Avisos()
    {
        listSimbolos = CtrCompilador.instancia().getCompilador().getTabela_Simbolos();
        possiveisOtimizacoes();
        tipagem();
        condicoes();
    }

    private void tipagem()
    {
        atribuicoes.forEach(in ->
        {
            Token tipo = getTipo(in.getCadeia_elementos().get(0));
            Token[] vet = tipo.getGerados();
            Token tipo_aux;
            List<Match> list = in.getCadeia_elementos();
            if (list.get(0).getLexema().equals("in"))
                System.out.println("hum");
            boolean erro = false;
            for (int i = 2; i < list.size() && !erro; i++)
            {
                boolean ValorCompativelComTipo = false;
                if (list.get(i).getToken().equals(Token.tIdentificador))
                {
                    Token tipo_Da_Variavel = getTipo(list.get(i));
                    if (((list.get(i).getToken().equals(Token.tIdentificador) && getTipo(list.get(i)).equals(Token.tDouble))
                            || list.get(i).getToken().equals(Token.tValor_Decimal))
                            && (tipo.equals(Token.tINT) || tipo.equals(Token.tValor_Inteiro)))
                    {
                        erros_avisos_semanticos.add(new Aviso(Aviso.perca_De_Precisao.getCodigo(),
                                "Possível perda de precisão devido ao tipo de " + list.get(i).getLexema().getPalavra(), list.get(i).getLexema()));
                    }
                    else if (!tipo.getIdToken().equals(tipo_Da_Variavel.getIdToken()))
                    {
                        erros_avisos_semanticos.add(new Erro(Erro.valor_nao_compativel,
                                list.get(i).getLexema().getPalavra() + " é do tipo " + tipo_Da_Variavel.getIdToken()
                                + " que não pode ser convertido para " + tipo.getIdToken(), list.get(i).getLexema()));
                    }

                }
                else if (Token.tValores.contains(list.get(i).getToken()))
                {
                    //por aqui
                    if (list.get(i).getToken().equals(Token.tIdentificador))
                        tipo_aux = getTipo(list.get(i));
                    else
                        tipo_aux = list.get(i).getToken();

                    for (int j = 0; j < vet.length && !ValorCompativelComTipo; j++)
                    {
                        ValorCompativelComTipo = vet[j].equals(list.get(i).getToken());
                    }
                    if ((tipo_aux.equals(Token.tDouble) || tipo_aux.equals(Token.tValor_Decimal))
                            && (tipo.equals(Token.tINT) || tipo.equals(Token.tValor_Inteiro)))
                    {
                        erros_avisos_semanticos.add(new Aviso(Aviso.perca_De_Precisao.getCodigo(),
                                "Possível perda de precisão devido ao tipo de " + list.get(i).getLexema().getPalavra(), list.get(i).getLexema()));
                    }
                    else if (!ValorCompativelComTipo)
                    {
                        erros_avisos_semanticos.add(new Erro(Erro.valor_nao_compativel, list.get(i).getLexema().getPalavra()
                                + " não pode ser convertido para " + tipo.getIdToken(), list.get(i).getLexema()));
                    }
                }
            }
        });

        lacos.forEach(in ->
        {
            if (in.getCadeia_elementos().get(0).getToken().equals(Token.tFor))
                quebraFor(in);
        });

    }

    private Token getTipo(Match id)
    {
        try
        {
            int i;
            Token ret = null;
            for (i = 0; i < declaracoes.size() && ret == null; i++)
            {
                if (declaracoes.get(i).getCadeia_elementos().contains(id))
                {
                    ret = declaracoes.get(i).getCadeia_elementos().get(0).getToken();
                }
            }
            return ret;

        }
        catch (Exception ex)
        {
            System.out.println("meh");
            return null;
        }
    }

    private void quebraFor(Instrucao in)
    {
        List<Match> cadeia_declaracao = new ArrayList<>();
        List<Match> cadeia_atribuicao = new ArrayList<>();
        Token tipo;

        int i = 0;
        while (!in.getCadeia_elementos().get(i).getToken().equals(Token.tPontoVirgula))
        {
            cadeia_declaracao.add(in.getCadeia_elementos().get(i));
            i++;
        }
        i++;
        while (!in.getCadeia_elementos().get(i).getToken().equals(Token.tPontoVirgula))
        {
            i++;
        }
        i++;
        while (i < in.getCadeia_elementos().size() && !in.getCadeia_elementos().get(i).getToken().equals(Token.tParenteses_fecha))
        {
            cadeia_atribuicao.add(in.getCadeia_elementos().get(i));
            i++;
        }
        List<Match> list = cadeia_declaracao;
        list.remove(0);
        list.remove(0);
        if (Token.tTipos.contains(list.get(0).getToken()))
        {
            tipo = list.get(0).getToken();
            list.remove(0);

        }
        else
        {
            tipo = getTipo(list.get(0));
        }
        microTipagem(tipo, list);

        list = cadeia_atribuicao;
        Token auxtipo = getTipo(list.get(0));
        if (auxtipo != null)
            tipo = auxtipo;

        microTipagem(tipo, list);

    }

    private void microTipagem(Token tipo, List<Match> list)
    {
        Token[] vet = tipo.getGerados();
        Token tipo_aux;
        for (int j = 2; j < list.size(); j++)
        {
            boolean ValorCompativelComTipo = false;
            if (!list.get(j).getLexema().getPalavra().equals(list.get(0).getLexema().getPalavra())
                    && (Token.tValores.contains(list.get(j).getToken()) || list.get(j).getToken().equals(Token.tIdentificador)))
            {

                if (list.get(j).getToken().equals(Token.tIdentificador))
                    tipo_aux = getTipo(list.get(j));
                else
                    tipo_aux = list.get(j).getToken();

                for (int k = 0; k < vet.length && !ValorCompativelComTipo; k++)
                {
                    ValorCompativelComTipo = vet[k].equals(tipo_aux);
                }
                if (((list.get(j).getToken().equals(Token.tIdentificador) && getTipo(list.get(j)).equals(Token.tDouble))
                        || list.get(j).getToken().equals(Token.tValor_Decimal))
                        && (tipo.equals(Token.tINT) || tipo.equals(Token.tValor_Inteiro)))
                {
                    erros_avisos_semanticos.add(new Aviso(Aviso.perca_De_Precisao.getCodigo(),
                            "Possível perda de precisão devido ao tipo de " + list.get(j).getLexema().getPalavra(), list.get(j).getLexema()));
                }
                else if (!ValorCompativelComTipo)
                {
                    erros_avisos_semanticos.add(new Erro(Erro.valor_nao_compativel, list.get(j).getLexema().getPalavra()
                            + " não pode ser convertido para " + tipo.getIdToken(), list.get(j).getLexema()));
                }
            }
        }
    }

    private Instrucao getdeclar(Instrucao in)
    {
        Instrucao intr = new Instrucao(Conversor.declaracao);

        List<Match> list = in.getCadeia_elementos();

        for (int i = 2; i < list.size(); i++)
        {
            intr.addCadeia_elementos(list.get(i));
        }
        return intr;
    }

    private void possiveisOtimizacoes()
    {
        int cgeral, catrib;
        Match auxId;
        for (int i = 0; i < instrucoes.size(); i++)
        {
            if (instrucoes.get(i).getNome_conversor().equals(Conversor.declaracao))
            {
                auxId = instrucoes.get(i).getCadeia_elementos().get(1);

                cgeral = catrib = 0;
                for (int j = i + 1; j < instrucoes.size(); j++)
                {
                    if (instrucoes.get(j).getCadeia_elementos().contains(auxId))
                    {
                        List<Match> list = instrucoes.get(j).getCadeia_elementos();
                        for (int k = 0; k + 1 < list.size(); k++)
                        {
                            if (list.get(k).equals(auxId)
                                    && list.get(k + 1).getToken().equals(Token.tIgual))
                            {
                                catrib++;
                            }
                        }
                        if (!instrucoes.get(j).getNome_conversor().equals(Conversor.atribuicao))
                            cgeral++;
                    }
                }
                if (cgeral == 0)
                {
                    erros_avisos_semanticos.add(new Aviso(Aviso.nunca_utilizado.getCodigo(),
                            auxId.getLexema().getPalavra() + " nunca é utilizado ", auxId.getLexema()));
                }
                else if (catrib == 0)
                    erros_avisos_semanticos.add(new Aviso(Aviso.constante_em_potencial.getCodigo(),
                            auxId.getLexema().getPalavra() + " é uma constante em potencial", auxId.getLexema()));
            }
        }
    }

    private void conversaoCI()
    {
        Conversor.goto_aux = 0;
        Stack<Instrucao> pilha = new Stack<>();
        lci = new ArrayList<>();
        List<InstrucaoIntermediaria> l = null;
        otimizador();
        int gaux;
        for (int i = 0; i < instrucoes.size(); i++)
        {
            Instrucao in = instrucoes.get(i);

            switch (in.getNome_conversor())
            {
                case Conversor.If:
                    l = in.toCodigoIntermediario();
                    gaux = Conversor.goto_aux;
                    l.add(new InstrucaoIntermediaria(Arrays.asList(new Match(
                            new Lexema("goto markJMPEND" + gaux, 0, 0), Token.tgoto)), "", 0));
                    l.add(new InstrucaoIntermediaria(Arrays.asList(new Match(
                            new Lexema("markJMPA" + gaux, 0, 0), Token.tgoto_mark)), "", 0));
                    i++;
                    pilha.clear();
                    in = instrucoes.get(i);
                    if (in.getNome_conversor().equals(Conversor.escIni))
                    {
                        pilha.push(in);
                        for (i++; i < instrucoes.size() && !pilha.isEmpty(); i++)
                        {
                            in = instrucoes.get(i);
                            if (in.getNome_conversor().equals(Conversor.escIni))
                                pilha.push(in);
                            else if (in.getNome_conversor().equals(Conversor.escFim))
                                pilha.pop();
                            else
                            {
                                l.addAll(in.toCodigoIntermediario());
                            }
                        }
                        i--;

                        l.add(new InstrucaoIntermediaria(Arrays.asList(new Match(new Lexema("markJMPEND" + gaux, 0, 0), Token.tgoto_mark)), "", 0));
                    }
                    Conversor.goto_aux++;
                    break;
                case Conversor.While:
                    pilha.clear();
                    gaux = Conversor.goto_aux;
                    l = new ArrayList<>();
                    l.add(new InstrucaoIntermediaria(Arrays.asList(new Match(new Lexema("markJMP" + gaux, 0, 0), Token.tgoto_mark)), "", 0));
                    l.addAll(in.toCodigoIntermediario());
                    l.add(new InstrucaoIntermediaria(Arrays.asList(new Match(new Lexema("goto markJMPEND" + gaux, 0, 0), Token.tgoto)), "", 0));
                    l.add(new InstrucaoIntermediaria(Arrays.asList(new Match(new Lexema("markJMPA" + gaux, 0, 0), Token.tgoto_mark)), "", 0));

                    i++;
                    in = instrucoes.get(i);
                    if (in.getNome_conversor().equals(Conversor.escIni))
                    {
                        pilha.push(in);
                        for (i++; i < instrucoes.size() && !pilha.isEmpty(); i++)
                        {
                            in = instrucoes.get(i);
                            if (in.getNome_conversor().equals(Conversor.escIni))
                                pilha.push(in);
                            else if (in.getNome_conversor().equals(Conversor.escFim))
                                pilha.pop();
                            else
                            {
                                l.addAll(in.toCodigoIntermediario());
                            }
                        }
                        i--;
                        l.add(new InstrucaoIntermediaria(Arrays.asList(new Match(new Lexema("goto markJMP" + gaux, 0, 0), Token.tgoto)), "", 0));
                        l.add(new InstrucaoIntermediaria(Arrays.asList(new Match(new Lexema("markJMPEND" + gaux, 0, 0), Token.tgoto_mark)), "", 0));
                    }
                    Conversor.goto_aux++;
                    break;
                case Conversor.For:
                    pilha.clear();
                    int posNull;
                    gaux = Conversor.goto_aux;
                    l = in.toCodigoIntermediario();//coloca um null onde o código fica dentro
                    posNull = l.indexOf(null);
                    l.remove(posNull);
                    i++;
                    in = instrucoes.get(i);
                    if (in.getNome_conversor().equals(Conversor.escIni))
                    {
                        pilha.push(in);
                        for (i++; i < instrucoes.size() && !pilha.isEmpty(); i++)
                        {
                            in = instrucoes.get(i);
                            if (in.getNome_conversor().equals(Conversor.escIni))
                                pilha.push(in);
                            else if (in.getNome_conversor().equals(Conversor.escFim))
                                pilha.pop();
                            else
                            {
                                List<InstrucaoIntermediaria> laaux = in.toCodigoIntermediario();
                                int auxpos = posNull + laaux.size();
                                l.addAll(posNull, laaux);
                                posNull = auxpos;
                            }
                        }

                    }
                    l.add(new InstrucaoIntermediaria(Arrays.asList(new Match(new Lexema("goto markJMP" + gaux, 0, 0), Token.tgoto)), "", 0));
                    l.add(new InstrucaoIntermediaria(Arrays.asList(new Match(new Lexema("markJMPEND" + gaux, 0, 0), Token.tgoto_mark)), "", 0));
                    Conversor.goto_aux++;

                    break;
                case Conversor.escIni:
                    l = null;
                    break;
                case Conversor.escFim:
                    l = null;
                    break;
                default:
                    l = in.toCodigoIntermediario();
                    break;
            }

            if (l != null)
            {
                lci.addAll(l);
            }
        }

    }

    private void condicoes()
    {
        List<Token> pc = new ArrayList<>();//permitidos_em_condicoes
        pc.addAll(Token.tOpLogicos);
        pc.addAll(Token.tOpRelacional);
        pc.addAll(Token.tOperadores);
        pc.addAll(Token.tValores);
        pc.add(Token.tParenteses_abre);
        pc.add(Token.tParenteses_fecha);
        pc.add(Token.tIdentificador);

        boolean flag = false;

        for (Instrucao in : instrucoes)
        {
            List<Match> l = new ArrayList<>(in.getCadeia_elementos());

            if (in.getNome_conversor().equals(Conversor.While)
                    || in.getNome_conversor().equals(Conversor.If) || flag)
            {

                for (Match m : l)
                {
                    if (m.getToken().equals(Token.tIdentificador))
                    {
                        if (getTipo(m).equals(Token.tString))
                        {
                            erros_avisos_semanticos.add(new Erro(Erro.valor_nao_compativel,
                                    "String não pode ser usada em expressões condicionais", m.getLexema()));
                        }
                    }
                    else if (m.getToken().equals(Token.tValor_String))
                    {
                        erros_avisos_semanticos.add(new Erro(Erro.valor_nao_compativel,
                                "String não pode ser usada em expressões condicionais", m.getLexema()));
                    }
                }
            }
        }
    }

    private int countErros(List<Controle> erros_avisos_semanticos)
    {
        int c = 0;
        for (Controle item : erros_avisos_semanticos)
        {
            if (item != null && item instanceof Erro)
                c++;
        }
        return c;
    }

    private void otimizador()
    {
        R2();
        R1();
        R2();
        R3();
        R4();
        R5();
        R6();
        R7();
    }

    private void R1()//Variaveis só usadas uma vez viram constantes
    {
        List<Instrucao> dec = getDeclaracoes();
        List<Instrucao> atri = getAtribuicoes();

        List<Match> auxList, auxList2;
        List<Instrucao> mremocao = new ArrayList<>();
        boolean flag;

        //descarta variaveis que dependem de outras em sua declaração
        for (Instrucao i : dec)
        {
            auxList = i.getCadeia_elementos();

            flag = true;
            for (int j = 2; j < auxList.size() && flag; j++)
            {
                if (auxList.get(j).getToken().equals(Token.tIdentificador))
                {
                    flag = false;
                }
            }

            for (int j = 0; j < atri.size() && flag; j++)
            {
                if (atri.get(j).getCadeia_elementos().get(0).equals(i.getCadeia_elementos().get(1)))
                    flag = false;
            }
            if (!flag)
                mremocao.add(i);
        }
        dec.removeAll(mremocao);

        for (Instrucao elem : dec)
        {
            auxList = new ArrayList<>(elem.getCadeia_elementos());
            Match variavel = auxList.get(1);
            auxList.remove(0);//tira o tipo
            auxList.remove(0);//tira a variavel
            auxList.remove(0);//tira a =

            if (auxList.get(auxList.size() - 1).getToken().equals(Token.tPontoVirgula))
                auxList.remove(auxList.size() - 1);

            for (Instrucao in : instrucoes)
            {
                if (elem != in)
                {
                    auxList2 = in.getCadeia_elementos();
                    int index = auxList2.indexOf(variavel);

                    if (index != -1)
                    {
                        in.getCadeia_elementos().remove(index);
                        in.getCadeia_elementos().addAll(index, auxList);
                        List<Match> tmp = in.getCadeia_elementos();
                    }
                }

            }
        }

        List<Match> variaveis = new ArrayList<>();

        for (Instrucao i : dec)
        {
            variaveis.add(i.getCadeia_elementos().get(1));
        }
        int[] vetCount = new int[variaveis.size()];

    }

    private void R2()//Variaveis nunca usadas são removidas
    {
        HashMap map = new HashMap();//para realizar as substituicoes depois
        Match aux;
        List<Instrucao> dec = getDeclaracoes();
        List<Instrucao> mremocao = new ArrayList<>();

        int c = 0;
        for (Instrucao in : dec)
        {
            aux = in.getCadeia_elementos().get(1);
            c = 0;
            for (Instrucao i : instrucoes)
            {
                if (i.getCadeia_elementos().contains(aux))
                    c++;
            }
            if (c <= 1)
                mremocao.add(in);
        }
        instrucoes.removeAll(mremocao);
    }

    private void R3()//operações entre valores simplificados exemplo: x = 50+50 vira x = 100
    {
        List<Instrucao> dec = getDeclaracoes();
        List<Instrucao> atri = getAtribuicoes();
        Match auxM, igual, tipo;
        String expressao;
        List<Match> aux;

        for (Instrucao i : dec)
        {
            //não é declaração simples
            if (i.getCadeia_elementos().size() > 2
                    && !i.getCadeia_elementos().get(3).equals(Token.tPontoVirgula))
            {
                aux = new ArrayList<>(i.getCadeia_elementos());
                tipo = aux.get(0);
                aux.remove(0);//tipo
                auxM = aux.get(0);
                aux.remove(0);//variavel
                igual = aux.get(0);
                aux.remove(0);//=
                if (aux.get(aux.size() - 1).getToken().equals(Token.tPontoVirgula))
                    aux.remove(aux.size() - 1);

                boolean flag = true;
                expressao = "";
                for (int j = 0; j < aux.size() && flag; j++)
                {
                    if (aux.get(j).getToken().equals(Token.tIdentificador))
                    {
                        flag = false;
                    }
                    else
                        expressao += aux.get(j).getLexema().getPalavra();
                }
                //otimizavel
                if (flag)
                {
                    try
                    {
                        expressao = Util.resolveExpressoes(expressao);
                        aux.clear();
                        if (tipo.getToken().equals(Token.tString))
                            expressao = "\"" + expressao + "\"";
                        aux.add(tipo);
                        aux.add(auxM);
                        aux.add(igual);
                        aux.add(new Match(new Lexema(expressao, 0, 0), tipo.getToken()));

                        i.setCadeia_elementos(aux);
                    }
                    catch (ScriptException ex)
                    {
                        System.out.println("Erro no eval classe Util");
                    }
                }
            }

        }
        for (Instrucao i : atri)
        {
            aux = new ArrayList<>(i.getCadeia_elementos());
            auxM = aux.get(0);
            aux.remove(0);//variavel
            igual = aux.get(0);
            aux.remove(0);//=

            if (aux.get(aux.size() - 1).getToken().equals(Token.tPontoVirgula))
                aux.remove(aux.size() - 1);

            boolean flag = true;
            expressao = "";
            for (int j = 0; j < aux.size() && flag; j++)
            {
                if (aux.get(j).getToken().equals(Token.tIdentificador))
                {
                    flag = false;
                }
                else
                    expressao += aux.get(j).getLexema().getPalavra();
            }
            //otimizavel
            if (flag)
            {
                try
                {
                    expressao = Util.resolveExpressoes(expressao);
                    aux.clear();
                    Token ttipo = getTipo(auxM);
                    if (ttipo.equals(Token.tString))
                        expressao = "\"" + expressao + "\"";
                    aux.add(auxM);
                    aux.add(igual);
                    aux.add(new Match(new Lexema(expressao, 0, 0), ttipo));

                    i.setCadeia_elementos(aux);
                }
                catch (ScriptException ex)
                {
                    System.out.println("Erro no eval classe Util");
                }
            }
        }
    }

    private void R4()//verificação de constantes em condicionais
    {
        List<Instrucao> mremocao = new ArrayList<>();
        int size = instrucoes.size();
        for (int in = 0; in < size; in++)
        {
            Instrucao i = instrucoes.get(in);
            List<Match> aux;
            switch (i.getNome_conversor())
            {
                case Conversor.If:
                    aux = new ArrayList<>(i.getCadeia_elementos());
                    break;
                case Conversor.While:
                    aux = new ArrayList<>(i.getCadeia_elementos());
                    break;
                case Conversor.For:
                    aux = new ArrayList<>(i.getCadeia_elementos());
                    retiraPorToken(aux, Token.tFor, Token.tParenteses_abre, Token.tParenteses_fecha);
                    while (!aux.get(0).getToken().equals(Token.tPontoVirgula))
                        aux.remove(0);
                    aux.remove(0);//retira o ponto e virgula
                    int index = findPorToken(aux, Token.tPontoVirgula);
                    while(index < aux.size())
                        aux.remove(index);
                    break;
                default:
                    aux = null;
                    break;
            }
            if (aux != null && !aux.isEmpty())
            {
                retiraPorToken(aux, Token.tParenteses_abre, Token.tParenteses_fecha, Token.tPontoVirgula, Token.tIf, Token.tWhile);
                if (aux.size() == 1)
                {
                    if (aux.get(0).getLexema().getPalavra().equals("false")
                            || aux.get(0).getLexema().getPalavra().equals("0"))
                    {
                        if (instrucoes.get(in + 1).getNome_conversor().equals(Conversor.escIni))
                        {
                            boolean flag = true;
                            for (; in < size && flag; in++)
                            {
                                i = instrucoes.get(in);
                                if (i.getNome_conversor().equals(Conversor.escFim))
                                    flag = false;
                                mremocao.add(i);
                            }
                        }
                    }
                    else
                        mremocao.add(i);
                }
            }

        }
        instrucoes.removeAll(mremocao);
    }

    private void R5()//geração de atribuições equivalentes simplificadas, exemplo: x = y*1 vira x = y
    {

    }

    private void R6()//eliminação de atibuição desnecessária, exemplo x = x
    {

    }

    private void R7()
    {

    }

    private List<Instrucao> getAtribuicoes()
    {
        return getPorTipo(Conversor.atribuicao);
    }

    private List<Instrucao> getDeclaracoes()
    {
        return getPorTipo(Conversor.declaracao);
    }

    private List<Instrucao> getPorTipo(String nomeConversor)
    {
        List<Instrucao> aux = new ArrayList<>();

        for (Instrucao i : instrucoes)
        {
            if (i.getNome_conversor().equals(nomeConversor))
                aux.add(i);
        }
        return aux;
    }

    private void retiraPorToken(List<Match> aux, Token... tokens)
    {
        List tks = Arrays.asList(tokens);

        List<Match> mremocao = new ArrayList<>();

        for (Match m : aux)
        {
            if (tks.contains(m.getToken()))
                mremocao.add(m);
        }
        aux.removeAll(mremocao);
    }

    private int findPorToken(List<Match> aux, Token t)
    {
        int i;
        for (i = 0; i < aux.size() && !aux.get(i).getToken().equals(t); i++)
        {
            
        }
        return i;
    }
}
