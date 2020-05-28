/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool;

import Classes.Controle.Aviso;
import Classes.Controle.Controle;
import Classes.Controle.Erro;
import Classes.Lexema;
import Classes.Semantico;
import Classes.Token;
import Controladora.CtrCompilador;
import util.CodeAreaInit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
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
    private ListView<Object> lvCodigoIntermediario;

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
                + "    double cient = 3.45E-5;\n"
                + "    double dec = 543.56;\n"
                + "    string s = \"skbdkslb slndbçs smpn\";\n"
                + "    int in = 9;\n"
                + "    char c = 'A';\n"
                + "    // int main dnjdn 56 ; fef\n"
                + "    while(dec<in and 10 <= 90)\n"
                + "    {\n"
                + "    	\n"
                + "    }\n"
                + "    if(dec>=in)\n"
                + "    {\n"
                + "    	\n"
                + "    }\n"
                + "    int i;\n"
                + "    for(int k = dec; dec < 45 and in < dec; k = k + 1)\n"
                + "    {\n"
                + "    	\n"
                + "    }\n"
                + "    for(i = dec; dec < 874.4 and 64 < dec; dec = dec + 1)\n"
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
        ObservableList<Object> oa = FXCollections.observableArrayList(trataErrosDesnecessarios(CtrCompilador.instancia().getCompilador().getErros_avisos()));
        Set<Object> set = new HashSet<>(oa);
        oa.clear();
        oa.addAll(set);

        lvErros_Avisos.setItems(oa);

        tabela.setItems(FXCollections.observableArrayList(
                CtrCompilador.instancia().getCompilador().getTabela_Simbolos()));

        tabela.refresh();
        processaErros();

        String css;
        Label l;
        int qtd_erros = countErros(CtrCompilador.instancia().getCompilador().getErros_avisos());

        if (CtrCompilador.instancia().getCompilador().getErros_avisos().isEmpty())
        {
            css = "-fx-border-color:green; -fx-border-width: 3;";
            l = new Label("Compilado com sucesso!!");
            l.setTextFill(Paint.valueOf("#00ff00"));

        } else if (qtd_erros > 0)
        {
            css = "-fx-border-color:red; -fx-border-width: 3;";
            l = new Label("Não pode ser compilado!!");
            l.setTextFill(Paint.valueOf("#ff0000"));
        } else
        {
            css = "-fx-border-color:orange; -fx-border-width: 3;";
            l = new Label("Compilado com avisos!!");
            l.setTextFill(Paint.valueOf("#ffa500"));
        }

        lvErros_Avisos.getItems().add(l);
        lvErros_Avisos.setStyle(css);

        lvCodigoIntermediario.getItems().clear();
        lvCodigoIntermediario.setItems(FXCollections.observableArrayList(new ArrayList<>(Semantico.getLci())));

    }

    @FXML
    private void evtPularParaLexema(MouseEvent event)
    {

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

    private void processaErros()
    {
        List<Object> err = CtrCompilador.instancia().getCompilador().getErros_avisos();

        int s = caCodigo.getText().split("\n").length;
        for (int i = 0; i < s; i++)
        {
            caCodigo.clearParagraphStyle(i);
        }
        for (Object interator : err)
        {
            if (interator instanceof Erro)
            {
                Erro erro = (Erro) interator;
                if (erro != null)
                    caCodigo.setParagraphStyle(erro.getLexema().getPosParagrafo(), Collections.singletonList("erro"));

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
        }
    }

    @FXML
    private void evtLimpar(MouseEvent event)
    {
        caCodigo.clear();
        lvErros_Avisos.getItems().clear();
        tabela.getItems().clear();
        String css = "-fx-border-color:transparent; -fx-border-width: 3;";
        lvErros_Avisos.setStyle(css);
    }

    private List<Object> trataErrosDesnecessarios(List<Object> erros_avisos)
    {
        List<Object> mremocao = new ArrayList<>();
        for (Object erros_aviso : erros_avisos)
        {
            if (erros_aviso == null || (((Controle) erros_aviso).getLexema() == null))
            {
                mremocao.add(erros_aviso);
            }
        }
        erros_avisos.removeAll(mremocao);
        erros_avisos = Arrays.stream(erros_avisos.toArray()).distinct().collect(Collectors.toList());
        return erros_avisos;
    }

    private int countErros(List<Object> ea)
    {
        int c = 0;
        for (Object item : ea)
        {
            if (item instanceof Erro)
                c++;
        }
        return c;
    }

    private int countAvisos(List<Object> ea)
    {
        int c = 0;
        for (Object item : ea)
        {
            if (item instanceof Aviso)
                c++;
        }
        return c;
    }
}
