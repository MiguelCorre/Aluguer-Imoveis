/**Classe onde a Administraçao pode verificar o relatorio financeiro anual e onde recebe "updates" diários dos seus funcionarios quando, por exemplo,
 * o motorista vai buscar alguem ao aeroporto ou a equipa de limpeza arruma um lote etc...

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */
package Administraçao;

import Ficheiros.DatabaseHandler;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class RelatorioAdministraçao extends Application{
    
   private Stage stage;  //VARIAVEL DA STAGE PARA A SCENE.
   DatabaseHandler databaseHandler; //VARIAVEL DA BASE DE DADOS.
   private int foo; // VARIAVEL QUE RECEBE O NUMERO DO LOTE DA BASE DADOS.
   private int foo2; // VARIAVEL QUE RECEBE O MES DA BASE DADOS.
   private String str3; // VARIAVEL AUXILIAR NO PROCESSAMENTO DA BASE DE DADOS
   ListView<String> list = new ListView<>(); // CRIAÇAO DE UMA LIST VIEW PARA MOSTRAR OS DADOS.

   
   ArrayList<Integer> money = new ArrayList<>();
   ArrayList<Integer> dias = new ArrayList<>();
   Random random = new Random();
   
    
   Label lblLote = new Label("Nº Lote");
    
 
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);                  
        launch(args);
    }

 
    /** Método para criar Scene.
     * 
     * @param stage - variavel responsavel pela stage.
        */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        databaseHandler = DatabaseHandler.getInstance();
        list.setEditable(true);
        list.setCellFactory(TextFieldListCell.forListView());
        stage.setTitle("Relatorio Financeiro");
        
        
        initUI();
        loadData();
        loadData2();
        stage.show();
    }
    
    /** Método para calcular o dinheiro ganho pelo Proprietario e pela Administraçao
     * @param comissao float - percentagem de comissao.
      * @return int - Resultado do total ganho.

     */
    public float somaArraysMoney(float comissao) { // SOMA OS ELEMENTOS DO ARRAY PARA CALCULAR VALOR TOTAL DE DINHEIRO GANHO.
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
     * @param Imp float - imposto.
      * @return int - Resultado do total ganho.

     */
    public float somaArraysLiq(float Imp) { // SOMA OS ELEMENTOS DO ARRAY PARA CALCULAR VALOR TOTAL DE DINHEIRO GANHO.
        float sum = 0;
        float conta = 0;
        float num = 0;
        for(int i = 0; i < money.size(); i++)
            sum += money.get(i);
        
        conta =sum - sum*0.30f;
        num = conta * 0.25f;
        conta = conta - num;
        
        
        return conta;
    }
    private int somaArraysDias() { // SOMA OS ELEMENTOS DO ARRAY PARA CALCULAR VALOR TOTAL DE DIAS RESERVADOS.
        int sum2 = 0;
        for(int i = 0; i < dias.size(); i++)
            sum2 += dias.get(i);
        return sum2;
    }
    
    /** Método para carregar dados da base de dados para a ListView(Relatorio Financeiro).

        */
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
                    issueData.add("Preço por noite : " + rs.getString("preco") + " euros");               
                    issueData.add("Proprietário : " + rs.getString("nome"));                 
                    issueData.add("----------Finanças----------");
                    String qu2 = "SELECT data_in, data_out FROM RESERVA WHERE lote = '" + foo + "' AND nomeCli IS NOT NULL GROUP BY data_in, data_out ORDER BY data_in, data_out";
                    ResultSet rs2 = databaseHandler.execQuery(qu2);
                    while (rs2.next()) {
                        int teste3 = Integer.parseInt(rs.getString("preco"));
                        Date teste = rs2.getDate("data_in");
                        Date teste2 = rs2.getDate("data_out");
                        int wow = (int) ((teste2.getTime() - teste.getTime()) / (1000 * 60 * 60 * 24));
                        int soma = wow*teste3;
                        money.add(soma);
                        dias.add(wow);
                        
                        
                    }
                    issueData.add("");
                    issueData.add("Total número de noites reservadas: " + somaArraysDias() + " noites.");
                    issueData.add("Vencimento bruto total : " + somaArraysMoney(1) + " euros.");
                    issueData.add("Vencimento liquido total : " + somaArraysLiq(0.70f) + " euros.");
                    issueData.add("Valor pago ao proprietário : " + somaArraysMoney(0.30f) + " euros.");
                    
            
                }
            } catch (SQLException ex) {
                Logger.getLogger(RelatorioAdministraçao.class.getName()).log(Level.SEVERE, null, ex);
            }
            list.setItems(issueData);
            
        }
    
    /** Método para carregar dados da base de dados para a ListView(RelatorioFuncionarios).

        */
    private void loadData2() {
        ObservableList<String> issueData = FXCollections.observableArrayList();

            String qu = "SELECT RESERVA.data_in, RESERVA.data_out, RESERVA.id, RESERVA.nomeCli, FUNCIONARIO.nome FROM RESERVA JOIN FUNCIONARIO ON FUNCIONARIO.carro = RESERVA.carro WHERE lote = '" + foo + "'";
            ResultSet rs = databaseHandler.execQuery(qu);

            String qu2 = "SELECT nome FROM FUNCIONARIO WHERE funcao = 'Limpeza' AND avail = true";
            ResultSet rs2 = databaseHandler.execQuery(qu2);
            
 
            try {
                 rs2.next();
                while (rs.next()) {
                    
                    Date teste = rs.getDate("data_in");
                    Date teste2 = rs.getDate("data_out");
                    LocalDate ld = new java.sql.Date(teste.getTime()).toLocalDate();
                    LocalDate ld2 = new java.sql.Date(teste2.getTime()).toLocalDate();
                    if(ld2.isEqual(LocalDate.now()) || ld2.isBefore(LocalDate.now())) {
                        issueData.add("-- " + rs.getString("data_in") + " ( Check-IN da reserva: " + rs.getString("id") + " )");
                        issueData.add(rs.getString("nome") + " foi buscar " + rs.getString("nomeCli") +  " ao aeroporto."); 
                        issueData.add("-- " + rs.getString("data_out") + " ( Check-OUT da reserva: " + rs.getString("id") + " )");
                        issueData.add(rs2.getString("nome") + " e a sua equipa de Limpeza foram limpar o lote. " );
                        
                        issueData.add("");
                    }
                    else if(ld.isEqual(LocalDate.now()) || ld.isBefore(LocalDate.now())) {
                        issueData.add("-- " + rs.getString("data_in") + " ( Check-IN da reserva: " + rs.getString("id") + " )");
                        issueData.add(rs.getString("nome") + " foi buscar " + rs.getString("nomeCli") +  " ao aeroporto.");  
                        
                        /* issueData.add("-- " + rs.getString("data_in") + " ( Check-IN da reserva: " + rs.getString("id") + " )");
                        issueData.add(rs.getString("nome") + " foi buscar " + rs.getString("nomeCli") +  " ao aeroporto.");      
                        issueData.add("-- " + rs.getString("data_out") + " ( Check-OUT da reserva: " + rs.getString("id") + " )");
                        issueData.add(rs2.getString("nome") + " e a sua equipa de Limpeza foram limpar o lote.");
                        issueData.add("");
*/
                    }
                        

            
                }
            } catch (SQLException ex) {
                Logger.getLogger(RelatorioAdministraçao.class.getName()).log(Level.SEVERE, null, ex);
            }
            list.setItems(issueData);
            
        }
 
    /** Método para criar a Scene.

        */
    private void initUI() {
        BorderPane border = new BorderPane();
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        Scene scene = new Scene(border, 600, 500);
        stage.setScene(scene);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        Button btnCheck = new Button("Financeiro");
        Button btnCheck2 = new Button("Funcionarios");
        Button btnVoltar = new Button("Voltar");
        
        String qu1 = "SELECT lote FROM CASA";
        ResultSet r23 = databaseHandler.execQuery(qu1);
        ComboBox combobox = new ComboBox(); 
        combobox.setEditable(true); // COMBO BOX
        
        
        try {              
            while (r23.next()) {
                String lotee = r23.getString("lote");  
                combobox.getItems().add("Lote " + lotee);
                }              
                } catch (SQLException ex) {
                  Logger.getLogger(ReservasAdministraçao.class.getName()).log(Level.SEVERE, null, ex);
                } 

  
        Label checkInlabel = new Label("Calendário");

        GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(lblLote, 0, 9);
        gridPane.add(combobox, 0, 10);
        gridPane.add(btnCheck, 0, 13);
        gridPane.add(btnCheck2, 0, 5);
        gridPane.add(btnVoltar, 0, 14);
        vbox.getChildren().add(gridPane);
        border.setLeft(vbox);
        border.setCenter(list);

        
        //////////////////////////////////////////////////////////////////////////
        
        btnVoltar.setOnAction((ActionEvent event) -> {
            Administraçao aic = new Administraçao();
            aic.start(stage);
        });
        
        
        btnCheck.setOnAction((ActionEvent event) -> {
            String loteID = combobox.getEditor().getText();  // EXTRAI INFORMAÇAO DO LOTE ESCOLHIDO NO COMBOBOX.
            String str = loteID.replaceAll("\\D+","");   // REMOVE TODOS OS CARATERES QUE NAO SEJAM NUMEROS.
            foo = Integer.parseInt(str);  // TRANSFORMA OS NUMEROS QUE ESTAVAM EM STRING E METE EM INTEGER, PARA PODER SER USADO NAS OPERAÇOES LA EM CIMA.
   
            loadData();
            dias.removeAll(dias); // APAGA OS ARRAYS PARA NAO HAVER CONFLITO AO MEXER EM VARIOS LOTES.
            money.removeAll(money); // MESMA RAZAO
            
        });
        
        btnCheck2.setOnAction((ActionEvent event) -> { 
            String loteID = combobox.getEditor().getText();  // EXTRAI INFORMAÇAO DO LOTE ESCOLHIDO NO COMBOBOX.
            String str = loteID.replaceAll("\\D+","");   // REMOVE TODOS OS CARATERES QUE NAO SEJAM NUMEROS.
            foo = Integer.parseInt(str);  // TRANSFORMA OS NUMEROS QUE ESTAVAM EM STRING E METE EM INTEGER, PARA PODER SER USADO NAS OPERAÇOES LA EM CIMA.
            System.out.println(foo);
            
            loadData2();

        });
        
    }
}

