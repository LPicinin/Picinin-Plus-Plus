/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool;

import util.CodeAreaInit;
import com.jfoenix.controls.JFXListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import org.fxmisc.richtext.CodeArea;

/**
 *
 * @author luish
 */
public class TelaPrincipalController implements Initializable
{

    @FXML
    private CodeArea caCodigo;
    @FXML
    private TextField txcomando;
    @FXML
    private TextArea txTerminal;
    @FXML
    private TreeView<?> treeViewArquivos;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        CodeAreaInit.inicializa(caCodigo);
    }

    @FXML
    private void evtExecutaComando(MouseEvent event)
    {
        try
        {
            String command = txcomando.getText();
            txcomando.clear();
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            txTerminal.appendText("\n--------------------------------------\n"+command+LocalTime.now().toString()+"\n");
            while (true)
            {
                line = r.readLine();
                if (line == null)
                {
                    break;
                }
                txTerminal.appendText(line + "\n");
            }
            /*Process proc = Runtime.getRuntime().exec(command);
            
            // Read the output
            
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(proc.getInputStream()));
            
            String line = "";
            txTerminal.appendText("\n--------------------------------------\n"+command+LocalTime.now().toString()+"\n");
            while((line = reader.readLine()) != null) {
                txTerminal.appendText(line + "\n");
            }
            proc.waitFor();
             */
        } catch (Exception ex)
        {
            txTerminal.appendText("[ERRO]: " + ex.getMessage());
        }
    }

}
