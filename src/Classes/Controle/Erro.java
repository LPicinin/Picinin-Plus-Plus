/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Controle;

import Classes.Lexema;

/**
 *
 * @author luish
 */
public class Erro extends Controle
{

    public static final Erro tokenNaoEncontrado = new Erro(404, "Não foi possível encontrar um token correspondente");
    public static final Erro tokenFinalDeCadeiaInesperada = new Erro(405, "Sequência de cadeia inesperada");
    public static final Erro variavelNaoDeclarada = new Erro(406, "Variavel não declarada");
    public static final Erro variavel_Ja_Declarada = new Erro(407, "Variavel já declarada");
    public static final Erro Inicio_da_Linguagem = new Erro(408, "Inicio de linguagem não condizente com o especificado");
    public static final Erro naoCompletado = new Erro(409, "Inicio condinzente com a linguagem mas não completada");
    public static final Erro expressaoIlegal = new Erro(410, "Expressão Irregular detectada!!");
    public static final Erro chavesFaltantes = new Erro(411, "chave esperado");
    public static final Erro parenteseFaltante = new Erro(411, "parentese esperado");
    public static final Erro codigo_Escopo_fora_do_fluxo = new Erro(666, "Escopo fora do fluxo de execução detectado!!!");

    public Erro(int codigo, String mensagem)
    {
        super(codigo, mensagem);
    }

    public Erro(int codigo, String mensagem, Lexema lexema)
    {
        super(codigo, mensagem, lexema);
    }
    public static Erro getError(Erro e, Lexema lexema)
    {
        return new Erro(e.codigo, e.mensagem, lexema);
    }
    @Override
    public String toString()
    {
        return "Erro detectado próximo à \'"+lexema.getPalavra()+"\' na Linha: "+(lexema.getPosParagrafo()+1)+" - Erro(" + codigo + ") - " + mensagem + "";
    }
}
