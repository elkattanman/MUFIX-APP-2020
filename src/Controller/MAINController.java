/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javax.swing.SwingUtilities;

/**
 * FXML Controller class
 *
 * @author Mustafa Khaled
 */
public class MAINController implements Initializable , Runnable , ThreadFactory,ControlledScreen {
    ScreensController myController;
    private Webcam webcam = null;
    private Executor executor = Executors.newSingleThreadExecutor(this);
    @FXML
    private Pane camiraPane;
    @FXML
    private JFXTextField nameTF, phoneTF, trackTF, statusTF;

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
                WebcamPanel panel= new WebcamPanel(webcam);
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
                Thread.sleep(100);
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
                String s=result.getText();
                System.out.println("QR : "+s);   
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

}
