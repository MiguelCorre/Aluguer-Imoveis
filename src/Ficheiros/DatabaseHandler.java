/**Classe onde se encontra a Base de Dados, é aqui que se cria a interface onde se guarda toda a informaçao do projeto, como por exemplo
 * a informaçao das reservas, das propriedades, dos funcionarios etc...

 * @author Miguel Correia, Rodrigo Nogueira, Gonçalo Amaral

 * @version 1.01

 */
package Ficheiros;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class DatabaseHandler {
    public static DatabaseHandler handler;
    
    private static String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    
    public DatabaseHandler() {
        createConnection();
        setupProprietarioTable();
        setupCasaTable();
        setupFuncionarioTable();
        setupReservaTable();
        
    }
    
    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }
    
    
    
    
    void createConnection () {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        finally {
        }
        return result;
    }

    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        }
        finally {
        }
    }
    
    void setupProprietarioTable() {
        String TABLE_NAME = "PROPRIETARIO";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists. Ready to go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "(" 
                            + "     nome varchar(200) primary key, \n"
                            + "     contacto varchar(200)"
                            + "  )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " ... setupDatabase");
        } finally {           
        }
    }
    
    void setupCasaTable() {
        String TABLE_NAME = "CASA";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists. Ready to go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "(" 
                            + "     lote varchar(200) primary key, \n"
                            + "     rua varchar(200), \n"
                            + "     tipo varchar(200), \n"
                            + "     quartos varchar(200), \n"
                            + "     preco varchar(200), \n"
                            + "     nome varchar(200), \n"
                            + "     avail boolean default true, \n"
                            + "     FOREIGN KEY (nome) REFERENCES PROPRIETARIO(nome)"
                            + "  )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " ... setupDatabase");
        } finally {           
        }
    }
    
    void setupFuncionarioTable() {
        String TABLE_NAME = "FUNCIONARIO";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists. Ready to go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "(" 
                            + "     id varchar(200) primary key, \n"
                            + "     nome varchar(200), \n"
                            + "     funcao varchar(200), \n"
                            + "     contacto varchar(200), \n"
                            + "     carro varchar(200) unique, \n"
                            + "     avail boolean default true"
                            + "  )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " ... setupDatabase");
        } finally {           
        }
    }
    
    
    void setupReservaTable() {
        String TABLE_NAME = "RESERVA";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists. Ready to go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "(" 
                            + "     lote varchar(200) , \n"
                            + "     id varchar(200), \n"
                            + "     data_in date, \n"
                            + "     data_out date, \n"
                            + "     nomeCli varchar(200), \n"
                            + "     contacto varchar(200), \n"
                            + "     carro varchar(200), \n"
                            + "     FOREIGN KEY (lote) REFERENCES CASA(lote), \n"
                            + "     FOREIGN KEY (carro) REFERENCES FUNCIONARIO(carro)"
                            + "  )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " ... setupDatabase");
        } finally {           
        }
    }
}