/**Classe que contem a interface principal da Administraçao, aqui pode escolher entre varias opçoes como ver o relatorio financeiro ou controlar reservas etc...

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */

package Administraçao;

import java.util.Locale;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projeto_poo.Main;
 
public class Administraçao extends Application {
 
    private Stage stage;
    
 
    public static void main(String[] args) {
        Locale.setDefault(Locale.UK);                  
        launch(args);
    }
 
    /** Método para chamar os métodos de criaçao de Scene..
     * @param stage - chama a stage.
        */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Administração");
        initUI();
        stage.show();
    }
 
    /** Método para criar a Scene.

        */
    private void initUI() {
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
        
        Button btnDate = new Button("Reservas"); // CRIAÇAO DE BOTOES PARA A SCENE.
        Button btnFuncionario = new Button("Reg. Funcionarios");
        Button btnImoveis = new Button("Reg. Imoveis");
        Button btnProprietarios = new Button("Reg. Proprietários");
        Button btnVoltar = new Button("Voltar");
        Button btnRela = new Button("Relatorio Financeiro");
        
        
        GridPane gridPane = new GridPane(); // UMA GRIDPANE PARA COLOCAR OS BOTOES.
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        gridPane.add(btnDate, 0, 0);
        gridPane.add(btnFuncionario, 0, 2);
        gridPane.add(btnImoveis, 0, 4);
        gridPane.add(btnProprietarios, 0, 6);
        gridPane.add(btnRela, 0, 8);
        gridPane.add(btnVoltar, 0, 10);        
        vbox.getChildren().add(gridPane);
      
        //AÇOES PARA CADA BOTAO . QUANDO SE CARREGA NELES ELES EXECUTAM UM CERTO EVENT.
        btnDate.setOnAction((ActionEvent event) -> {
            ReservasAdministraçao data = new ReservasAdministraçao();
            data.start(stage);
        });
        
        btnFuncionario.setOnAction((ActionEvent event) -> {
            Funcionarios func = new Funcionarios();
            func.start(stage);
        });
        
        btnImoveis.setOnAction((ActionEvent event) -> {
            AddImoveis imoveis = new AddImoveis();
            imoveis.start(stage);
        });
        
        btnVoltar.setOnAction((ActionEvent event) -> {
            Main main = new Main();
            main.start(stage);
        });
        
        btnProprietarios.setOnAction((ActionEvent event) -> {
            Proprietarios props = new Proprietarios();
            props.start(stage);
        });
        
        btnRela.setOnAction((ActionEvent event) -> {
            RelatorioAdministraçao teste = new RelatorioAdministraçao();
            teste.start(stage);
        });
    
    }
}
