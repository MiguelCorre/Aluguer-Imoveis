/**Classe onde os Clientes fazem as suas reservas. Apresenta várias tabs, uma para cada lote existente na empresa e 
 * os clientes metem a sua informaçao pessoal, escolhem os dias de check-IN e check-OUT e escolhem se querem
 * boleia até ao lote ou nao.

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */

package AluguerImovel;

import Ficheiros.DatabaseHandler;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import Administraçao.ReservasAdministraçao;
import Administraçao.RelatorioAdministraçao;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import projeto_poo.Login;
import projeto_poo.Main;

public class AluguerImoveisClientes extends Application {
  
    private Callback<DatePicker, DateCell> dayCellFactory1;
    private Callback<DatePicker, DateCell> dayCellFactoryIn1;
    DatabaseHandler databaseHandler;
    private Stage primaryStage;
    private Button btnDate1;
    private Button btnVoltar;
    private Button btnDelete;
    private int foo; // VARIAVEL PARA RECEBER O NUMERO DO LOTE DA BASE DE DADOS.
    ListView<String> list ;

  public static void main(String[] args) {
    Application.launch(args);
  }

  /** Método para chamar os métodos de criaçao de Scene..
        */
  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    databaseHandler = DatabaseHandler.getInstance();
    
    checkData();
    initUI();
    loadData();

  }
  
  /** Método para verificar o conteudo dentro da base de dados, para fins de testes.
     * 
        */
  private void checkData() {
        String qu = "SELECT * FROM RESERVA WHERE lote = '1'";
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String lotex = rs.getString("id");
                String ruax = rs.getString("data_in");
                String availx = rs.getString("nomeCli");
                String availxx = rs.getString("carro");
                System.out.println(lotex);
                System.out.println(ruax);
                System.out.println(availx);
                System.out.println(availxx);
                System.out.println("");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
    primaryStage.setTitle("Tabs");
    StackPane root = new StackPane();
    Scene scene = new Scene(root, 700, 750, Color.WHITE);
    TabPane tabPane = new TabPane();
    //BorderPane borderPane = new BorderPane();

    String qu = "SELECT count(*) FROM CASA";
    ResultSet r1 = databaseHandler.execQuery(qu);
    final DatePicker[] checkInDatePicker;
    final DatePicker[] checkOutDatePicker;

    try {
      //  while 
      r1.next(); //{
      int colCount = r1.getInt(1);
      System.out.println(colCount);
      checkInDatePicker = new DatePicker[colCount];
      checkOutDatePicker = new DatePicker[colCount];

      for (int i = 0; i < colCount; i++) {

        Tab tab = new Tab();
        tab.setText("Lote" + (i + 1));
        tab.setId("" + (i + 1));
        tab.setClosable(false);

        
        BorderPane border = new BorderPane();
        tab.setContent(border);

        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        vbox.getChildren().add(new Label("Check In"));
        vbox.setAlignment(Pos.CENTER);
        border.setLeft(vbox);

        btnDate1 = new Button("Reservar");
        btnVoltar = new Button("Voltar");
        btnDelete = new Button("Cancelamento Reservas");

        tabPane.getTabs().add(tab);

        checkInDatePicker[i] = new DatePicker();
        checkInDatePicker[i].setId("" + (i + 1));
        checkOutDatePicker[i] = new DatePicker();
        checkOutDatePicker[i].setId("" + (i + 1));
        int fooo = Integer.parseInt(checkInDatePicker[i].getId());
        checkInDatePicker[i].setValue(LocalDate.now());
        LocalDate kk = checkInDatePicker[i].getValue();
        final DatePicker xptoIn = checkInDatePicker[i];
        final DatePicker xptoOut = checkOutDatePicker[i];

        final Callback<DatePicker, DateCell> dayCellFactory1;
        dayCellFactory1 = new Callback<DatePicker, DateCell>() {
          @Override
          public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
              @Override
              public void updateItem(LocalDate item, boolean empty) { // VARIAVEL "ITEM" É O DIA ESCOLHIDO NO CALENDARIO.
                super.updateItem(item, empty);

                foo = Integer.parseInt(tab.getId());
                String qu1 = "SELECT data_in, data_out FROM RESERVA WHERE lote = '" + foo + "'";
                ResultSet r2 = databaseHandler.execQuery(qu1);

                try {

                  while (r2.next()) {
                    Date teste = r2.getDate("data_in");
                    Date teste2 = r2.getDate("data_out");
                    long wow = (teste2.getTime() - teste.getTime()) / (1000 * 60 * 60 * 24); // CALCULO DO NUMERO DE DIAS ENTRE O CHECKIN E O CHECKOUT. A INFORMAÇAO VEM EM MILISSEGUNDOS LOGO FAZEMOS AQUELA DIVISAO PARA CONVERTER PARA DIAS.

                    for (int i = 0; i < wow; i++) {
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
                
                if (item.isBefore(xptoIn.getValue().plusDays(1))
                        || item.isAfter(xptoIn.getValue().plusDays(7))) {
                  setDisable(true);
                  setStyle("-fx-background-color: #ffc0cb;");
                }
                long p = ChronoUnit.DAYS.between(
                        xptoIn.getValue(), item
                );
                setTooltip(new Tooltip(
                        "You're about to stay for " + p + " days")
                );
              }
            };
          }
        };

        final Callback<DatePicker, DateCell> dayCellFactoryIn1;
        dayCellFactoryIn1 = new Callback<DatePicker, DateCell>() {
          @Override
          public DateCell call(final DatePicker datePicker) {
              return new DateCell() {
              @Override
              public void updateItem(LocalDate item, boolean empty) { // VARIAVEL "ITEM" É O DIA ESCOLHIDO NO CALENDARIO.
                super.updateItem(item, empty);
                foo = Integer.parseInt(tab.getId());
                String qu1 = "SELECT data_in, data_out FROM RESERVA WHERE lote = '" + foo + "'";
                ResultSet r2 = databaseHandler.execQuery(qu1);

                try {

                  while (r2.next()) {
                    Date teste = r2.getDate("data_in");
                    Date teste2 = r2.getDate("data_out");
                    long wow = (teste2.getTime() - teste.getTime()) / (1000 * 60 * 60 * 24); // CALCULO DO NUMERO DE DIAS ENTRE O CHECKIN E O CHECKOUT. A INFORMAÇAO VEM EM MILISSEGUNDOS LOGO FAZEMOS AQUELA DIVISAO PARA CONVERTER PARA DIAS.

                    for (int i = 0; i < wow; i++) {
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
        checkInDatePicker[i].setDayCellFactory(dayCellFactoryIn1);
        checkOutDatePicker[i].setDayCellFactory(dayCellFactory1);
        checkOutDatePicker[i].setValue(checkInDatePicker[i].getValue().plusDays(1));
        
        foo = Integer.parseInt(tab.getId());
        list = new ListView();
        loadData();
        border.setRight(list);
        
        TextField txtNomeC = new TextField();
        TextField txtContactoC = new TextField();
        ComboBox combobox = new ComboBox(); // COMBO BOX
        combobox.setEditable(true); 
        String nenhum = "Nenhum";
        combobox.getItems().addAll(nenhum, "Carro 4 portas", 
                "Carrinha 7 portas");

        vbox.getChildren().add(checkInDatePicker[i]); // ADICIONA AS CELLS À VBOX PARA PODER SER MOSTRADO NA SCENE.
        vbox.getChildren().add(new Label("Check Out"));
        vbox.getChildren().add(checkOutDatePicker[i]);
        vbox.getChildren().add(new Label("Nome Completo"));
        vbox.getChildren().add(txtNomeC);
        vbox.getChildren().add(new Label("Contacto"));
        vbox.getChildren().add(txtContactoC);
        vbox.getChildren().add(new Label("Boleia"));
        vbox.getChildren().add(combobox);
        vbox.getChildren().add(btnDate1);
        vbox.getChildren().add(btnVoltar);
        vbox.getChildren().add(btnDelete);
        vbox.setAlignment(Pos.TOP_LEFT);
        
        btnVoltar.setOnAction((ActionEvent event) -> {
          Main main = new Main();
          main.start(primaryStage);
        });
        
        btnDelete.setOnAction((ActionEvent event) -> {
          CancelamentoReservas cancel = new CancelamentoReservas();
          cancel.start(primaryStage);
        });

        btnDate1.setOnAction((ActionEvent event) -> {
          String loteID = tab.getId();
          String NomeCli = txtNomeC.getText();
          String ContactoC = txtContactoC.getText();
          java.sql.Date checkIN = java.sql.Date.valueOf(xptoIn.getValue());
          java.sql.Date checkOUT = java.sql.Date.valueOf(xptoOut.getValue());
          String carroReserva = combobox.getEditor().getText();
          
          String finid =""; //ID final
            Random r = new Random();
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //String onde se vai buscar a letra na variavel 'r'

            for (int u = 0; u < 2; u++) {
                char id =(alphabet.charAt(r.nextInt(alphabet.length()))); //busca do caracter random
                finid += String.valueOf(id);
            }
            for (int u = 0; u < 3; u++){
                int num = r.nextInt(9) + 0;//busca de um numero random entre 0-9
                finid+=String.valueOf(num);

            }

          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("Confirmar Reserva");
          alert.setHeaderText(null);
          alert.setContentText("Confirmar a reserva?");

          Optional<ButtonType> response = alert.showAndWait();

          if (response.get() == ButtonType.OK) {
            
              if (carroReserva.isEmpty() || carroReserva.equals(nenhum) ) {
                  String str = "INSERT INTO RESERVA(lote, id, data_in, data_out, nomeCli, contacto)  VALUES (  "
                    + "'" + loteID + "',"
                    + "'" + finid + "',"
                    + "'" + checkIN + "',"
                    + "'" + checkOUT + "',"
                    + "'" + NomeCli + "',"
                    + "'" + ContactoC + "')"; 

            String str2 = "UPDATE CASA SET avail = false WHERE lote = '" + loteID + "'";

            if (databaseHandler.execAction(str) && databaseHandler.execAction(str2)) {
              Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
              alert1.setTitle("Successo!!");
              alert1.setHeaderText(null);
              alert1.setContentText("A sua reserva é a número : " + finid);
              alert1.showAndWait();

            } else {
              Alert alert2 = new Alert(Alert.AlertType.ERROR);
              alert2.setTitle("Failure.");
              alert2.setHeaderText(null);
              alert2.setContentText("Book issue failed!");
              alert2.showAndWait();
            }
              } else {
            String str = "INSERT INTO RESERVA  VALUES (  "
                    + "'" + loteID + "',"
                    + "'" + finid + "',"
                    + "'" + checkIN + "',"
                    + "'" + checkOUT + "',"
                    + "'" + NomeCli + "',"
                    + "'" + ContactoC + "'," 
                    + "'" + carroReserva + "')";

            String str2 = "UPDATE CASA SET avail = false WHERE lote = '" + loteID + "'";

            if (databaseHandler.execAction(str) && databaseHandler.execAction(str2)) {
              Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
              alert1.setTitle("Successo!!");
              alert1.setHeaderText(null);
              alert1.setContentText("A sua reserva é a número : " + finid);
              alert1.showAndWait();

            } else {
              Alert alert2 = new Alert(Alert.AlertType.ERROR);
              alert2.setTitle("Failure.");
              alert2.setHeaderText(null);
              alert2.setContentText("Book issue failed!");
              alert2.showAndWait();
            }
              }
          } else {
            Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
            alert3.setTitle("Cancelled");
            alert3.setHeaderText(null);
            alert3.setContentText("Book issue Cancelled!");
            alert3.showAndWait();
          }
          
        });

      }

    } catch (SQLException ex) {
      Logger.getLogger(ReservasAdministraçao.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    root.getChildren().add(tabPane);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}