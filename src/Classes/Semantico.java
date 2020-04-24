/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.Controle.Instrucao;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luish
 */
public class Semantico extends Constantes
{

    private List<Instrucao> instrucoes;

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

    public Object analise()
    {
        return null;
    }

}
