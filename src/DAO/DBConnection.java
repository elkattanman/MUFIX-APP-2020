/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
 * @author Mustafa Khaled
 */
public class DBConnection {
    
    public static Connection conn=null;
    public static Connection getCon(){
        if(conn!=null) return conn;
        try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://./usersDB.mdb");
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            System.err.println("Error in connection \n ex: "+ex.getMessage());
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
}
