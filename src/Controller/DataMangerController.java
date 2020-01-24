/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.StudentDAO;
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
    private JFXTextField searchTF;
    @FXML
    private JFXTextField nameTF;
    @FXML
    private JFXTextField phoneTF;
    @FXML
    private JFXTextField trackTF;

    ObservableList<Student> list;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) -> param.getValue().getValue().name);
        phoneCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) -> param.getValue().getValue().phone);
        trackCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) -> param.getValue().getValue().track);

        list = new StudentDAO().getAllUSers();

        TreeItem<Student> root = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
        table.setRoot(root);
        table.setShowRoot(false);
        table.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<Student>> observable, TreeItem<Student> oldValue, TreeItem<Student> newValue) -> {
            showDetails(newValue);
        });
        searchTF.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            table.setPredicate((TreeItem<Student> t) -> t.getValue().name.getValue().contains(newValue) | t.getValue().track.getValue().contains(newValue) | t.getValue().phone.getValue().contains(newValue));
        });

    }

    public void showDetails(TreeItem<Student> treeItem) {
        nameTF.setText(treeItem.getValue().getName());
        phoneTF.setText(treeItem.getValue().getPhone());
        trackTF.setText(treeItem.getValue().getTrack());
    }

    @FXML
    private void addAction(ActionEvent event) {
        list.addAll(new Student(0, nameTF.getText(), phoneTF.getText(), trackTF.getText()));
    }

    @FXML
    private void printAction(ActionEvent event) {
        try {
            new QR().generateQRCodeImage(new Student(0, nameTF.getText(), phoneTF.getText(), trackTF.getText()).toString(), 300, 300, QR.QR_CODE_IMAGE_PATH);
        } catch (WriterException | IOException ex) {
            System.err.println("Error in Print \n ex: " + ex.getMessage());
            Logger.getLogger(DataMangerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void printAllAction(ActionEvent event) {
        list.forEach((Student s) -> {
            try {
                new QR().generateQRCodeImage(s.toString(), 300, 300, QR.QR_CODE_IMAGE_PATH + s.toString() + ".png");
            } catch (WriterException | IOException ex) {
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
        nameTF.clear();
        phoneTF.clear();
        trackTF.clear();
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

}
