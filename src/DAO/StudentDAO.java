/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Student;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 *
 * @author Mustafa Khaled
 */
public class StudentDAO {

    private final String getAll_stmt = "Select * From users";
    private final String getUser_stmt = "Select * From users where id=? and name=?";
    private final String attendance_stmt = "insert into attendance values( ? , ? )";

    public boolean attendance(int id) {
        try (PreparedStatement pstmt = DBConnection.getCon().prepareStatement(attendance_stmt)) {
            Date d = new Date(System.currentTimeMillis());
            SimpleDateFormat df = new SimpleDateFormat("DD/MM/YYYY");
            pstmt.setInt(1, id);
            pstmt.setString(2, df.format(d));
            pstmt.execute();
        } catch (SQLException ex) {
            if (ex.getErrorCode() == -104) {
                return false;
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public static void createAttendanceTable() {
//        String table="drop table attendance";
        String table = "create table attendance ("
                + "     ID integer not null,"
                + "     day varchar(11) not null,"
                + "     CONSTRAINT physician_FK_ID"
                + "     FOREIGN KEY (ID)"
                + "     REFERENCES users (ID) ON DELETE CASCADE,"
                + "     primary key (ID, day)"
                + ")";
        try (PreparedStatement pstmt = DBConnection.getCon().prepareStatement(table)) {
            pstmt.execute();
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ObservableList<Student> getAllUSers() {
        ObservableList<Student> all = FXCollections.observableArrayList();
        try (PreparedStatement pstmt = DBConnection.getCon().prepareStatement(getAll_stmt)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    all.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                }
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return all;
    }

    public Student getUser(int id, String name) {
        Student s = null;
        try (PreparedStatement pstmt = DBConnection.getCon().prepareStatement(getUser_stmt)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    s=new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public static void main(String[] args) {
//        createAttendanceTable();
//        System.out.println(new StudentDAO().getUser(1, "Mustafa Khaled"));
    }
}
