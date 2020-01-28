/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.StudentDAO;
import Model.DES;
import Model.QR;
import Model.Student;
import com.google.zxing.WriterException;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableColumn;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import mufix.app.pkg2020.MUFIXAPP2020;

/**
 * FXML Controller class
 *
 * @author Mustafa Khaled
 */
public class DataMangerController implements Initializable, ControlledScreen {

    ScreensController myController;
    @FXML
    private JFXTreeTableView<Student> table;
    @FXML
    private TreeTableColumn<Student, String> nameCol, phoneCol, trackCol;
    @FXML
    private JFXTextField searchTF, nameTF, phoneTF, trackTF, idTF;

    ObservableList<Student> list;
    @FXML
    private TreeTableColumn<Student, Integer> idCol;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("phone"));
        trackCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("track"));

        /*
        nameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) -> param.getValue().getValue().name);
        phoneCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) -> param.getValue().getValue().phone);
        trackCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) -> param.getValue().getValue().track);
         */
        list = new StudentDAO().getAllUSers();

        TreeItem<Student> root = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
        table.setRoot(root);
        table.setShowRoot(false);
        table.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<Student>> observable, TreeItem<Student> oldValue, TreeItem<Student> newValue) -> {
            showDetails(newValue);
        });
        searchTF.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            table.setPredicate((TreeItem<Student> t) -> t.getValue().getName().contains(newValue) | t.getValue().getTrack().contains(newValue) | t.getValue().getPhone().contains(newValue));
        });

    }

    public void showDetails(TreeItem<Student> treeItem) {
        idTF.setText("" + treeItem.getValue().getId());
        nameTF.setText(treeItem.getValue().getName());
        phoneTF.setText(treeItem.getValue().getPhone());
        trackTF.setText(treeItem.getValue().getTrack());
    }

    @FXML
    private void addAction(ActionEvent event) {
        Student st=new Student(0, nameTF.getText(), phoneTF.getText(), trackTF.getText());
        new StudentDAO().insertStudent(st);
        list.addAll(new Student(st.getId() , st.getName(), st.getPhone(), st.getTrack()));
    }

    @FXML
    private void printAction(ActionEvent event) {
        try {
            Student st = new Student(Integer.parseInt(idTF.getText()), nameTF.getText(), phoneTF.getText(), trackTF.getText());
            new QR().generateQRCodeImage(st, 320, 320, QR.QR_CODE_IMAGE_PATH+st.toString() + ".png");
        } catch (WriterException | IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            System.err.println("Error in Print \n ex: " + ex.getMessage());
            Logger.getLogger(DataMangerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DataMangerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void printAllAction(ActionEvent event) {
        list.forEach((Student s) -> {
            try {
                DES des = new DES("Mufix.org");
                new QR().generateQRCodeImage(s, 300, 300, QR.QR_CODE_IMAGE_PATH + s.toString() + ".png");
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Look, an Error Dialog");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
                System.err.println("Error in PrintALL \n ex : " + ex.getMessage());
                Logger.getLogger(DataMangerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    @FXML
    private void clearAction(ActionEvent event) {
        idTF.clear();
        nameTF.clear();
        phoneTF.clear();
        trackTF.clear();
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @FXML
    private void RightAction(MouseEvent event) {
        myController.setScreen(MUFIXAPP2020.MainID);
    }

    @FXML
    private void LeftAction(MouseEvent event) {
    }

}
