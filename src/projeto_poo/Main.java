/**Classe Main, quando o programa abre esta é a primeira coisa que se ve, tem a opçao de seguir para a area do Cliente 
 * e para a area da Administraçao/Proprietario.

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */
package projeto_poo;

import AluguerImovel.AluguerImoveisClientes;
import Ficheiros.DatabaseHandler;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    
     /** Método para criaçao da Scene.

        */
    
    @Override
    public void start(Stage primaryStage) {
        
        new Thread (() -> {
            DatabaseHandler.getInstance();
        }).start();
        
        DropShadow shadow = new DropShadow();
        
        Button button = new Button();
        button.setText("Administração");
        button.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        button.setLayoutX(300);
        button.setLayoutY(384);     
        
        button.setOnAction((ActionEvent event) -> {
            Login login = new Login();
            login.start(primaryStage);         
        });     
        
        Button button2 = new Button();
        button2.setText("Aluguer Imoveis");
        button2.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        button2.setLayoutX(574);
        button2.setLayoutY(384);
        
        button2.setOnAction((ActionEvent event) -> {
            AluguerImoveisClientes cliente = new AluguerImoveisClientes();
            cliente.start(primaryStage);         
        }); 
        
        
        
        
        
///////////////////////////////////////////////////////////////////////////////////////////
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, 
            new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                button.setEffect(shadow);
                }
            });
              
        button2.addEventHandler(MouseEvent.MOUSE_ENTERED, 
            new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                button2.setEffect(shadow);
                }
            });
        
//Removing the shadow when the mouse cursor is off
        button.addEventHandler(MouseEvent.MOUSE_EXITED, 
            new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                button.setEffect(null);
}
});
        
        button2.addEventHandler(MouseEvent.MOUSE_EXITED, 
            new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                button2.setEffect(null);
}
            });
///////////////////////////////////////////////////////////////////////////////
        
        Pane root = new Pane();
        
        root.getChildren().add(button);
        root.getChildren().add(button2);
        
        Scene scene = new Scene(root, 1024, 768);
              
        primaryStage.setTitle("Alojamento Local");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }    
}
        

    

