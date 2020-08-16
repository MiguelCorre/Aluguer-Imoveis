/**Classe onde se encontra o Login, bloqueado por um "username" e "password" para dar acesso à Administraçao ou a interface dos Proprietarios.

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */
package projeto_poo;

import Administraçao.Administraçao;
import Ficheiros.DatabaseHandler;
import Proprietario.Proprietário;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 

public class Login extends Application {
 
 String user = "j";
 String user2 = "a";
 String user3 = "b";
 String pw = "p";
 String pw2 = "a";
 String pw3 = "b";
 String checkUser, checkPw;
 public String twste;
 Boolean check = false;
 DatabaseHandler databaseHandler;
 
     /** Método para criaçao da Scene.

        */
    @Override
    public void start(Stage primaryStage) {
        databaseHandler = DatabaseHandler.getInstance();
        primaryStage.setTitle("Login");
        
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
        Label lblUserName = new Label("Username");
        final TextField txtUserName = new TextField();
        Label lblPassword = new Label("Password");
        final PasswordField pf = new PasswordField();
        Button btnLogin = new Button("Login");
        final Label lblMessage = new Label();
        Button btnVoltar = new Button("Voltar");
        
        
        //Adding Nodes to GridPane layout
        gridPane.add(lblUserName, 0, 0);
        gridPane.add(txtUserName, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(pf, 1, 1);
        gridPane.add(btnLogin, 2, 1);
        gridPane.add(btnVoltar, 2, 2);
        gridPane.add(lblMessage, 1, 2);
        
                
        //Reflection for gridPane
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPane.setEffect(r);
        
        //DropShadow effect 
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
        
        //Adding text and DropShadow effect to it
        Text text = new Text("Login");
        text.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 28));
        text.setEffect(dropShadow);
        
        //Adding text to HBox
        hb.getChildren().add(text);
                          
        //Add ID's to Nodes
        bp.setId("bp");
        gridPane.setId("root");
        btnLogin.setId("btnLogin");
        text.setId("text");
        
        //Action for btnLogin
        
        btnLogin.setOnAction((ActionEvent event) -> {
            checkUser = txtUserName.getText();
            checkPw = pf.getText();
            if(checkUser.equals(user) && checkPw.equals(pw)){
                check = true;
                Administraçao aic = new Administraçao();
                aic.start(primaryStage);
            }
            if(checkUser.equals(user2) && checkPw.equals(pw2)) {
                check = true;
                Proprietário pro = new Proprietário(1,null,null);
                pro.start(primaryStage);
            }
            if(checkUser.equals(user3) && checkPw.equals(pw3)) {
                check = true;
                Proprietário aicc = new Proprietário(2,null,null);
                aicc.start(primaryStage);
            }
            else{
                lblMessage.setText("Incorrect user or pw.");
                lblMessage.setTextFill(Color.RED);
            }
            txtUserName.setText("");
            pf.setText("");
        });
        btnVoltar.setOnAction((ActionEvent event) -> {
            Main m = new Main();
        });
        
        txtUserName.setOnAction((ActionEvent event) -> {
            checkUser = txtUserName.getText();
            checkPw = pf.getText();
            if(checkUser.equals(user) && checkPw.equals(pw)){
                check = true;
                Administraçao aic = new Administraçao();
                aic.start(primaryStage);
            }
            if(checkUser.equals(user2) && checkPw.equals(pw2)) {
                check = true;
                Proprietário pro = new Proprietário(1,null,null);
                pro.start(primaryStage);
            }
            if(checkUser.equals(user3) && checkPw.equals(pw3)) {
                check = true;
                Proprietário aicc = new Proprietário(2,null,null);
                aicc.start(primaryStage);
            }
            else{
                lblMessage.setText("Incorrect user or pw.");
                lblMessage.setTextFill(Color.RED);
            }
            txtUserName.setText("");
            pf.setText("");
        });
        
        pf.setOnAction((ActionEvent event) -> {
            checkUser = txtUserName.getText();
            checkPw = pf.getText();
            if(checkUser.equals(user) && checkPw.equals(pw)){
                check = true;
                Administraçao aic = new Administraçao();
                aic.start(primaryStage);
            }
            if(checkUser.equals(user2) && checkPw.equals(pw2)) {
                check = true;
                Proprietário pro = new Proprietário(1,null,null);
                pro.start(primaryStage);
            }
            if(checkUser.equals(user3) && checkPw.equals(pw3)) {
                check = true;
                Proprietário aicc = new Proprietário(2,null,null);
                aicc.start(primaryStage);
            }
            else{
                lblMessage.setText("Incorrect user or pw.");
                lblMessage.setTextFill(Color.RED);
            }
            txtUserName.setText("");
            pf.setText("");
        });
       
        //Add HBox and GridPane layout to BorderPane Layout
        bp.setTop(hb);
        bp.setCenter(gridPane);  
        
        //Adding BorderPane to the scene and loading CSS
     Scene scene = new Scene(bp);
     
     primaryStage.setScene(scene);
       
     
     primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
    

