/**Classe onde se encontra a interface principal dos Proprietarios, apartir daqui estes tem a opçao de escolher entre verificar as reservas
 * feitas em sua casa e ver o relatorio financeiro.

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */
package Proprietario;

import Ficheiros.DatabaseHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projeto_poo.Main;


public class Proprietário extends Application {
    private Stage stage;
    DatabaseHandler databaseHandler;
    String kak;
    String lop;
    int lot;
    
    /** Constructor para a classe Proprietario.
     * @param lot int - nº do lote
     * @param kak String - nome do Proprietario
     * @param lop String - nome retirado da base dados

        */
    public Proprietário(int lot,String kak, String lop) {
        this.kak =  kak;
        this.lop =  lop;
        this.lot =  lot;
    }
    
    public static void main(String[] args) {
        Locale.setDefault(Locale.UK);                  
        launch(args);
    }
 
    /** Método para chamar os métodos de criaçao de Scene..

        */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        databaseHandler = DatabaseHandler.getInstance();
        initUI();
        stage.show();
    }
    
    /** Método para criaçao da Scene.

        */
    private void initUI() {
        VBox vbox = new VBox(20);  // CRIA UMA VBOX PARA SER ADICIONADA A SCENE.
        vbox.setStyle("-fx-padding: 10;"); // ACRESCENTA "PADDING" A VBOX, QUE DA ESPAÇAMENTO ENTRE OS NODES.
        String qu1 = "SELECT nome FROM CASA WHERE lote = '" + lot + "'"; // SQL STATEMENT "SELECT"
        ResultSet r23 = databaseHandler.execQuery(qu1); // LE O "SELECT" STATEMENT E GUARDA NUMA VARIAVEL.
        try {
            r23.next(); // FAZ ITERAÇAO DO SELECT, OU SEJA, É ESTE O COMANDO QUE PERCORRE OS RESULTADOS DADOS PELO SELECT.
            lop = r23.getString("nome"); // GUARDA O NOME DA OPERAÇAO ANTERIOR DENTRO DUMA VARIAVEL.
            stage.setTitle("Proprietário: " + lop); // METE O TITULO DA STAGE PARA O QUE ESTA DENTRO DE "()".
            setKak(lop); // CHAMA O METODO "setKak" E METE A VARIAVEL "KAK" IGUAL AO CONTEUDO DA VARIAL "LAP".
        } catch (SQLException ex) {
            Logger.getLogger(Proprietário.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        Scene scene = new Scene(vbox, 400, 400); // CRIAÇAO DA SCENE.
        stage.setScene(scene);
        
        Button btnDate = new Button("Reservas"); // CRIAÇAO DE BUTOES COM O NOME DENTRO DOS "()".
        Button btnRela = new Button("Relatório Finanças");
        Button btnVoltar = new Button("Voltar");
        
        
        GridPane gridPane = new GridPane(); // CRIA UMA GRIDPANE PARA SER ADICIONADA A SCENE.
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        gridPane.add(btnDate, 0, 0); // ADICIONA OS BUTOES NA GRIDPANE, ORGANIZADOS PELOS NUMEROS DENTRO DOS "()".
        gridPane.add(btnRela, 0, 2);
        gridPane.add(btnVoltar, 0, 4);
        vbox.getChildren().add(gridPane); // A GRIDPANE VAI PARA DENTRO DA VBOX.
     
        btnDate.setOnAction((ActionEvent event) -> { // AÇOES PARA CADA UM DOS BOTOES. QUANDO CLICAMOS NO BOTAO ATIVA ESTA FUNÇAO.
            ReservasProprietario data = new ReservasProprietario(lot,kak,lop);
            data.start(stage);
        });
        
        btnRela.setOnAction((ActionEvent event) -> {
            RelatorioFinanças rf = new RelatorioFinanças(lot,kak,lop);
            rf.start(stage);
        });
        
        
        
        btnVoltar.setOnAction((ActionEvent event) -> {
            Main main = new Main();
            main.start(stage);
        });
    
    }
    

     /** Método para receber o conteudo de "Kak"
      * @return String - Nome do Proprietario.

     */
    public String getKak() {
        return kak;
    }

    public void setKak(String kak) {
        this.kak = kak;
    }
    
    
    
}
