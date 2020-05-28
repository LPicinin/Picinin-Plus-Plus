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
    public static final Erro instrucao_nao_pertencente_ha_linguagem = new Erro(412, "Instrução irregular detectada");
    public static final int valor_nao_compativel = 562;
    public static final int declaracaoIncorreta = 563;

    private boolean semantico = false;

    public Erro(int codigo, String mensagem)
    {
        super(codigo, mensagem);
    }

    public Erro(int codigo, String mensagem, boolean semantico)
    {
        super(codigo, mensagem);
        this.semantico = semantico;
    }

    public Erro(int codigo, String mensagem, Lexema lexema)
    {
        super(codigo, mensagem, lexema);
    }
    public Erro(int codigo, String mensagem, Lexema lexema, boolean semantico)
    {
        super(codigo, mensagem, lexema);
        this.semantico = semantico;
    }

    public static Erro getError(Erro e, Lexema lexema)
    {
        return new Erro(e.codigo, e.mensagem, lexema);
    }

    @Override
    public String toString()
    {
        return "○ Erro detectado próximo à \'" + lexema.getPalavra() + "\' na Linha: " + (lexema.getPosParagrafo() + 1) + " - Erro(" + (semantico ? 8001 : codigo) + ") - " + mensagem + "";
    }
}
