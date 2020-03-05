/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Aluno
 */
public class MyAlert
{

    private Stage stage;
    private String url_Imagem;

    public MyAlert(Stage stage, String url_Imagem)
    {
        this.stage = stage;
        this.url_Imagem = url_Imagem;
    }

    public static Stage erro(String msg)
    {
        return montaAlert(msg, "/img/ERRO.jpg");
    }

    public static Stage compilou(String msg)
    {
        return montaAlert(msg, "/img/compilou.jpg");
    }

    private static Stage montaAlert(String msg, String urlImagem)
    {
        ImageView img;
        img = new ImageView(urlImagem);
        Label lbl = new Label(msg);
        lbl.setFont(Font.font("Arial Bold", 25));
        VBox vb = new VBox(lbl, img);
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(25);
        VBox.setMargin(lbl, new Insets(15, 0, 0, 0));
        Scene s = new Scene(vb);
        Stage st = new Stage();
        st.setScene(s);
        st.centerOnScreen();
        st.initStyle(StageStyle.UTILITY);
        return st;
    }
}
