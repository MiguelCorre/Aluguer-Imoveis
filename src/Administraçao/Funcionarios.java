/**Classe para a Administraçao registar informaçao dos seus funcionarios na base de dados.

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
import javafx.scene.control.ComboBox;
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


public class Funcionarios extends Application {
    private TextField txtID;
    private TextField txtNome;
    private TextField txtContacto;
    DatabaseHandler databaseHandler;
    private Stage stage;
    private Button btnRegisto;
    private Button btnVoltar;
    ComboBox combobox;
    ComboBox combobox2;
        
    
    
    /** Método para verificar o conteudo dentro da base de dados, para fins de testes.
     * 
        */
    private void checkData() {
        String qu = "SELECT * FROM FUNCIONARIO";
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String idx = rs.getString("id");
                String nomex = rs.getString("nome");
                String funcaox = rs.getString("funcao");
                System.out.println(idx);
                System.out.println(nomex);
                System.out.println(funcaox);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /** Método para apagar os campos no fim de cada registo.
     * 
        */
    public void clearFields() {
        txtID.clear();
        txtNome.clear();
        txtContacto.clear();
        //combobox.getItems().clear();
        //combobox2.getItems().clear(); 
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
        Label lbid = new Label("ID");
        txtID = new TextField();
        Label lbnome = new Label("Nome");
        txtNome = new TextField();
        Label lbfuncao = new Label("Funcao");
        Label lbcontacto = new Label("Contacto");
        Label lbcarro = new Label("Carro");
        txtContacto = new TextField();
        btnRegisto = new Button("Registar");
        btnVoltar = new Button("Voltar");
        final Label lblMessage = new Label();
        
        combobox = new ComboBox(); 
        combobox.setEditable(true); // COMBO BOX
        combobox.getItems().addAll("Motorista", 
                "Limpeza",
                "Receçao");
        
        combobox2 = new ComboBox(); 
        combobox2.setEditable(true); // COMBO BOX
        combobox2.getItems().addAll("Carro 4 portas", 
                "Carrinha 7 portas");
        
        //Adding Nodes to GridPane layout
        gridPane.add(lbid, 0, 0);
        gridPane.add(txtID, 1, 0);
        gridPane.add(lbnome, 0, 1);
        gridPane.add(txtNome, 1, 1);
        gridPane.add(lbfuncao, 0, 2);
        gridPane.add(combobox, 1, 2);
        gridPane.add(lbcontacto, 0, 3);
        gridPane.add(txtContacto, 1, 3);
        gridPane.add(lbcarro, 0, 4);
        gridPane.add(combobox2, 1, 4);
         
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
        Text text = new Text("Registo Funcionarios");
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
            String funcID = txtID.getText();
            String funcNome = txtNome.getText();
            String funcCarro = combobox2.getEditor().getText();
            String funcContacto = txtContacto.getText();
            String funcFuncao = combobox.getEditor().getText();

            if (funcID.isEmpty() || funcNome.isEmpty() || funcFuncao.isEmpty() || funcContacto.isEmpty()) {
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Por favor preencha todos os espaços.");
                alert.showAndWait();
                return;
            }
            
            
            
            //int savedValue = Integer.parseInt();
            
            if (funcCarro.isEmpty()) {
                String qu = "INSERT INTO FUNCIONARIO(id, nome, funcao, contacto) VALUES (" +
                    "'" + funcID + "'," +
                    "'" + funcNome + "'," +
                    "'" + funcFuncao + "'," +
                    "'" + funcContacto + "')"; 

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
                
            } else {
            String qu = "INSERT INTO FUNCIONARIO VALUES (" +
                    "'" + funcID + "'," +
                    "'" + funcNome + "'," +
                    "'" + funcFuncao + "'," +
                    "'" + funcContacto + "'," +
                    "'" + funcCarro + "'," +
                    "" + true + ")";

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
     //scene.getStylesheets().add(getClass().getClassLoader().getResource("login.css").toExternalForm());
     stage.setScene(scene);
       
     //primaryStage.setResizable(false);
     //stage.show();
        stage.show();
                }
    
    public static void main(String[] args) {
        launch(args);
    }
}

