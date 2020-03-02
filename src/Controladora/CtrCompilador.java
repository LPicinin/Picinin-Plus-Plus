/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladora;

import Classes.Compilador;
import java.util.List;

/**
 *
 * @author luish
 */
public class CtrCompilador
{
    private static CtrCompilador ctrCompilador;
    private Compilador compilador;
    
    private CtrCompilador()
    {
        compilador = new Compilador();
    }
    public static CtrCompilador instancia()
    {
        if(ctrCompilador == null)
            ctrCompilador = new CtrCompilador();
        return ctrCompilador;
    }

    public void Analisar(String code)
    {
        compilador = new Compilador(code);
        compilador.analisar();
    }
    
    
}
