/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luish
 */
public class Compilador
{
    private String code;

    public Compilador()
    {
    }

    public Compilador(String code)
    {
        this.code = code;
    }
    
    public List<Object> analisar()
    {
        List<Object> erros_avisos = new ArrayList<>();
        
        return erros_avisos;
    }
}
