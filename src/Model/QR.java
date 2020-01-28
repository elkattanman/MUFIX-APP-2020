/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Mustafa Khaled
 */
public class QR {

    public static final String QR_CODE_IMAGE_PATH = "./MyQRCodes/";
    public static final String QR_CODE_LOGO_PATH = "./QR_CODE_LOGO_PATH.png";
    public static final String TEMP_PATH = "./temp.png";

    public void generateQRCodeImage(Student st,int width, int height, String filePath)
            throws WriterException, IOException, Exception {
        DES des=new DES("Mufix.org");
        String text=st.toString();
        text= des.ENCRYPT(text);
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        // Create new configuration that specifies the error correction
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        // Create a qr code with the url as content and a size of WxH px
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        //Path path = FileSystems.getDefault().getPath(filePath);
        // Load QR image
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());
        // Load logo image
        BufferedImage overly = ImageIO.read(new FileInputStream(QR_CODE_LOGO_PATH));

        // Calculate the delta height and width between QR code and logo
        int deltaHeight = qrImage.getHeight() - overly.getHeight();
        int deltaWidth = qrImage.getWidth() - overly.getWidth();

        // Initialize combined image
        BufferedImage combined = ImageIO.read(new FileInputStream(TEMP_PATH));
        Graphics2D g = (Graphics2D) combined.getGraphics();

        // Write QR code to new image at position 0/0
        g.drawImage(qrImage, 35, 130, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Write logo into combine image at position (deltaWidth / 2) and
        // (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
        // the same space for the logo to be centered
        g.drawImage(overly, 35+(int) Math.round(deltaWidth / 2), 130+(int) Math.round(deltaHeight / 2), null);

        Font font = new Font("Comfortaa Bold", Font.PLAIN, 35);
        g.setFont(font);
        g.setColor(new Color(0x9e0b0f));
        g.drawString(st.getName(), 365,307);
        g.drawString(st.getTrack(), 487,362);
        //write comb here 
        //MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        ImageIO.write(combined, "png", new File(filePath));
    }

    private MatrixToImageConfig getMatrixConfig() {
        // ARGB Colors
        // Check Colors ENUM
        return new MatrixToImageConfig(QR.Colors.BLACK.getArgb(), QR.Colors.WHITE.getArgb());
    }

    public enum Colors {
        BLUE(0xFF40BAD0),
        RED(0xFFE91C43),
        PURPLE(0xFF8A4F9E),
        ORANGE(0xFFF4B13D),
        WHITE(0xFFFFFFFF),
        BLACK(0xFF000000);

        private final int argb;

        Colors(final int argb) {
            this.argb = argb;
        }

        public int getArgb() {
            return argb;
        }
    }

    public static void main(String[] args) {
        try {
            Student st=new Student();
            st.ToObj("MUFIX{1-Mustafa Khaled-0111111-Java}");
            new QR().generateQRCodeImage(st , 300, 300, QR_CODE_IMAGE_PATH + "Mustafa.png");
        } catch (WriterException | IOException ex) {
            Logger.getLogger(QR.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(QR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
