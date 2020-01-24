/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.StudentDAO;
import Model.Student;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.jfoenix.controls.JFXTextField;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javax.swing.SwingUtilities;
import mufix.app.pkg2020.MUFIXAPP2020;

/**
 * FXML Controller class
 *
 * @author Mustafa Khaled
 */
public class MAINController implements Initializable, Runnable, ThreadFactory, ControlledScreen {

    ScreensController myController;
    private Webcam webcam = null;
    private Executor executor = Executors.newSingleThreadExecutor(this);
    @FXML
    private Pane camiraPane;
    @FXML
    private JFXTextField nameTF, phoneTF, trackTF, statusTF;

    Student st = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webcam = Webcam.getWebcams().get(0);
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);
        camiraPane.getChildren().add(swingNode);
        executor.execute(this);

    }

    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dimension size = new Dimension(640, 480);
                webcam.setViewSize(size);
                WebcamPanel panel = new WebcamPanel(webcam);
                panel.setPreferredSize(size);
                panel.setFPSDisplayed(true);
                swingNode.setContent(panel);
            }
        });
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {

                if ((image = webcam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    // fall thru, it means there is no QR code in image
                }
            }
            if (result != null) {
                String s = result.getText();
                st = new Student();
                if (s.matches("MUFIX(.*)") && st.ToObj(s)) {
                    System.out.println("QR : " + s);
                    nameTF.setText(st.getName());
                    phoneTF.setText(st.getPhone());
                    trackTF.setText(st.getTrack());
                    st=new StudentDAO().getUser(st.getId(), st.getName());
                    if ( st!= null) {
                        statusTF.setStyle("-fx-font-size: 30px;-fx-text-fill : Green");
                        statusTF.setText("User Found!");
                    } else {
                        statusTF.setStyle("-fx-font-size: 30px;-fx-text-fill : RED");
                        statusTF.setText("User Not Found!");
                    }
                } else {
                    statusTF.setStyle("-fx-text-fill : red");
                    statusTF.setText("Data Not Efficient");
                }
            }
        } while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "Scanner-runner");
        t.setDaemon(true);
        return t;
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @FXML
    private void RegisterAction(ActionEvent event) {
        if(st==null){
            statusTF.setStyle("-fx-font-size: 25px;-fx-text-fill : RED");
            statusTF.setText("No Such Object!");
            return;
        }
        if (!new StudentDAO().attendance(st)) {
            statusTF.setStyle("-fx-font-size: 25px;-fx-text-fill : RED");
            statusTF.setText("Already Registered!");
        }else{
            statusTF.setStyle("-fx-font-size: 30px;-fx-text-fill : Green");
            statusTF.setText("OK!");
        }
    }

    @FXML
    private void CancelAction(ActionEvent event) {
        st=null;
        nameTF.clear();
        phoneTF.clear();
        trackTF.clear();
        statusTF.clear();
    }

    @FXML
    private void RightAction(MouseEvent event) {
    }

    @FXML
    private void LeftAction(MouseEvent event) {
        myController.setScreen(MUFIXAPP2020.MangerID);
    }
    

}
