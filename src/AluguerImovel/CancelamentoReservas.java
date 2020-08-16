/**Classe onde os Clientes tem a opçao de cancelar qualquer reserva que queiram, bastando apenas colocar
 * o id da sua reserva, que recebem no momento da confirmaçao da mesma.

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */
package AluguerImovel;

import Administraçao.ReservasAdministraçao;
import Ficheiros.DatabaseHandler;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Miguel HPC
 */
public class CancelamentoReservas extends Application {
    
    private Stage stage; 
    DatabaseHandler databaseHandler;
    private String txtDel;
    private int CalcDias;
    private int teste2;
    private int teste3;
    
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        databaseHandler = DatabaseHandler.getInstance();
        
        initUI();
        stage.show();
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);                  
        launch(args);
    }
    
    
    
    
    private void initUI() {
        
        stage.setTitle("Cancelamentos");
        VBox root = new VBox();
        root.setStyle("-fx-padding: 10;");
        Scene scene = new Scene(root, 350, 250);
        Button btnDelete = new Button("Cancelar Reserva");
        Button btnVoltar = new Button("Voltar");
        TextField txtDelete = new TextField();
        
        root.getChildren().add(txtDelete);
        root.getChildren().add(btnDelete);
        root.getChildren().add(btnVoltar);
        
        
        btnVoltar.setOnAction((ActionEvent event) -> {
          AluguerImoveisClientes aic = new AluguerImoveisClientes();
          aic.start(stage);
        });
        btnDelete.setOnAction((ActionEvent event) -> {   //// BOTAO PARA APAGAR RESERVAS
          
          txtDel = txtDelete.getText();
          String qu = "SELECT data_in FROM RESERVA WHERE id = '" + txtDel + "'";
          ResultSet rs = databaseHandler.execQuery(qu);
          Boolean flag = false;  // MANDA UM FLAG PARA VERIFICAR SE EXISTE RESERVA
          try {     
                while(rs.next())  {
                Date teste = rs.getDate("data_in");
                LocalDate ld = new java.sql.Date(teste.getTime()).toLocalDate();
                teste2 = LocalDate.now().getDayOfMonth();
                teste3 = ld.getDayOfMonth();
                CalcDias = (teste3 - teste2);
                flag = true;      // SE NAO HOUVER ERRO A CORRER O CODIGO ACIMA, O FLAG TORNA-SE TRUE E CORRE TUDO COMO NORMAL
                
                if (-1 < CalcDias && CalcDias <= 2) {
                    System.out.println(CalcDias);
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Erro.");
                    alert2.setHeaderText(null);
                    alert2.setContentText("Nao pode cancelar reservas a menos de 48 horas.");
                    alert2.showAndWait();
                    
          } else {
                      
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar cancelamento");
                alert.setHeaderText(null);
                alert.setContentText("Confirmar cancelamento da reserva?");
          
          

          Optional<ButtonType> response = alert.showAndWait();

          if (response.get() == ButtonType.OK) {
            txtDel = txtDelete.getText();
            String str = "DELETE FROM RESERVA WHERE id = '" + txtDel + "'";
                    

            if (databaseHandler.execAction(str)) {
              Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
              alert1.setTitle("Successo!!");
              alert1.setHeaderText(null);
              alert1.setContentText("A sua reserva " + txtDel + " foi cancelada com sucesso.");
              alert1.showAndWait();

            } else {
              Alert alert2 = new Alert(Alert.AlertType.ERROR);
              alert2.setTitle("Erro.");
              alert2.setHeaderText(null);
              alert2.setContentText("Ocorreu um erro na operação");
              alert2.showAndWait();
            }
          } else {
            Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
            alert3.setTitle("Cancelado.");
            alert3.setHeaderText(null);
            alert3.setContentText("Operação cancelada.");
            alert3.showAndWait();
          }      
                  }
                
          }
                if(!flag) {  // SE O CODIGO ACIMA FOI CORRIDO COM ERRO, QUER DIZER QUE NAO HAVIA RESERVA, LOGO ESTE "IF" É ATIVADO E LEVANTA UMA MENSAGEM DE ERRO
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Erro.");
                    alert2.setHeaderText(null);
                    alert2.setContentText("Nao existe tal reserva.");
                    alert2.showAndWait();
                } 
                } catch (SQLException ex) {
                  Logger.getLogger(CancelamentoReservas.class.getName()).log(Level.SEVERE, null, ex);
                }
        });
        
        stage.setScene(scene);
        stage.show();
    }
      }

