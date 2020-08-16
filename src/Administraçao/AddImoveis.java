/**Classe para a Administraçao adicionar lotes à sua empresa para os seus clientes poderem reservas,
 * Contem a informaçao toda da propriedade como nº quartos e preço por noite.

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */
package Administraçao;

import Ficheiros.DatabaseHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
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

public class AddImoveis extends Application {
    private TextField txtLote;
    private TextField txtRua;
    private TextField txtTipo;
    private TextField txtQuarto;
    private TextField txtPreco;
    private TextField txtProprietario;
    DatabaseHandler databaseHandler;
    private Stage stage;
    private Button btnRegisto;
    private Button btnVoltar;
    private String nome;

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
        txtLote.clear();
        txtRua.clear();
        txtTipo.clear();
        txtQuarto.clear();
        txtPreco.clear();
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
        Label lblote = new Label("Lote");
        txtLote = new TextField();
        Label lbrua = new Label("Rua");
        txtRua = new TextField();
        Label lbtipo = new Label("Tipo");
        txtTipo = new TextField();
        Label lbquarto = new Label("Nr. Quartos");
        txtQuarto = new TextField();
        Label lbpreco = new Label("Preço");
        txtPreco = new TextField();
        Label lbproprietario = new Label("Proprietário");
        
        String qu1 = "SELECT nome FROM PROPRIETARIO";
        ResultSet r2 = databaseHandler.execQuery(qu1);
        ComboBox combobox = new ComboBox(); 
        combobox.setEditable(true); // COMBO BOX
        try {              
            while (r2.next()) {
                nome = r2.getString("nome");  
                combobox.getItems().add(nome);
                }              
                } catch (SQLException ex) {
                  Logger.getLogger(ReservasAdministraçao.class.getName()).log(Level.SEVERE, null, ex);
                } 
               
        btnRegisto = new Button("Registar");
        btnVoltar = new Button("Voltar");
        final Label lblMessage = new Label();
        
        //Adding Nodes to GridPane layout
        gridPane.add(lblote, 0, 0);
        gridPane.add(txtLote, 1, 0);
        gridPane.add(lbrua, 0, 1);
        gridPane.add(txtRua, 1, 1);
        gridPane.add(lbtipo, 0, 2);
        gridPane.add(txtTipo, 1, 2);
        gridPane.add(lbquarto, 0, 3);
        gridPane.add(txtQuarto, 1, 3);
        gridPane.add(lbpreco, 0, 4);
        gridPane.add(txtPreco, 1, 4);
        gridPane.add(lbproprietario, 0, 6);
        gridPane.add(combobox, 1, 6);
         
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
            String casaLote = txtLote.getText();
            String casaRua = txtRua.getText();
            String casaTipo = txtTipo.getText();
            String casaQuarto = txtQuarto.getText();
            String casaPreco = txtPreco.getText();
            String casaProprietario = combobox.getEditor().getText();

            if (casaLote.isEmpty() || casaRua.isEmpty() || casaTipo.isEmpty() || casaQuarto.isEmpty() || casaPreco.isEmpty() || casaProprietario.isEmpty()) {
                Alert alert = new Alert (Alert.AlertType.ERROR); // SE ALGUM CAMPO ACIMA ESTIVER EM BRANCO DA ERRO.
                alert.setHeaderText(null);
                alert.setContentText("Por favor preencha todos os espaços.");
                alert.showAndWait();
                return;
            }
            
            
            
            //int savedValue = Integer.parseInt();
            
            
            String qu = "INSERT INTO CASA VALUES (" +
                    "'" + casaLote + "'," +
                    "'" + casaRua + "'," +
                    "'" + casaTipo + "'," +
                    "'" + casaQuarto + "'," +
                    "'" + casaPreco + "'," +
                    "'" + casaProprietario + "'," +
                    "" + true + ")";


            if(databaseHandler.execAction(qu)) { // SE EXECUTAR O INSERT CORRETAMENTE PROCEDE COM SUCESSO.
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
