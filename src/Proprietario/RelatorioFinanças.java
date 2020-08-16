/**Classe onde se pode verificar o relatorio financeiro do Proprietario.

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */
package Proprietario;

import Administraçao.ReservasAdministraçao;
import Administraçao.RelatorioAdministraçao;
import Ficheiros.DatabaseHandler;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class RelatorioFinanças extends Proprietário {
    private Stage stage; 
   DatabaseHandler databaseHandler;
   private int foo; // VARIAVEL QUE RECEBE O NUMERO DO LOTE DA BASE DADOS.
   private int foo2; // VARIAVEL QUE RECEBE O MES DA BASE DADOS.
   private String str3;
   ListView<String> list = new ListView<>(); // CRIAÇAO DE UMA LIST VIEW PARA MOSTRAR OS DADOS.
   String kak; // NOME PROPRIETARIO.
   String lop; // VARIAVEL PARA ACEDER BASE DADOS.
   int lot; // NUMERO LOTE.
   
   
   ArrayList<Integer> money = new ArrayList<>();
   ArrayList<Integer> dias = new ArrayList<>();
   
   /** Constructor para a classe RelatorioFinanças, faz referencia a classe-mae Proprietario
     * @param lot int - nº do lote
     * @param kak String - nome do Proprietario
     * @param lop String - nome retirado da base dados

        */
   RelatorioFinanças(int lot, String kak, String lop) {
       super(lot,kak,lop);
       this.kak =  kak;
       this.lop =  lop;
       this.lot =  lot;
       
   }
   

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);                  
        launch(args);
    }
    
    
    private void checkData() {
        String qu = "SELECT * FROM RESERVA";
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String lotexx = rs.getString("lote");
                String datainx = rs.getString("data_in");
                String dataoutx = rs.getString("data_out");
                System.out.println(lotexx);
                System.out.println(datainx);
                System.out.println(dataoutx);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
 
    /** Método para chamar os métodos de criaçao de Scene..

        */
    public void start(Stage stage) {
        this.stage = stage;
        databaseHandler = DatabaseHandler.getInstance();

        stage.setTitle("Relatorio Financeiro");
        
        checkData();
        initUI();
        loadData();
        stage.show();
    }
    
    /** Método para calcular o dinheiro ganho pelo Proprietario e pela Administraçao
     * @param comissao float - percentagem de comissao.
     * @param sum float - soma do dinheiro total ganho com as reservas.
      * @return int - Resultado do total ganho.

     */
    private float somaArraysMoney(float comissao) { // SOMA OS ELEMENTOS DO ARRAY PARA CALCULAR VALOR TOTAL DE DINHEIRO GANHO.
        float sum = 0;
        float conta = 0;
        for(int i = 0; i < money.size(); i++)
            sum += money.get(i);
        if(comissao < 1) {
        conta = sum - sum * comissao;
        }
        return sum - conta;
    }
    
    /** Método para calcular o dinheiro ganho pelo Proprietario e pela Administraçao
     * @param sum2 float - soma do dinheiro total ganho com as reservas.
      * @return int - Resultado do total ganho.

     */
    private int somaArraysDias() { // SOMA OS ELEMENTOS DO ARRAY PARA CALCULAR VALOR TOTAL DE DIAS RESERVADOS.
        int sum2 = 0;
        for(int i = 0; i < dias.size(); i++)
            sum2 += dias.get(i);
        return sum2;
    }
    
    
    private void loadData() {
        ObservableList<String> issueData = FXCollections.observableArrayList();
        

            String qu = "SELECT * FROM CASA WHERE lote = '" + foo + "'";
            ResultSet rs = databaseHandler.execQuery(qu);

            try {
                while (rs.next()) {
                    issueData.add("----------Dados do Imovél----------");
                    issueData.add("Numero do lote : " + rs.getString("lote"));      
                    issueData.add("Rua : " + rs.getString("rua"));                 
                    issueData.add(rs.getString("tipo"));                 
                    issueData.add("Nº Quartos : " + rs.getString("quartos"));              
                    issueData.add("Preço por noite : " + rs.getString("preco"));               
                    issueData.add("Proprietário : " + rs.getString("nome"));                 
                    issueData.add("----------Finanças: " + str3 + "----------");
                    String qu2 = "SELECT data_in, data_out FROM RESERVA WHERE lote = '" + foo + "' AND MONTH(data_in) = " + foo2 + " AND nomeCli IS NOT NULL GROUP BY data_in, data_out ORDER BY data_in, data_out";
                    ResultSet rs2 = databaseHandler.execQuery(qu2);
                    while (rs2.next()) {
                        int teste3 = Integer.parseInt(rs.getString("preco"));
                        Date teste = rs2.getDate("data_in");
                        Date teste2 = rs2.getDate("data_out");
                        int wow = (int) ((teste2.getTime() - teste.getTime()) / (1000 * 60 * 60 * 24));
                        int soma = wow*teste3;
                        money.add(soma);
                        dias.add(wow);
                        issueData.add("Reserva de: " + rs2.getString("data_in") + " a " + rs2.getString("data_out") + ", Total: " + soma + " euros.");
                        
                    }
                    issueData.add("");
                    issueData.add("Vencimento bruto total : " + somaArraysMoney(1) + " euros.");
                    issueData.add("Vencimento liquido total : " + somaArraysMoney(0.30f) + " euros.");
                    issueData.add("Total número de noites reservadas: " + somaArraysDias() + " noites.");
            
                }
            } catch (SQLException ex) {
                Logger.getLogger(RelatorioAdministraçao.class.getName()).log(Level.SEVERE, null, ex);
            }
            list.setItems(issueData);
        }
 
    private void initUI() {
        BorderPane border = new BorderPane();
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        Scene scene = new Scene(border, 500, 400);
        stage.setScene(scene);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        Button btnCheck = new Button("Ok");
        Button btnVoltar = new Button("Voltar");
        
        
        String qu1 = "SELECT lote FROM CASA WHERE nome = '" + kak + "'";
        ResultSet r23 = databaseHandler.execQuery(qu1);
        ComboBox combobox = new ComboBox(); 
        combobox.setEditable(true); // COMBO BOX
        
        ComboBox combobox2 = new ComboBox(); 
        combobox2.setEditable(true);
        
        try {              
            while (r23.next()) {
                String lotee = r23.getString("lote");  
                combobox.getItems().add("Lote " + lotee);
                }              
                } catch (SQLException ex) {
                  Logger.getLogger(ReservasAdministraçao.class.getName()).log(Level.SEVERE, null, ex);
                } 
        
                     
            combobox2.getItems().addAll("Janeiro 01",
                    "Fevereiro 02",
                    "Março 03",
                    "Abril 04",
                    "Maio 05",
                    "Junho 06",
                    "Julho 07",
                    "Agosto 08",
                    "Setembro 09",
                    "Outubro 10",
                    "Novembro 11",
                    "Dezembro 12");
  
        //Label checkInlabel = new Label("Calendário");
        Label lblLote = new Label("Nº Lote");

        //GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(lblLote, 0, 9); // ADICIONA OS ELEMENTOS À GRIDPANE PARA SER DISPLAYED.
        gridPane.add(combobox, 0, 10);
        gridPane.add(combobox2, 0, 12);
        gridPane.add(btnCheck, 0, 13);
        gridPane.add(btnVoltar, 0, 14);
        vbox.getChildren().add(gridPane); // METE A GRIDPANE DENTRO DA VBOX.
        border.setLeft(vbox); // ORGANIZA OS ELEMENTOS, DENTRO DA SCENE, NUM BORDERPANE, QUE PERMITE ESCOLHER EM QUE LADO QUEREMOS AS COISAS.
        border.setCenter(list);
        
        //////////////////////////////////////////////////////////////////////////
        
        btnVoltar.setOnAction((ActionEvent event) -> {
            Proprietário aic = new Proprietário(lot,kak,lop);
            aic.start(stage);
        });
        
        btnCheck.setOnAction((ActionEvent event) -> {
            String loteID = combobox.getEditor().getText();  // EXTRAI INFORMAÇAO DO LOTE ESCOLHIDO NO COMBOBOX.
            String str = loteID.replaceAll("\\D+","");   // REMOVE TODOS OS CARATERES QUE NAO SEJAM NUMEROS.
            String mes = combobox2.getEditor().getText();
            String str2 = mes.replaceAll("\\D+","");
            str3 = mes.replaceAll("\\d",""); // REMOVE TODOS OS NÚMEROS.
            foo = Integer.parseInt(str);  // TRANSFORMA OS NUMEROS QUE ESTAVAM EM STRING E METE EM INTEGER, PARA PODER SER USADO NAS OPERAÇOES LA EM CIMA.
            foo2 = Integer.parseInt(str2);
            
            loadData();
            dias.removeAll(dias); // APAGA OS ARRAYS PARA NAO HAVER CONFLITO AO MEXER EM VARIOS LOTES.
            money.removeAll(money); // MESMA RAZAO
        });
        
    }
}
