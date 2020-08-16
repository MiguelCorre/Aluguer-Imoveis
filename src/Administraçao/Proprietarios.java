/**Classe para a Administraçao registar Proprietarios à base de dados, guardando o seu nome e contacto.

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */
package Administraçao;

import Ficheiros.DatabaseHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Proprietarios extends Application {
    
    private TextField txtNome;
    private TextField txtContacto;

    DatabaseHandler databaseHandler;
    private Stage stage;
    private Button btnRegisto;
    private Button btnVoltar;
   
        
    
    
    /** Método para verificar o conteudo dentro da base de dados, para fins de testes.
     * 
        */
    private void checkData() {
        String qu = "SELECT * FROM CASA";
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String lotex = rs.getString("lote");
                String ruax = rs.getString("rua");
                String availx = rs.getString("avail");
                System.out.println(lotex);
                System.out.println(ruax);
                System.out.println(availx);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /** Método para apagar os campos no fim de cada registo.
     * 
        */
    public void clearFields() {
        txtNome.clear();
        txtContacto.clear();
    }
    
    /** Método para chamar os métodos de criaçao de Scene..
     * @param stage - chama a stage.
        */
    @Override
    public void start(Stage stage) {
        
        databaseHandler = DatabaseHandler.getInstance();
        
        checkData();
        
        this.stage = stage;
        stage.setTitle("Reservas ");
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(50,50,50,50));
        
        //Adding HBox
        HBox hb = new HBox();
        hb.setPadding(new Insets(20,20,20,30));
        
        //Adding GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        
       //Implementing Nodes for GridPane

        Label lbNome = new Label("Nome");
        txtNome = new TextField();
        Label lbContacto = new Label("Contacto");
        txtContacto = new TextField();
        
        btnRegisto = new Button("Registar");
        btnVoltar = new Button("Voltar");
        final Label lblMessage = new Label();
        
        //Adding Nodes to GridPane layout
        gridPane.add(lbNome, 0, 0);
        gridPane.add(txtNome, 1, 0);
        gridPane.add(lbContacto, 0, 1);
        gridPane.add(txtContacto, 1, 1);
         
        gridPane.add(btnRegisto, 3, 1);
        gridPane.add(lblMessage, 1, 2);
        gridPane.add(btnVoltar, 3, 2);
        
                
        //Reflection for gridPane
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPane.setEffect(r);
        
        //DropShadow effect 
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
        
        //Adding text and DropShadow effect to it
        Text text = new Text("Registo Imoveis");
        text.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 28));
        text.setEffect(dropShadow);
        
        //Adding text to HBox
        hb.getChildren().add(text);
                          
        //Add ID's to Nodes
        bp.setId("bp");
        gridPane.setId("root");
        btnRegisto.setId("btnRegisto");
        text.setId("text");
        
        btnRegisto.setOnAction((ActionEvent event) -> {
            String propNome = txtNome.getText();
            String propContacto = txtContacto.getText();


            if (propNome.isEmpty() || propContacto.isEmpty()) {
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Por favor preencha todos os espaços.");
                alert.showAndWait();
                return;
            }
            
            
            
            //int savedValue = Integer.parseInt();
            
            
            String qu = "INSERT INTO PROPRIETARIO VALUES (" +
                    "'" + propNome + "'," +
                    "'" + propContacto + "')" ;

            System.out.println(qu);

            if(databaseHandler.execAction(qu)) {
                Alert alert = new Alert (Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Sucesso!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("A operaçao nao foi bem sucedida.");
                alert.showAndWait();           
            }
            
            clearFields();
    
    });
        
        btnVoltar.setOnAction((ActionEvent event) -> {
            Administraçao aic = new Administraçao();
            aic.start(stage);
        });
        
        bp.setTop(hb);
        bp.setCenter(gridPane);  
        
        
        
        //Adding BorderPane to the scene and loading CSS
     Scene scene = new Scene(bp);
     stage.setScene(scene);
       
     stage.show();
                }
    
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
