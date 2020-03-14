/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool;

import Classes.Controle.Erro;
import Classes.Token;
import Controladora.CtrCompilador;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import util.CodeAreaInit;
import java.io.BufferedReader;
import java.io.File;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javax.swing.Timer;
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
                + "    double x = 543.56;\n"
                + "    string s = \"skbdkslb slndbçs smpn\";\n"
                + "    while(x<s)\n"
                + "    {\n"
                + "    \t\n"
                + "    }\n"
                + "    if(x<s)\n"
                + "    {\n"
                + "    \t\n"
                + "    }\n"
                + "    else\n"
                + "    {\n"
                + "    \t\n"
                + "    }\n"
                + "    for(int i = x; x < s and s < x; x = x + 1)\n"
                + "    {\n"
                + "    \t\n"
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
        if (CtrCompilador.instancia().getCompilador().getErros_avisos().isEmpty())
        {
            css = "-fx-border-color:green; -fx-border-width: 3;";
        } else
            css = "-fx-border-color:red; -fx-border-width: 3;";

        pnCodigo.setStyle(css);

        Timer timer = new Timer(1000, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                pnCodigo.setStyle("");
            }
        });
        timer.setRepeats(false); // Only execute once
        timer.start();

        /*
        if (lvErros_Avisos.getItems().size() == 0)
            MyAlert.compilou("Compilou!!!").show();
        else
            MyAlert.erro("Não Compilou!!!").show();
         */
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
        /*
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() > 1)
        {
            Object o = lvErros_Avisos.getSelectionModel().getSelectedItem();
            Erro e = (Erro) o;
            Lexema l = e.getLexema();
            caCodigo.requestFocus();
            caCodigo.position(l.getPosParagrafo(), l.getPosLinha());
            new Alert(Alert.AlertType.INFORMATION, l.getPalavra()+" EM "+l.getPosParagrafo()+" - "+l.getPosLinha(), ButtonType.OK).show();
        }
         */
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
}
