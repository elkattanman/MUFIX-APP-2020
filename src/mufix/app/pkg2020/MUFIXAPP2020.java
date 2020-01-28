/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mufix.app.pkg2020;

import Controller.ScreensController;
import Model.QR;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Mustafa Khaled
 */
public class MUFIXAPP2020 extends Application {
    
    public static String MainID = "main";
    public static String MainFile = "/View/MAIN.fxml";
    public static String MangerID = "DataManger";
    public static String MangerFile = "/View/DataManger.fxml";
    
    @Override
    public void start(Stage stage) throws Exception {
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(MUFIXAPP2020.MainID, MUFIXAPP2020.MainFile);
        mainContainer.loadScreen(MUFIXAPP2020.MangerID, MUFIXAPP2020.MangerFile);
        mainContainer.setScreen(MainID);
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("MUFIX APP");
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
