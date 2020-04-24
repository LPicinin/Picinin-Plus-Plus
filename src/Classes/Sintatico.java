/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Classes.Controle.Controle;
import Classes.Controle.Erro;
import Classes.Controle.Match;
import Classes.Controle.Simbolo;
import Classes.Controle.TipoAnalise;
import static Classes.Token.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author luish
 */
public class Sintatico extends Constantes
{

    private Stack<Match> pilha_entrada;
    //private Stack<Token> pilha;
    private Queue<Match> fila_sugestoes;
    //private List<Match> listaTokens;

    private Lexico al_lexica;
    //private Stack<Match> pilha_simbolos;
    private int posToken;//para consumir os tokens extraidos pela analise lexica

    public Sintatico(String codeString)
    {
        //pilha_simbolos = new Stack<>();
        lexemas_tokens_correspondidos = new ArrayList<>();
        erros = new ArrayList<>();
        code = codeString.toCharArray();
    }

    public Sintatico()
    {
        lexemas_tokens_correspondidos = new ArrayList<>();
        erros = new ArrayList<>();
    }

    public List<Match> getLexemas_tokens_correspondidos()
    {
        return lexemas_tokens_correspondidos;
    }

    public void setLexemas_tokens_correspondidos(List<Match> lexemas_tokens_correspondidos)
    {
        Sintatico.lexemas_tokens_correspondidos = lexemas_tokens_correspondidos;
    }

    public List<Erro> getErros()
    {
        return erros;
    }

    public void setErros(List<Erro> erros)
    {
        Sintatico.erros = erros;
    }

    public List<Simbolo> analise()
    {

        al_lexica = new Lexico();
        al_lexica.analise();
        try
        {
            geraAnaliseSintatica();
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage() + "\n" + ex.getCause());
        }

        return geraTabelaSimbolos();
    }

    private List<Simbolo> geraTabelaSimbolos()
    {
        List<Simbolo> tabela_simbolos = new ArrayList<>();
        List<Match> lt = lexemas_tokens_correspondidos;
        Match aux;

        for (int i = 0; i < lt.size(); i++)
        {
            aux = lt.get(i);

            if (!aux.getToken().equals(tIdentificador))
            {
                addSimbolo(tabela_simbolos, new Simbolo(aux, "", ""));
            } else
            {
                String val = "";
                String tipo = "";
                if (i == 0 && !lt.get(0).getToken().equals(Token.tInicio_Linguagem))
                {

                } else if (i - 1 >= 0 && lt.get(i - 1).getToken().equals(tInicio_Linguagem))
                {
                    tipo = lt.get(i - 1).getToken().getIdToken().replace("t", "");
                } else
                {
                    //o +2 é pra pular atribuição
                    if (i + 2 < lt.size() && tValores.contains(lt.get(i + 2).getToken()) && tIgual.equals(lt.get(i + 1).getToken()))
                    {
                        val = lt.get(i + 2).getLexema().getPalavra();
                    } else if (i + 3 < lt.size()
                            && tValores.contains(lt.get(i + 3).getToken())
                            && tIgual.equals(lt.get(i + 1).getToken())
                            && (tOper_menos.equals(lt.get(i + 2).getToken()) || tOper_soma.equals(lt.get(i + 2).getToken())))
                    {
                        val = lt.get(i + 2).getLexema().getPalavra() + lt.get(i + 3).getLexema().getPalavra();
                    }

                    if (tTipos.contains(lt.get(i - 1).getToken()))
                    {
                        tipo = lt.get(i - 1).getToken().getIdToken().replace("t", "");
                    }
                }
                addSimbolo(tabela_simbolos, new Simbolo(aux, val, tipo));
            }
        }
        return tabela_simbolos;
    }

    private void addSimbolo(List<Simbolo> tabela_simbolos, Simbolo simbolo)
    {
        Simbolo aux;
        if (simbolo.getToken().equals(tIdentificador))
        {
            int index = tabela_simbolos.indexOf(simbolo);
            if (index != -1)
            {
                aux = tabela_simbolos.get(index);
                if (simbolo.getTipo().isEmpty())
                {
                    tabela_simbolos.add(simbolo);
                } else
                {
                    erros.add(Erro.getError(Erro.variavel_Ja_Declarada, simbolo.getMatch().getLexema()));
                }
            } else if (simbolo.getTipo().isEmpty())
            {
                erros.add(Erro.getError(Erro.variavelNaoDeclarada, simbolo.getMatch().getLexema()));
            } else
            {
                tabela_simbolos.add(simbolo);
            }
        } else
        {
            tabela_simbolos.add(simbolo);
        }
    }

    private Stack<Match> geraPilhaEntrada()
    {
        pilha_entrada = new Stack<>();
        //pilha_simbolos.add(new Match(new Lexema("§", 0, 0), Token.tVazio));
        List<Match> l = lexemas_tokens_correspondidos;
        for (int i = l.size() - 1; i >= 0; i--)
        {
            pilha_entrada.push(l.get(i));
        }
        return pilha_entrada;
    }

    //Sem tabela
    private void geraAnaliseSintatica()
    {
        pilha_entrada = geraPilhaEntrada();
        //pilha = new Stack<>();
        //System.out.println(pilha_entrada.peek()+" - "+pilha_entrada.pop());
        fila_sugestoes = new LinkedList<>();
        int max = lexemas_tokens_correspondidos.size();
        posToken = 0;
        Controle c = analisar(TipoAnalise.a_inicioPrograma);
        if (c != null)
        {
            erros.add(Erro.getError(Erro.Inicio_da_Linguagem, c.getLexema()));
        }
        analisar(TipoAnalise.a_chaves);
        analisar(TipoAnalise.ca_codigoForaDoEscopo);
        //analisar(TipoAnalise.ca_else);
        if (lexemas_tokens_correspondidos.size() == 0)
        {
            erros.add(Erro.tokenNaoEncontrado);
        }
        boolean flag;
        while (!pilha_entrada.isEmpty())
        {
            flag = true;
            if (Token.tOperadores.contains(pilha_entrada.peek().getToken()))
            {
                erros.add(Erro.getError(Erro.expressaoIlegal, pilha_entrada.peek().getLexema()));
            } else if (Token.tTipos.contains(pilha_entrada.peek().getToken()))
            {
                flag = false;
                Controle retorno = analisar(TipoAnalise.ca_declaracao);
                if (retorno != null && retorno instanceof Erro)
                {
                    erros.add((Erro) retorno);
                }
                //break;
            } else
            {
                for (TipoAnalise ta : TipoAnalise.listaAnalises)
                {
                    if (ta.getFirst().equals(pilha_entrada.peek().getToken()))
                    {
                        flag = false;
                        Controle retorno = analisar(ta.getCodigo());
                        if (retorno != null && retorno instanceof Erro)
                        {
                            erros.add((Erro) retorno);
                        }
                        break;
                    }
                }
            }
            if (flag)
            {
                buscaTokenDeConexao();
            }
        }
        //e aqui o show começa
    }

    private Controle analisar(int tipoAnalise)
    {
        Controle c = null;
        switch (tipoAnalise)
        {
            case TipoAnalise.a_inicioPrograma:
                c = al_inicioPrograma();
                break;
            case TipoAnalise.ca_declaracao:
                c = al_declaracao();
                break;
            case TipoAnalise.ca_atribuicao:
                c = al_atribuicao();
                break;
            case TipoAnalise.a_chaves:
                c = al_chaves();
                break;
            case TipoAnalise.ca_for:
                c = al_for();
                break;
            case TipoAnalise.ca_while:
                c = al_while();
                break;
            case TipoAnalise.a_parenteses:
                c = al_parenteses();
                break;
            case TipoAnalise.ca_naoReconhecido:
                c = al_Token_naoReconhecido();
                break;
            case TipoAnalise.ca_If:
                c = al_if();
                break;
            case TipoAnalise.ca_else:
                c = al_else();
                break;
            case TipoAnalise.ca_chaveAbre:
                pilha_entrada.pop();
                break;
            case TipoAnalise.ca_chaveFecha:
                pilha_entrada.pop();
                break;
            case TipoAnalise.ca_codigoForaDoEscopo:
                al_codigo_fora_do_escopo();
                break;
        }
        return c;
    }

    private Controle al_inicioPrograma()
    {
        if (pilha_entrada.size() - 2 < 0)
        {
            return regraNaoCompletada();
        }

        if (pilha_entrada.peek().getToken().equals(Token.tInicio_Linguagem))
        {
            pilha_entrada.pop();
            if (pilha_entrada.peek().getToken().equals(Token.tIdentificador))
            {
                pilha_entrada.pop();
                if (pilha_entrada.peek().getToken().equals(Token.tChave_abre))
                {
                    //retira chave da pilha
                    pilha_entrada.pop();
                    return null;
                } else
                {

                }
            } else
            {

            }
        }
        while (!pilha_entrada.isEmpty() && !pilha_entrada.pop().getToken().equals(Token.tChave_abre))
        {
        }
        return Erro.getError(Erro.Inicio_da_Linguagem, pilha_entrada.peek().getLexema());
    }

    private Controle al_Token_naoReconhecido()
    {
        posToken++;
        Erro error = Erro.getError(Erro.tokenNaoEncontrado, pilha_entrada.pop().getLexema());
        buscaTokenDeConexao();
        return error;
    }

    private void buscaTokenDeConexao()
    {
        while (!pilha_entrada.isEmpty() && !token_De_Conexao.contains(pilha_entrada.peek().getToken()))//talvez retirar a negação
        {
            posToken++;
            pilha_entrada.pop();
        }
        if (!pilha_entrada.isEmpty())
        {
            pilha_entrada.pop();
        }
    }

    private Controle al_declaracao()
    {

        if (pilha_entrada.size() - 3 < 0)
        {
            return regraNaoCompletada();
        }
        {
            Match[] t = new Match[5];
            t[0] = pilha_entrada.pop();
            t[1] = pilha_entrada.pop();
            t[2] = pilha_entrada.pop();

            if (Token.tTipos.contains(t[0].getToken())
                    && t[1].getToken().equals(Token.tIdentificador)
                    && t[2].getToken().equals(Token.tPontoVirgula))
            {
                return null;
            } else if (pilha_entrada.size() - 2 > 0 && t[2].getToken().equals(Token.tIgual))
            {
                pilha_entrada.push(t[2]);
                pilha_entrada.push(t[1]);
                return al_atribuicao();
            }
            return regraNaoCompletada();
        }
    }

    private Controle al_atribuicao()
    {
        boolean erro = true;
        ArrayList<Match> r = new ArrayList<>();
        Match aux;
        if (pilha_entrada.size() >= 4)
        {
            r.add(pilha_entrada.pop());
            r.add(pilha_entrada.pop());
            //consome sinal
            while (!pilha_entrada.isEmpty() && pilha_entrada.peek().getToken().equals(Token.tOper_menos) || pilha_entrada.peek().getToken().equals(Token.tOper_soma))
            {
                aux = pilha_entrada.pop();
            }
            r.add(pilha_entrada.pop());
            if (r.get(0).getToken().equals(Token.tIdentificador) && r.get(1).getToken().equals(Token.tIgual))
            {
                if (Token.tValores.contains(r.get(2).getToken()) || r.get(2).getToken().equals(Token.tIdentificador))//pode ser Muita Coisa
                {
                    //atribuicao Simples
                    if (pilha_entrada.peek().getToken().equals(Token.tPontoVirgula))
                    {
                        pilha_entrada.pop();//retira o ;
                        erro = false;
                    } else//possivel operacao - tem que resolver
                    {
                        if (!pilha_entrada.isEmpty()
                                && (pilha_entrada.peek().getToken().equals(Token.tParenteses_abre)
                                || Token.tOperadores.contains(pilha_entrada.peek().getToken())))
                        {
                            boolean OperacaoValida = true;
                            boolean entrouNoFor = false;
                            for (aux = pilha_entrada.pop(); !pilha_entrada.isEmpty() && !pilha_entrada.peek().getToken().equals(Token.tPontoVirgula) && OperacaoValida; aux = pilha_entrada.pop())
                            {
                                entrouNoFor = true;
                                //se é ((id ou valor) e operador) ou (operador e abre parentese) ou (operador e (id ou valor))
                                if ((((aux.getToken().equals(Token.tIdentificador) || Token.tValores.contains(aux.getToken())))
                                        && Token.tOperadores.contains(pilha_entrada.peek().getToken()))
                                        || ((Token.tOperadores.contains(aux.getToken())
                                        && pilha_entrada.peek().getToken().equals(Token.tParenteses_abre)))
                                        || (Token.tOperadores.contains(aux.getToken()))
                                        && (pilha_entrada.peek().getToken().equals(Token.tIdentificador) || Token.tValores.contains(pilha_entrada.peek().getToken())))
                                {
                                    OperacaoValida = true;
                                } else
                                {
                                    OperacaoValida = false;
                                    erro = true;
                                }
                            }
                            if (OperacaoValida && entrouNoFor)
                            {
                                if (pilha_entrada.peek().getToken().equals(Token.tPontoVirgula))
                                {
                                    pilha_entrada.pop();
                                }
                                return null;
                            }
                            return Erro.getError(Erro.expressaoIlegal, aux.getLexema());
                        }
                    }
                } else
                {
                    erro = true;
                }
            } else
            {
                erro = true;
            }
        } else
        {
            erro = true;
        }

        if (erro)
        {
            return Erro.getError(Erro.tokenFinalDeCadeiaInesperada, pilha_entrada.peek().getLexema());
        } else
        {
            return null;
        }
    }

    private Controle al_for()
    {
        Controle retorno = null;
        Match m = pilha_entrada.pop();
        if (m.getToken().equals(Token.tFor))
        {
            m = pilha_entrada.pop();
            if (m.getToken().equals(Token.tParenteses_abre) && !pilha_entrada.isEmpty())
            {
                //parte da declaracao
                if (!pilha_entrada.peek().getToken().equals(Token.tPontoVirgula))
                {
                    if (Token.tTipos.contains(pilha_entrada.peek().getToken()))
                    {
                        retorno = al_declaracao();
                    } else if (pilha_entrada.peek().getToken().equals(Token.tIdentificador))
                    {
                        retorno = al_atribuicao();
                    }
                    if (retorno instanceof Erro)
                    {
                        return retorno;
                    }
                } else
                {
                    pilha_entrada.pop();
                }

                if (!pilha_entrada.isEmpty())
                {
                    //parteDaExpressao
                    retorno = al_expressao();
                } else
                {
                    return regraNaoCompletada();
                }

                if (retorno instanceof Erro)
                {
                    return retorno;
                }
                if (!pilha_entrada.isEmpty())
                {
                    //parte da atribuicao
                    retorno = al_atribuicao2();
                } else
                {
                    return regraNaoCompletada();
                }
            }
        }
        if (pilha_entrada.peek().getToken().equals(Token.tParenteses_fecha))
        {
            pilha_entrada.pop();
        }
        return null;
    }

    private Controle al_expressao()
    {
        Match aux;

        boolean OperacaoValida = true;
        for (aux = pilha_entrada.pop(); !pilha_entrada.isEmpty() && !pilha_entrada.peek().getToken().equals(Token.tPontoVirgula) && OperacaoValida; aux = pilha_entrada.pop())
        {
            //se encontrar parentese fechado apenas consome o seu token
            if (aux.getToken().equals(Token.tParenteses_fecha))
            {
                OperacaoValida = true;
            } //se é ((id ou valor) e operador) ou (operador e abre parentese) ou (operador e (id ou valor))
            else if ((((aux.getToken().equals(Token.tIdentificador) || Token.tValores.contains(aux.getToken())))
                    && Token.tOperadores.contains(pilha_entrada.peek().getToken()))
                    || ((Token.tOperadores.contains(aux.getToken())
                    && pilha_entrada.peek().getToken().equals(Token.tParenteses_abre)))
                    || (Token.tOperadores.contains(aux.getToken()))
                    && (pilha_entrada.peek().getToken().equals(Token.tIdentificador) || Token.tValores.contains(pilha_entrada.peek().getToken())))
            {
                OperacaoValida = true;
            } else
            {
                OperacaoValida = false;
            }
        }
        if (OperacaoValida)
        {
            if (pilha_entrada.peek().getToken().equals(Token.tPontoVirgula))
            {
                pilha_entrada.pop();
            }
            return null;
        }
        return Erro.getError(Erro.expressaoIlegal, aux.getLexema());
    }

    private Controle al_expressao2()
    {
        Match aux;

        boolean OperacaoValida = true;
        for (aux = pilha_entrada.pop(); !pilha_entrada.isEmpty() && !pilha_entrada.peek().getToken().equals(Token.tParenteses_fecha) && OperacaoValida; aux = pilha_entrada.pop())
        {
            //se encontrar parentese fechado apenas consome o seu token
            if (aux.getToken().equals(Token.tParenteses_fecha))
            {
                OperacaoValida = true;
            } //se é ((id ou valor) e operador) ou (operador e abre parentese) ou (operador e (id ou valor))
            else if ((((aux.getToken().equals(Token.tIdentificador) || Token.tValores.contains(aux.getToken())))
                    && Token.tOperadores.contains(pilha_entrada.peek().getToken()))
                    || ((Token.tOperadores.contains(aux.getToken())
                    && pilha_entrada.peek().getToken().equals(Token.tParenteses_abre)))
                    || (Token.tOperadores.contains(aux.getToken()))
                    && (pilha_entrada.peek().getToken().equals(Token.tIdentificador) || Token.tValores.contains(pilha_entrada.peek().getToken())))
            {
                OperacaoValida = true;
            } else
            {
                OperacaoValida = false;
            }
        }
        if (OperacaoValida)
        {
            if (pilha_entrada.peek().getToken().equals(Token.tParenteses_fecha))
            {
                pilha_entrada.pop();
            }
            return null;
        }
        return Erro.getError(Erro.expressaoIlegal, aux.getLexema());
    }

    private Controle al_atribuicao2()
    {
        boolean erro = true;
        ArrayList<Match> r = new ArrayList<>();
        Match aux;
        if (pilha_entrada.size() >= 4)
        {
            r.add(pilha_entrada.pop());
            r.add(pilha_entrada.pop());
            r.add(pilha_entrada.pop());
            if (r.get(0).getToken().equals(Token.tIdentificador) && r.get(1).getToken().equals(Token.tIgual))
            {
                if (Token.tValores.contains(r.get(2).getToken()) || r.get(2).getToken().equals(Token.tIdentificador))//pode ser Muita Coisa
                {
                    //atribuicao Simples
                    if (pilha_entrada.peek().getToken().equals(Token.tParenteses_fecha))
                    {
                        pilha_entrada.pop();//retira o ;
                        erro = false;
                    } else//possivel operacao - tem que resolver
                    {
                        if (!pilha_entrada.isEmpty()
                                && (pilha_entrada.peek().getToken().equals(Token.tParenteses_abre)
                                || Token.tOperadores.contains(pilha_entrada.peek().getToken())))
                        {
                            boolean OperacaoValida = true;
                            for (aux = pilha_entrada.pop(); !pilha_entrada.isEmpty() && !pilha_entrada.peek().getToken().equals(Token.tParenteses_fecha) && OperacaoValida; aux = pilha_entrada.pop())
                            {
                                //se é ((id ou valor) e operador) ou (operador e abre parentese) ou (operador e (id ou valor))
                                if ((((aux.getToken().equals(Token.tIdentificador) || Token.tValores.contains(aux.getToken())))
                                        && Token.tOperadores.contains(pilha_entrada.peek().getToken()))
                                        || ((Token.tOperadores.contains(aux.getToken())
                                        && pilha_entrada.peek().getToken().equals(Token.tParenteses_abre)))
                                        || (Token.tOperadores.contains(aux.getToken()))
                                        && (pilha_entrada.peek().getToken().equals(Token.tIdentificador) || Token.tValores.contains(pilha_entrada.peek().getToken())))
                                {
                                    OperacaoValida = true;
                                } else
                                {
                                    OperacaoValida = false;
                                    erro = true;
                                }
                            }
                            if (OperacaoValida)
                            {
                                if (pilha_entrada.peek().getToken().equals(Token.tPontoVirgula))
                                {
                                    pilha_entrada.pop();
                                }
                                return null;
                            }
                            return Erro.getError(Erro.expressaoIlegal, aux.getLexema());
                        }
                    }
                } else
                {
                    erro = true;
                }
            } else
            {
                erro = true;
            }
        } else
        {
            erro = true;
        }

        if (erro)
        {
            return Erro.getError(Erro.tokenFinalDeCadeiaInesperada, pilha_entrada.peek().getLexema());
        } else
        {
            return null;
        }
    }

    private Controle al_while()
    {
        Controle res = null;
        Match aux = pilha_entrada.pop();
        if (aux.getToken().equals(Token.tWhile) && pilha_entrada.pop().getToken().equals(Token.tParenteses_abre))
        {
            //res = al_expressao_boolean();
            res = al_expressao2();
            if (res == null && pilha_entrada.peek().getToken().equals(Token.tParenteses_fecha))
            {
                //tira ) da pilha
                pilha_entrada.pop();
            }
        } else
        {
            return regraNaoCompletada(aux);
        }

        return res;
    }

    private Controle al_expressao_boolean()
    {
        Controle c = null;
        Match aux = pilha_entrada.pop();
        if (Token.tValor_Bool.equals(aux.getToken()) && pilha_entrada.peek().getToken().equals(Token.tParenteses_fecha))
        {
            return null;
        } else
        {
            while (!pilha_entrada.isEmpty()
                    && !pilha_entrada.peek().getToken().equals(Token.tParenteses_fecha)
                    && pilha_entrada.size() - 3 >= 0)
            {
                //se valor(true ou false) e (operadorLogico) e valor(true ou false)
                if (!((aux.getToken().equals(Token.tValor_Bool)
                        || aux.getToken().equals(Token.tIdentificador))
                        && (Token.tOpLogicos.contains((aux = pilha_entrada.pop()).getToken())
                        || Token.tOpRelacional.contains(aux.getToken()))
                        && (pilha_entrada.peek().getToken().equals(Token.tValor_Bool)
                        || pilha_entrada.peek().getToken().equals(Token.tIdentificador))))
                {
                    c = Erro.getError(Erro.expressaoIlegal, pilha_entrada.peek().getLexema());
                    break;
                } else
                {
                    aux = pilha_entrada.pop();
                }

                //aux = pilha_entrada.pop();
            }
        }
        return c;
    }

    private Controle al_if()
    {
        Controle res = null;
        Match aux = pilha_entrada.pop();
        if (aux.getToken().equals(Token.tIf) && pilha_entrada.pop().getToken().equals(Token.tParenteses_abre))
        {
            //res = al_expressao_boolean();
            res = al_expressao2();
            if (res == null && pilha_entrada.peek().getToken().equals(Token.tParenteses_fecha))
            {
                //tira ) da pilha
                pilha_entrada.pop();
            }
        } else
        {
            return regraNaoCompletada(aux);
        }

        return res;
    }

    private Controle regraNaoCompletada(Match aux)
    {
        Erro error = Erro.getError(Erro.naoCompletado, aux.getLexema());
        buscaTokenDeConexao();
        posToken++;
        return error;
    }

    private Controle regraNaoCompletada()
    {
        Erro error = Erro.getError(Erro.naoCompletado, (pilha_entrada.isEmpty() ? new Lexema("Vazio", 0, 0) : pilha_entrada.pop().getLexema()));
        buscaTokenDeConexao();
        posToken++;
        return error;
    }

    private Controle al_chaves()
    {
        Controle res = null;
        Stack<Match> abre = new Stack<>();
        List<Match> l = lexemas_tokens_correspondidos;
        for (int i = 0; i < l.size(); i++)
        {
            if (l.get(i).getToken().equals(Token.tChave_abre))
            {
                if (abre.isEmpty() && i > 2)
                {
                    erros.add(new Erro(666, "Escopo fora do fluxo de execução detectado!!!", l.get(i).getLexema()));
                }
                abre.push(l.get(i));
            } else if (!abre.isEmpty() && l.get(i).getToken().equals(Token.tChave_fecha))
            {
                abre.pop();
            } else if (abre.isEmpty() && l.get(i).getToken().equals(Token.tChave_fecha))
            {
                erros.add(Erro.getError(Erro.chavesFaltantes, l.get(i).getLexema()));
            }
        }
        for (Match ca : abre)
        {
            erros.add(Erro.getError(Erro.chavesFaltantes, ca.getLexema()));
        }

        return res;
    }

    private Controle al_parenteses()
    {
        Controle res = null;
        Stack<Match> abre = new Stack<>();
        Stack<Match> fecha = new Stack<>();
        for (Match l : lexemas_tokens_correspondidos)
        {
            if (l.getToken().equals(Token.tParenteses_abre))
            {
                abre.push(l);
            } else if (l.getToken().equals(Token.tParenteses_fecha))
            {
                fecha.push(l);
            }
        }
        if (abre.size() == fecha.size())
        {
            return null;
        }
        for (int i = 0; i < abre.size() && !fecha.isEmpty(); i++)
        {
            abre.pop();
            fecha.pop();
        }

        if (fecha.isEmpty())
        {
            res = Erro.getError(Erro.parenteseFaltante, abre.peek().getLexema());
        } else
        {
            res = Erro.getError(Erro.parenteseFaltante, fecha.peek().getLexema());
        }

        for (Match cf : fecha)
        {
            erros.add(Erro.getError(Erro.parenteseFaltante, cf.getLexema()));
        }
        for (Match ca : abre)
        {
            erros.add(Erro.getError(Erro.parenteseFaltante, ca.getLexema()));
        }

        return res;
    }

    private Controle al_else()
    {
        int index;
        Match m = pilha_entrada.pop();
        Controle res = null;
        Stack<Match> fecha = new Stack<>();

        if (m.getToken().equals(Token.tElse) && !pilha_entrada.isEmpty()
                && lexemas_tokens_correspondidos.get(m.getPosLista() - 1).getToken().equals(Token.tChave_fecha))
        {
            index = m.getPosLista();
            m = pilha_entrada.pop();
            if (m.getToken().equals(Token.tChave_abre) && verificaIf_antes_else(index))
            {
                return null;
            }
        }
        return new Erro(Erro.expressaoIlegal.getCodigo(), "palavra 'if' era esperado antes do 'else'", m.getLexema());
    }

    private boolean verificaIf_antes_else(int index)
    {
        boolean flag = true;
        Stack<Match> fecha = new Stack<>();
        List<Match> listaLexTok = lexemas_tokens_correspondidos;
        for (int i = index - 1; i > 0 && flag; i--)
        {
            if (fecha.isEmpty() && listaLexTok.get(i).getToken().equals(Token.tIf))
            {
                flag = false;
            } else if (listaLexTok.get(i).getToken().equals(Token.tChave_fecha))
            {
                fecha.push(listaLexTok.get(i));
            } else if (!fecha.isEmpty() && listaLexTok.get(i).getToken().equals(Token.tChave_abre))
            {
                fecha.pop();
            } else if (listaLexTok.get(i).getToken().equals(Token.tElse))
            {
                i = 0;
            }
            /*
            else if(fecha.isEmpty() && listaLexTok.get(i).getToken().equals(Token.tIf))
            {
                flag = false;
            }*/
        }
        return !flag;
    }

    private void al_codigo_fora_do_escopo()
    {
        int i = 0;
        Integer lin_erro = null;

        List<Match> l = lexemas_tokens_correspondidos;
        for (i = l.size() - 1; i > 0 && !l.get(i).getToken().equals(Token.tChave_fecha); i--)
        {
            if (lin_erro == null || lin_erro > l.get(i).getLexema().getPosParagrafo())
            {
                lin_erro = l.get(i).getLexema().getPosParagrafo();
                erros.add(Erro.getError(Erro.codigo_Escopo_fora_do_fluxo, l.get(i).getLexema()));
            }
        }
    }
}
