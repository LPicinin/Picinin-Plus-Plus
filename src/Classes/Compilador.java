/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author luish
 */
public class Compilador
{
    //identificadoe - lexema - (se id tipo) - variavel(escopo, tipo e valor)
    private String code;
    private List<Object> erros_avisos;
    private Stack<String> pilha_simbolos;
    
    public Compilador()
    {
    }

    public Compilador(String code)
    {
        this.code = code;
    }
    
    public void analisar()
    {
        erros_avisos = new ArrayList<>();
        
    }
}
