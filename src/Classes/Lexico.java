/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.Controle.Erro;
import Classes.Controle.Match;

/**
 *
 * @author luish
 */
public class Lexico extends Constantes
{

    private int pos;

    public Object analise(Lexema lex)
    {
        String palavra = lex.getPalavra();
        //System.out.println(palavra);
        boolean naoAchou = true;
        int i;
        for (i = 0; i < Token.tokens.size() && naoAchou; i++)
        {
            naoAchou = !palavra.matches(Token.tokens.get(i).getRegex());
        }
        if (!naoAchou)
        {
            return new Match(lex, Token.tokens.get(i - 1));
        } else
        {
            return new Match(lex, Token.tNaoReconhecido);
            //return Erro.getError(Erro.tokenNaoEncontrado, lex);
        }
    }

    public void analise()
    {
        int i;
        posParagrafo = 0;
        posLinha = 0;
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
            if (i < code.length)
            {
                pos = i;
                if (i + 1 < code.length && code[i] == '/' && code[i + 1] == '/')
                {
                    while (pos < code.length && code[pos] != '\n')
                        pos++;
                    posParagrafo++;
                    posLinha = 0;
                } else
                {
                    if (code[i] == '\n')
                        posParagrafo++;
                    if (cadeia.length() > 0)//achou uma cadeia
                    {
                        //System.out.println(cadeia.toString());
                        addRepostaLexico(cadeia.toString());
                        //al_lexica.analise(cadeia.toString());
                        cadeia.setLength(0);
                    }

                    if (fespecial)//achou um caracter especial
                    {
                        if (code[pos] != '"')
                        {
                            cadeia.append(code[pos]);
                            posLinha++;
                            if (pos + 1 < code.length
                                    && ((code[pos] == '<' || code[pos] == '>') && code[pos + 1] == '=')
                                    || (code[i] == '=' && code[i + 1] == '=')
                                    || (code[i] == '!' && code[i + 1] == '='))
                            {
                                cadeia.append(code[pos + 1]);
                                pos++;
                                posLinha++;
                            }
                        } else
                        {
                            //consome de " até outro " ou \n(erro)
                            if (consomeString(code, cadeia))
                            {
                                //achou uma string
                            } else
                            {
                                //achou um erro
                            }
                            pos--;
                        }
                        //System.out.println(cadeia.toString());
                        addRepostaLexico(cadeia.toString());
                        cadeia.setLength(0);
                    }
                    posLinha++;
                }

            }

        }
        //atualiza index de Match
        int c = 0;
        for (Match l : lexemas_tokens_correspondidos)
        {
            l.setPosLista(c++);
        }

    }

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

    private void addRepostaLexico(String cadeia)
    {
        posLinha += cadeia.length();
        Lexema lex = new Lexema(cadeia.toString(), posParagrafo, posLinha);
        Object resposta = analise(lex);
        if (resposta instanceof Erro)
        {
            Erro e = (Erro) resposta;
            erros.add(e);
        } else if (resposta instanceof Match)
        {
            Match m = (Match) resposta;
            lexemas_tokens_correspondidos.add(m);
        }
    }

    private void addErro(Erro e)
    {
        erros.add(e);
    }

    private void addMatch(Match m)
    {
        lexemas_tokens_correspondidos.add(m);
    }
}
