/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool;

import Classes.Controle.Erro;
import Classes.Lexema;
import Classes.Token;
import Controladora.CtrCompilador;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import util.CodeAreaInit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

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
    @FXML
    private VBox pnerros;
    @FXML
    private HBox pnCodigo;

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
                + "    double dec = 543.56;\n"
                + "    double cient = 3.45E5;\n"
                + "    string s = \"skbdkslb slndbçs smpn\";\n"
                + "    int in = 9;\n"
                + "    char c = 'A';\n"
                + "    // int main dnjdn 56 ; fef\n"
                + "    while(dec<s and 10 <= 90)\n"
                + "    {\n"
                + "    	\n"
                + "    }\n"
                + "    if(dec<s)\n"
                + "    {\n"
                + "    	\n"
                + "    }\n"
                + "    else\n"
                + "    {\n"
                + "    	\n"
                + "    }\n"
                + "    int i;\n"
                + "    for(int k = dec; dec < s and s < dec; k = k + 1)\n"
                + "    {\n"
                + "    	\n"
                + "    }\n"
                + "    for(i = dec; dec < s and s < dec; dec = dec + 1)\n"
                + "    {\n"
                + "    	\n"
                + "    }\n"
                + "}");

        //caCodigo.setStyleClass(5, 10, "erro");
        //caCodigo.setStyleClass(5, 10, "erro");
        //caCodigo.setStyleSpans(0, 5, computeHighlighting(Erro.getError(Erro.naoCompletado, new Lexema("sss", 0, 5))));
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
        processaErros();

        String css;
        Label l;
        if (CtrCompilador.instancia().getCompilador().getErros_avisos().isEmpty())
        {
            css = "-fx-border-color:green; -fx-border-width: 3;";
            l = new Label("Compilado com sucesso!!");
            l.setTextFill(Paint.valueOf("#00ff00"));

        } else
        {
            css = "-fx-border-color:red; -fx-border-width: 3;";
            l = new Label("Não pode ser compilado!!");
            l.setTextFill(Paint.valueOf("#ff0000"));
        }

        lvErros_Avisos.getItems().add(l);
        lvErros_Avisos.setStyle(css);

    }

    @FXML
    private void evtPularParaLexema(MouseEvent event)
    {
        /*
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() > 1)
        {
            Object o = tabela.getSelectionModel().getSelectedItem();
            if(o!= null)
            {
                Match m = ((Simbolo) o).getMatch();
                Lexema l = m.getLexema();
                //caCodigo.moveTo(l.getPosParagrafo(), l.getPosLinha());
                //new Alert(Alert.AlertType.INFORMATION, l.getPalavra() + " EM " + l.getPosParagrafo() + " - " + l.getPosLinha(), ButtonType.OK).show();
            }
        }
         */
    }

    @FXML
    private void evtErroAviso(MouseEvent event)
    {

        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() > 1)
        {
            Object o = lvErros_Avisos.getSelectionModel().getSelectedItem();
            Erro e = (Erro) o;
            Lexema l = e.getLexema();
            caCodigo.requestFocus();
            caCodigo.moveTo(l.getPosParagrafo(), 0);
        }

    }

    private static StyleSpans<Collection<String>> computeHighlighting(Erro text)
    {
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        String styleClass = "erro";

        spansBuilder.add(Collections.emptyList(), text.getLexema().getPosLinha() - text.getLexema().getPalavra().length());
        spansBuilder.add(Collections.singleton(styleClass), text.getLexema().getPosLinha() - text.getLexema().getPalavra().length());
        lastKwEnd = text.getLexema().getPosLinha() + text.getLexema().getPalavra().length();

        spansBuilder.add(Collections.emptyList(), text.getLexema().getPosLinha() - text.getLexema().getPalavra().length());
        return spansBuilder.create();
    }

    private void processaErros()
    {
        List<Object> err = CtrCompilador.instancia().getCompilador().getErros_avisos();
        pnerros.getChildren().clear();
        for (Object interator : err)
        {
            if (interator instanceof Erro)
            {
                Erro erro = (Erro) interator;
                while (pnerros.getChildren().size() <= erro.getLexema().getPosParagrafo())
                {
                    Text icon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.CLOSE);
                    icon.setFill(Paint.valueOf("#ff0000"));
                    icon.setVisible(false);
                    Tooltip.install(icon, new Tooltip(erro.getMensagem()));
                    pnerros.getChildren().add(icon);
                }
                pnerros.getChildren().get(erro.getLexema().getPosParagrafo()).setVisible(true);
            }
        }
    }

    @FXML
    private void evtabrirArquivo(MouseEvent event)
    {
        FileChooser fc = new FileChooser();
        fc.setTitle("Abrir Código!!!");
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Formatos Suportados", ".ppp"));
        File arq = fc.showOpenDialog(null);
        if (arq != null)
        {
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(arq));
                String line = br.readLine();
                String code = "";
                while (line != null)
                {
                    code += line + "\n";
                    line = br.readLine();
                }
                caCodigo.replaceText(code);

            } catch (IOException ex)
            {
                System.out.println("Erro ao abrir arquivo: " + ex.getMessage());
            }

        }
    }

    @FXML
    private void evtSaveCode(MouseEvent event)
    {
        FileChooser fc = new FileChooser();
        File arq;
        fc.setInitialDirectory(new File(".."));
        arq = fc.showSaveDialog(null);

        try
        {
            FileWriter writer = new FileWriter(arq); //new FileWriter("MyFile.txt", true);
            writer.write(caCodigo.getText());
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
