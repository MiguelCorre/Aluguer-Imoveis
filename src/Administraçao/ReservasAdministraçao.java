/**Classe onde a Administraçao pode controlar todas as reservas feitas, basta escolher o lote desejado e aparece todas as reservas feitas até ao momento.

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */

package Administraçao;

import Ficheiros.DatabaseHandler;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import projeto_poo.Login;

public class ReservasAdministraçao extends Application {
  
   private Stage stage;
   private DatePicker checkInDatePicker;   
   DatabaseHandler databaseHandler;
   private int foo;
   ListView<String> list = new ListView<String>();
    
   Label lblLote = new Label("Nº Lote");
    
 
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);                  
        launch(args);
    }
    
    
    /** Método para verificar o conteudo dentro da base de dados, para fins de testes.
     * 
        */
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
     * @param stage - chama a stage.
        */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        databaseHandler = DatabaseHandler.getInstance();

        stage.setTitle("Reservas ");
        
        checkData();
        initUI();
        loadData();
        stage.show();
    }
    
    /** Método para carregar dados da base de dados para a ListView(Relatorio Financeiro).

        */
    private void loadData() {
        Login login = new Login();
        ObservableList<String> issueData = FXCollections.observableArrayList();

            String qu = "SELECT * FROM CASA WHERE lote = '" + foo + "'";
            ResultSet rs = databaseHandler.execQuery(qu);

            try {
                while (rs.next()) {
                    issueData.add("----------Dados do Imovél----------");
                    issueData.add("Numero do lote : " + rs.getString("lote"));
                    issueData.add("");
                    issueData.add("Rua : " + rs.getString("rua"));
                    issueData.add("");
                    issueData.add(rs.getString("tipo"));
                    issueData.add("");
                    issueData.add("Nº Quartos : " + rs.getString("quartos"));
                    issueData.add("");
                    issueData.add("Preço por noite : " + rs.getString("preco"));
                    issueData.add("");
                    issueData.add("Proprietário : " + rs.getString("nome"));
            
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
        Scene scene = new Scene(border, 400, 400);
        stage.setScene(scene);
        
        checkInDatePicker = new DatePicker();        
        checkInDatePicker.setValue(LocalDate.now());
        
        
        final Callback<DatePicker, DateCell> dayCellFactoryIn;
        dayCellFactoryIn = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        String qu = "SELECT data_in, data_out FROM RESERVA WHERE lote = '" + foo  + "'";
                        ResultSet r1 = databaseHandler.execQuery(qu);
            
                        try {
                            
                            while (r1.next()) {
                                Date teste = r1.getDate("data_in");
                                Date teste2 = r1.getDate("data_out");
                                long wow = (teste2.getTime() - teste.getTime()) / (1000 * 60 * 60 * 24); // CALCULO DO NUMERO DE DIAS ENTRE O CHECKIN E O CHECKOUT. A INFORMAÇAO VEM EM MILISSEGUNDOS LOGO FAZEMOS AQUELA DIVISAO PARA CONVERTER PARA DIAS.

                                for (int i = 0; i < wow ; i++ ) {
                                    LocalDate ld = new java.sql.Date(teste.getTime()).toLocalDate().plusDays(i);
                                    LocalDate ld2 = new java.sql.Date(teste2.getTime()).toLocalDate();
                                    
                                    if (item.isEqual(ld) || item.isEqual(ld2)) {
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;");
                                    }
                                    
                                    
                                }
                                
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ReservasAdministraçao.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
         
        checkInDatePicker.setDayCellFactory(dayCellFactoryIn);
        
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        Button btnCheck = new Button("Ok");
        Button btnVoltar = new Button("Voltar");
        
        String qu1 = "SELECT lote FROM CASA";
        ResultSet r23 = databaseHandler.execQuery(qu1);
        ComboBox combobox = new ComboBox(); 
        combobox.setEditable(true); // COMBO BOX
        
        try {              
            while (r23.next()) {
                String lotee = r23.getString("lote");  
                combobox.getItems().add(lotee);
                }              
                } catch (SQLException ex) {
                  Logger.getLogger(ReservasAdministraçao.class.getName()).log(Level.SEVERE, null, ex);
                } 
        
        Label checkInlabel = new Label("Calendário");
        gridPane.add(checkInlabel, 0, 0);
        GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(checkInDatePicker, 0, 1);
        gridPane.add(btnCheck, 0, 11);
        gridPane.add(btnVoltar, 0, 7);
        gridPane.add(lblLote, 0, 9);
        gridPane.add(combobox, 0, 10);
        vbox.getChildren().add(gridPane);
        border.setLeft(vbox);
        border.setRight(list);
        
        //////////////////////////////////////////////////////////////////////////
        
        btnVoltar.setOnAction((ActionEvent event) -> {
            Administraçao aic = new Administraçao();
            aic.start(stage);
        });
        
        btnCheck.setOnAction((ActionEvent event) -> {  
            String loteID = combobox.getEditor().getText();
            foo = Integer.parseInt(loteID);
            System.out.println("OI");
            checkInDatePicker.setDayCellFactory(dayCellFactoryIn);
            loadData();
        });
        
    }
} 
