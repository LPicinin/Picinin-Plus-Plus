/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.Controle.Controle;
import Classes.Controle.Conversor;
import Classes.Controle.Instrucao;
import Classes.Controle.Match;
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

    public List<Controle> extrair()
    {
        erros_avisos_semanticos = new ArrayList<>();
        otimizar();
        /*
        instrucoes.forEach((in) ->
        {
            System.out.println(in.toString());
        });
        */
        return erros_avisos_semanticos;
    }

    private void otimizar()
    {
        List<Instrucao> declaracoes = new ArrayList<>();
        List<Instrucao> atribuicoes = new ArrayList<>();
        List<Instrucao> lacos = new ArrayList<>();
        List<Instrucao> condicoes = new ArrayList<>();

        instrucoes.forEach((in) ->
        {
            switch (in.getNome_conversor())
            {
                case Conversor.atribuicao:
                    if (contain(Token.tIgual, in) == -1)
                        atribuicoes.add(in);
                    else//quebra declaração com atribuição :)
                    {
                        List<Instrucao> da = quebraDeclaracao(in);//declaracao[0] e atribuicao[1]
                        declaracoes.add(da.get(0));
                        atribuicoes.add(da.get(1));
                    }
                    break;
                case Conversor.declaracao:
                    declaracoes.add(in);
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
        return i  == size ? -1 : i;
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
        
        for(; i < list.size(); i++)
        {
            atrib.addCadeia_elementos(list.get(i));
        }
        
        ArrayList<Instrucao> aux_ret = new ArrayList<>();
        aux_ret.add(dec);
        aux_ret.add(atrib);
        
        return aux_ret;
    }

}
