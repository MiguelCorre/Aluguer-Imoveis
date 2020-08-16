/**Classe para controlar as reservas feitos pelos Clientes e para o proprio Proprietario poder reservar o seu proprio lote.

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */
package Proprietario;

import Administraçao.ReservasAdministraçao;
import Administraçao.RelatorioAdministraçao;
import Ficheiros.DatabaseHandler;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import projeto_poo.Login;

/**
 *
 * @author Miguel HPC
 */
public class ReservasProprietario extends Proprietário {
    private Stage stage;
    private DatePicker checkInDatePicker;
    private DatePicker checkOutDatePicker;
    DatabaseHandler databaseHandler;
    private int foo;
    ListView<String> list = new ListView<String>();
    String kak;
    String lop;
    int lot;
    Label lblLote = new Label("Nº Lote");
    ComboBox cancela = new ComboBox();
    
 
    /** Constructor para a classe ReservasProprietario, faz referencia a classe-mae Proprietario
     * @param lot int - nº do lote
     * @param kak String - nome do Proprietario
     * @param lop String - nome retirado da base dados

        */
    ReservasProprietario(int lot, String kak, String lop) {
       super(lot,kak,lop);
       this.kak =  kak;
       this.lop =  lop;
       this.lot =  lot;
       
   }
    
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
    
    /** Método para carregar dados da base de dados para a ListView.

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
 
    /** Método para chamar os métodos de criaçao de Scene..
     * @param stage - chama a stage.
        */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        databaseHandler = DatabaseHandler.getInstance();
        
        checkData();            
        initUI();
        loadData();
        stage.setTitle("Reservas ");
        stage.show();
    }
 
    /** Método para criar a Scene.

        */
    private void initUI() {
        BorderPane border = new BorderPane();
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        Scene scene = new Scene(border, 400, 500);
        stage.setScene(scene);
        
        checkInDatePicker = new DatePicker();
        checkOutDatePicker = new DatePicker();
        checkInDatePicker.setValue(LocalDate.now());
        
        
        final Callback<DatePicker, DateCell> dayCellFactory;
        dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) { // VARIAVEL "ITEM" É O DIA ESCOLHIDO NO CALENDARIO.
                        super.updateItem(item, empty);
     
                        String qu = "SELECT data_in, data_out FROM RESERVA WHERE lote = '" + foo + "'";
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
                        
                        if (item.isBefore(checkInDatePicker.getValue().plusDays(1)) || item.isAfter(checkInDatePicker.getValue().plusDays(7))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }   
                        long p = ChronoUnit.DAYS.between(
                                    checkInDatePicker.getValue(), item
                            );
                            setTooltip(new Tooltip(
                                "You're about to stay for " + p + " days") // UM POPUP QUE INDICA QUANTOS DIAS VAI FICAR.
                            );
                    }
                };
            }
        };
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
                                long wow = (teste2.getTime() - teste.getTime()) / (1000 * 60 * 60 * 24);

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
        checkOutDatePicker.setDayCellFactory(dayCellFactory);
        checkOutDatePicker.setValue(checkInDatePicker.getValue().plusDays(1));
        
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        Button btnDate1 = new Button("Reservar");
        Button btnCheck = new Button("Ok");
        Button btnVoltar = new Button("Voltar");
        Button btnCancelar = new Button("Cancelar Reserva");
        
        String qu1 = "SELECT lote FROM CASA WHERE nome = '" + kak + "'";
        ResultSet r23 = databaseHandler.execQuery(qu1);
        ComboBox combobox = new ComboBox(); 
        combobox.setEditable(true); // COMBO BOX
        cancela.setEditable(true);
        
        try {              
            while (r23.next()) {
                String lotee = r23.getString("lote");  
                combobox.getItems().add(lotee);
                }              
                } catch (SQLException ex) {
                  Logger.getLogger(ReservasAdministraçao.class.getName()).log(Level.SEVERE, null, ex);
                }
        
        
        Label checkInlabel = new Label("Check-In Date:");
        gridPane.add(checkInlabel, 0, 0);
        GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(checkInDatePicker, 0, 1);
        Label checkOutlabel = new Label("Check-Out Date:");
        gridPane.add(checkOutlabel, 0, 2);
        GridPane.setHalignment(checkOutlabel, HPos.LEFT);
        gridPane.add(checkOutDatePicker, 0, 3);
        gridPane.add(btnDate1, 0, 5);
        gridPane.add(btnVoltar, 0, 7);
        gridPane.add(lblLote, 0, 9);
        gridPane.add(combobox, 0, 10);
        gridPane.add(btnCheck, 0, 12);
        gridPane.add(cancela, 0, 14);
        gridPane.add(btnCancelar, 0, 15);
        vbox.getChildren().add(gridPane);
        border.setLeft(vbox);
        border.setRight(list);
        
        //////////////////////////////////////////////////////////////////////////
        
        btnVoltar.setOnAction((ActionEvent event) -> {
            Proprietário prop = new Proprietário(lot,kak,lop);
            prop.start(stage);
        });
        
        
        
        btnDate1.setOnAction((ActionEvent event) -> {
            
            String loteID = combobox.getEditor().getText();
            
            java.sql.Date checkIN =java.sql.Date.valueOf(checkInDatePicker.getValue());
            java.sql.Date checkOUT =java.sql.Date.valueOf(checkOutDatePicker.getValue());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Operaçao");
            alert.setHeaderText(null);
            alert.setContentText("Quer confirmar a sua reserva?");

            Optional<ButtonType> response = alert.showAndWait();
            
            if (response.get() == ButtonType.OK) {
                String str = "INSERT INTO RESERVA (lote, data_in, data_out,nomeCli) VALUES (  "
                        + "'" + loteID + "',"
                        + "'" + checkIN + "',"
                        + "'" + checkOUT + "',"
                        + "" + null + ")";

                String str2 = "UPDATE CASA SET avail = false WHERE lote = '" + loteID + "'";

                if (databaseHandler.execAction(str) && databaseHandler.execAction(str2)) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Sucesso!!");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Reserva Concluida!");
                    alert1.showAndWait();

                } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Erro");
                    alert2.setHeaderText(null);
                    alert2.setContentText("Houve um erro");
                    alert2.showAndWait();
                }
            } else {
                Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                alert3.setTitle("Cancelado");
                alert3.setHeaderText(null);
                alert3.setContentText("A reserva foi cancelada");
                alert3.showAndWait();
            }
                    
            

        });
            
        btnCheck.setOnAction((ActionEvent event) -> {
            String loteID = combobox.getEditor().getText();
            String qu2 = "SELECT data_in, data_out FROM RESERVA WHERE nomeCli IS NULL AND lote = '" + loteID + "'";
            ResultSet r24 = databaseHandler.execQuery(qu2);
            try {              
                while (r24.next()) {
                    String loteee = r24.getString("data_in");  
                    //String loteeo = r24.getString("data_out");  
                    cancela.getItems().addAll(loteee);
                    }              
                    } catch (SQLException ex) {
                      Logger.getLogger(ReservasAdministraçao.class.getName()).log(Level.SEVERE, null, ex);
                    }
            foo = Integer.parseInt(loteID); // CONVERTE O NUMERO OBTIDO ACIMA PARA INTEGER.
            checkInDatePicker.setDayCellFactory(dayCellFactoryIn);
            checkOutDatePicker.setDayCellFactory(dayCellFactory);
            checkOutDatePicker.setValue(checkInDatePicker.getValue().plusDays(1));
            loadData();
                    
                });
        
        btnCancelar.setOnAction((ActionEvent event) -> {
            String data = cancela.getEditor().getText();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = formatter.parse(data);
            } catch (ParseException ex) {
                Logger.getLogger(ReservasProprietario.class.getName()).log(Level.SEVERE, null, ex);
            }
            String qa = "DELETE FROM RESERVA WHERE data_in = '" + data + "'";
            if (databaseHandler.execAction(qa)) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Sucesso!!");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Reserva cancelada com sucesso!");
                    alert1.showAndWait();

                } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Erro");
                    alert2.setHeaderText(null);
                    alert2.setContentText("Houve um erro");
                    alert2.showAndWait();
                }
            
            
        });
            
    }
}
