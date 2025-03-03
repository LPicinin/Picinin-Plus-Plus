package Classes.Controle;

/*
	Program to implement Three Address Code in Java
	Author: Surajit Karmakar		Author Link: https://www.facebook.com/surajit.3528
	www.pracspedia.com
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArvoreTresEnderecos
{

    private static final char[][] precedence =
    {
        {
            '/', '1'
        },
        {
            '*', '1'
        },
        {
            '+', '2'
        },
        {
            '-', '2'
        },
        {
            '&', '6'
        },
        {
            '|', '7'
        },
        {
            '>', '5'
        },
        {
            '<', '5'
        },
        {
            '•', '5'//maior igual
        },
        {
            '○', '5'//menor igual
        },
        {
            '=', '5'//igual
        },
        {
            '♦', '5'//diferente
        }
            
    };

    private static int precedenceOf(String t)
    {
        char token = t.charAt(0);
        for (int i = 0; i < precedence.length; i++)
        {
            if (token == precedence[i][0])
            {
                return Integer.parseInt(precedence[i][1] + "");
            }
        }
        return -1;
    }

    public List<String> build(String expr)
    {
        List<String> list = new ArrayList<>();
        int i, j, opc = 0;
        char token;
        boolean processed[], flag;
        String[][] operators = new String[10][2];
        String temp;
        processed = new boolean[expr.length()];
        Arrays.fill(processed, false);

        for (i = 0; i < expr.length(); i++)
        {
            token = expr.charAt(i);
            flag = true;
            for (j = 0; j < precedence.length && flag; j++)
            {
                if (token == precedence[j][0])
                {
                    operators[opc][0] = token + "";
                    operators[opc][1] = i + "";
                    opc++;
                    flag = false;
                }
            }
        }

        for (i = opc - 1; i >= 0; i--)
        {
            for (j = 0; j < i; j++)
            {
                if (precedenceOf(operators[j][0]) > precedenceOf(operators[j + 1][0]))
                {
                    temp = operators[j][0];
                    operators[j][0] = operators[j + 1][0];
                    operators[j + 1][0] = temp;
                    temp = operators[j][1];
                    operators[j][1] = operators[j + 1][1];
                    operators[j + 1][1] = temp;
                }
            }
        }

        for (i = 0; i < opc; i++)
        {
            j = Integer.parseInt(operators[i][1] + "");
            String op1 = "", op2 = "";
            if (processed[j - 1] == true)
            {
                if (precedenceOf(operators[i - 1][0]) == precedenceOf(operators[i][0]))
                {
                    op1 = "t" + i;
                } else
                {
                    for (int x = 0; x < opc; x++)
                    {
                        if ((j - 2) == Integer.parseInt(operators[x][1]))
                        {
                            op1 = "t" + (x + 1) + "";
                        }
                    }
                }
            } else
            {
                op1 = expr.charAt(j - 1) + "";
            }
            if (processed[j + 1] == true)
            {
                for (int x = 0; x < opc; x++)
                {
                    if ((j + 2) == Integer.parseInt(operators[x][1]))
                    {
                        op2 = "t" + (x + 1) + "";
                    }
                }
            } else
            {
                op2 = expr.charAt(j + 1) + "";
            }
            list.add("t" + (i + 1) + " = " + op1 + " " + operators[i][0] + " " + op2);
            processed[j] = processed[j - 1] = processed[j + 1] = true;
        }
        return list;
    }
}
