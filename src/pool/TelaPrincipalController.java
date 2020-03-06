/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool;

import Classes.Controle.Erro;
import Classes.Controle.Match;
import Classes.Controle.Simbolo;
import Classes.Lexema;
import Classes.Token;
import Controladora.CtrCompilador;
import util.CodeAreaInit;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
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
    private TreeView<File> treeViewArquivos;
    @FXML
    private CheckBox chExibirTodos;
    @FXML
    private ListView<Object> lvErros_Avisos;
    @FXML
    private TableView<Object> tabela;
    @FXML
    private TableColumn<Object, String> colCadeia;
    @FXML
    private TableColumn<Object, Token> colToken;
    @FXML
    private TableColumn<Object, String> colValor;
    @FXML
    private TableColumn<Object, String> colTipo;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        CodeAreaInit.inicializa(caCodigo);
        colCadeia.setCellValueFactory(new PropertyValueFactory("cadeia"));
        colToken.setCellValueFactory(new PropertyValueFactory("token"));
        colTipo.setCellValueFactory(new PropertyValueFactory("tipo"));
        colValor.setCellValueFactory(new PropertyValueFactory("valor"));
        
        caCodigo.replaceText("main teste\n"
                + "{\n"
                + "    double x = 543.56;\n"
                + "    string s = \"skbdkslb slndbçs smpn\";\n"
                + "}");
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
            txTerminal.appendText("\n--------------------------------------\n" + command + LocalTime.now().toString() + "\n");
            while (true)
            {
                line = r.readLine();
                if (line == null)
                {
                    break;
                }
                txTerminal.appendText(line + "\n");
            }

        } catch (Exception ex)
        {
            txTerminal.appendText("[ERRO]: " + ex.getMessage());
        }
    }

    @FXML
    private void evtCompilar(MouseEvent event)
    {
        CtrCompilador.instancia().Analisar(caCodigo.getText());
        
        lvErros_Avisos.setItems(FXCollections.observableArrayList(
                CtrCompilador.instancia().getCompilador().getErros_avisos())); 
        
        tabela.setItems(FXCollections.observableArrayList(
                CtrCompilador.instancia().getCompilador().getTabela_Simbolos()));
        tabela.refresh();
        /*
        if (lvErros_Avisos.getItems().size() == 0)
            MyAlert.compilou("Compilou!!!").show();
        else
            MyAlert.erro("Não Compilou!!!").show();
        */
        /*
        for (Token token : Token.tokens)
        {
            System.out.println(token.getIdToken() + " - " + token.getRegex());
        }
                 */
    }

    @FXML
    private void evtPularParaLexema(MouseEvent event)
    {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() > 1)
        {
            Object o = tabela.getSelectionModel().getSelectedItem();
            Match m = ((Simbolo) o).getMatch();
            Lexema l = m.getLexema();
            caCodigo.requestFocus();
            caCodigo.position(l.getPosParagrafo(), l.getPosLinha());
            new Alert(Alert.AlertType.INFORMATION, l.getPalavra()+" EM "+l.getPosParagrafo()+" - "+l.getPosLinha(), ButtonType.OK).show();
        }
    }

    @FXML
    private void evtErroAviso(MouseEvent event)
    {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() > 1)
        {
            Object o = lvErros_Avisos.getSelectionModel().getSelectedItem();
            Erro e = (Erro) o;
            Lexema l = e.getLexema();
            caCodigo.requestFocus();
            caCodigo.position(l.getPosParagrafo(), l.getPosLinha());
            new Alert(Alert.AlertType.INFORMATION, l.getPalavra()+" EM "+l.getPosParagrafo()+" - "+l.getPosLinha(), ButtonType.OK).show();
        }
    }

}
