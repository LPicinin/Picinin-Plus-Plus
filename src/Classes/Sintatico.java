/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.Controle.Erro;
import Classes.Controle.Match;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author luish
 */
public class Sintatico extends Analisador
{

    private Lexico al_lexica;
    private Stack<String> pilha_simbolos;
    private List<Match> lexemas_tokens_correspondidos;
    private List<Erro> erros;
    

    public Sintatico()
    {
        pilha_simbolos = new Stack<>();
        lexemas_tokens_correspondidos = new ArrayList<>();
        erros = new ArrayList<>();
        al_lexica = new Lexico();
    }

    public Stack<String> getPilha_simbolos()
    {
        return pilha_simbolos;
    }

    public void setPilha_simbolos(Stack<String> pilha_simbolos)
    {
        this.pilha_simbolos = pilha_simbolos;
    }

    public List<Match> getLexemas_tokens_correspondidos()
    {
        return lexemas_tokens_correspondidos;
    }

    public void setLexemas_tokens_correspondidos(List<Match> lexemas_tokens_correspondidos)
    {
        this.lexemas_tokens_correspondidos = lexemas_tokens_correspondidos;
    }

    public List<Erro> getErros()
    {
        return erros;
    }

    public void setErros(List<Erro> erros)
    {
        this.erros = erros;
    }

    @Override
    public List<Object> analise(Lexema lex)
    {
        String codeString = lex.getLexema();
        int i;
        posParagrafo = 0;
        posLinha = 0;
        code = codeString.toCharArray();
        StringBuilder cadeia = new StringBuilder();
        boolean fespecial = true;

        //percorre o código até o fim do código
        for (pos = 0; pos < code.length; pos++)
        {
            //consome espaços, tabs e quebras de linha
            consomeCaracteres(code);
            i = pos;
            //monta cadeia até um caracter especial ou ignorado
            while (i < code.length && !(fespecial = caracteresEspeciais.contains(code[i])) && !caracteresIgnorados.contains(code[i]))
            {
                cadeia.append(code[i]);
                i++;
            }
            pos = i;
            if (cadeia.length() > 0)//achou uma cadeia
            {
                //System.out.println(cadeia.toString());
                addRepostaLexico(new Lexema(cadeia.toString(), posParagrafo, posLinha));
                //al_lexica.analise(cadeia.toString());
                cadeia.setLength(0);
            }

            if (fespecial)//achou um caracter especial
            {
                if (code[pos] != '"')
                    cadeia.append(code[pos]);
                else
                {
                    //consome de " até outro " ou \n(erro)
                    if (consomeString(code, cadeia))
                    {
                        //achou uma string
                    } else
                    {
                        //achou um erro
                    }
                }
                //System.out.println(cadeia.toString());
                addRepostaLexico(new Lexema(cadeia.toString(), posParagrafo, posLinha));
                cadeia.setLength(0);
            }

        }
        return null;
    }

    /*
int main()
{
    double x = 543.56;
    string s = "skbdkslb slndbçs smpn"
}
     */
    private void consomeCaracteres(char[] code)
    {
        while (pos < code.length && caracteresIgnorados.contains(code[pos]))
        {
            if (code[pos] == '\n')
            {
                posParagrafo++;
                posLinha = 0;
            } else
                posLinha++;
            pos++;
        }
    }

    private boolean consomeString(char[] code, StringBuilder cadeia)
    {
        int posini = pos;
        cadeia.append(code[pos++]);
        posLinha++;
        while (pos < code.length && code[pos] != '"' && code[pos] != '\n')
        {
            cadeia.append(code[pos++]);
            posLinha++;
        }
        if (code[pos] == '"')
        {
            cadeia.append(code[pos++]);
            posLinha++;
        } else
        {
            try
            {
                Erro error = (Erro) Erro.tokenFinalDeCadeiaInesperada.clone();
                error.setLexema(new Lexema(cadeia.toString(), posParagrafo, posini));
                addErro(error);
                return false;
            } catch (CloneNotSupportedException ex)
            {
                System.out.println("Erro Compilador> " + ex.getMessage());
            }
            erros.add(Erro.tokenNaoEncontrado);
        }
        return true;
    }

    private void addErro(Erro e)
    {
        erros.add(e);
    }

    private void addMatch(Match m)
    {
        lexemas_tokens_correspondidos.add(m);
    }

    private void addRepostaLexico(Lexema cadeia)
    {
        Object resposta = al_lexica.analise(cadeia);
        if (resposta instanceof Erro)
        {
            Erro e = (Erro) resposta;
            erros.add(e);
            //System.out.println(e.getLexema() + " = Erro:> " + e.getMensagem());
        } else if (resposta instanceof Match)
        {
            Match m = (Match) resposta;
            lexemas_tokens_correspondidos.add(m);
            //System.out.println(m.getLexema().getLexema() + " = " + m.getToken().getIdToken());
        }
    }
}
