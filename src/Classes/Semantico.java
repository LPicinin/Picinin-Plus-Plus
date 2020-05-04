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
import Classes.Controle.Match;
import Classes.Controle.Simbolo;
import Controladora.CtrCompilador;
import java.util.ArrayList;
import java.util.List;

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

    public Semantico()
    {
        instrucoes = new ArrayList<>();
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
        tipagem();
    }

    private void tipagem()
    {
        atribuicoes.forEach(in ->
        {
            Token tipo = getTipo(in.getCadeia_elementos().get(0));
            Token[] vet = tipo.getGerados();
            List<Match> list = in.getCadeia_elementos();

            boolean erro = false;
            for (int i = 2; i < list.size() && !erro; i++)
            {
                boolean ValorCompativelComTipo = false;
                if (list.get(i).getToken().equals(Token.tIdentificador))
                {
                    Token tipo_Da_Variavel = getTipo(list.get(i));
                    if (!tipo.getIdToken().equals(tipo_Da_Variavel.getIdToken()))
                    {
                        erros_avisos_semanticos.add(new Erro(Erro.valor_nao_compativel,
                                list.get(i).getLexema().getPalavra() + " é do tipo " + tipo_Da_Variavel.getIdToken()
                                + " que não pode ser convertido para " + tipo.getIdToken(), list.get(i).getLexema()));
                    }

                } else if (Token.tValores.contains(list.get(i).getToken()))
                {
                    for (int j = 0; j < vet.length && !ValorCompativelComTipo; j++)
                    {
                        ValorCompativelComTipo = vet[j].equals(list.get(i).getToken());
                    }
                    if ((list.get(i).getToken().equals(Token.tDouble) || list.get(i).getToken().equals(Token.tValor_Decimal))
                        && (tipo.equals(Token.tINT) || tipo.equals(Token.tValor_Inteiro)))
                    {
                        erros_avisos_semanticos.add(new Aviso(Aviso.perca_De_Precisao.getCodigo(),
                                "Possível perda de precisão devido ao tipo de " + list.get(i).getLexema().getPalavra(), list.get(i).getLexema()));
                    } else if (!ValorCompativelComTipo)
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
            boolean flag = true;
            Token ret = null;
            for (i = 0; i < declaracoes.size() && ret == null; i++)
            {
                if (declaracoes.get(i).getCadeia_elementos().contains(id))
                {
                    ret = declaracoes.get(i).getCadeia_elementos().get(0).getToken();
                }
            }
            return ret;

        } catch (Exception ex)
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

        } else
        {
            tipo = getTipo(list.get(0));
        }
        microTipagem(tipo, list);
        
        list = cadeia_atribuicao;
        Token auxtipo = getTipo(list.get(0));
        if(auxtipo != null)
            tipo = auxtipo;
        
        microTipagem(tipo, list);
        

    }

    private void microTipagem(Token tipo, List<Match> list)
    {
        Token[] vet = tipo.getGerados();

        for (int j = 0; j < list.size(); j++)
        {
            boolean ValorCompativelComTipo = false;
            if (Token.tValores.contains(list.get(j).getToken()))
            {
                for (int k = 0; k < vet.length && !ValorCompativelComTipo; k++)
                {
                    ValorCompativelComTipo = vet[k].equals(list.get(j).getToken());
                }
                if ((list.get(j).getToken().equals(Token.tDouble) || list.get(j).getToken().equals(Token.tValor_Decimal))
                        && (tipo.equals(Token.tINT) || tipo.equals(Token.tValor_Inteiro)))
                {
                    erros_avisos_semanticos.add(new Aviso(Aviso.perca_De_Precisao.getCodigo(),
                            "Possível perda de precisão devido ao tipo de " + list.get(j).getLexema().getPalavra(), list.get(j).getLexema()));
                } else if (!ValorCompativelComTipo)
                {
                    erros_avisos_semanticos.add(new Erro(Erro.valor_nao_compativel, list.get(j).getLexema().getPalavra()
                            + " não pode ser convertido para " + tipo.getIdToken(), list.get(j).getLexema()));
                }
            }
        }
    }

}
