package Model;

import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;


/**
 *
 * @author Mustafa Khaled
 */
public class DES {

    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    private KeySpec mykeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    byte[] keyAsByte;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;

    public DES(String myEncKey) throws Exception {
        myEncryptionKey = myEncKey;
        myEncryptionScheme=DES_ENCRYPTION_SCHEME;
        keyAsByte=myEncryptionKey.getBytes(UNICODE_FORMAT);
        mykeySpec = new DESKeySpec(keyAsByte);
        mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher= Cipher.getInstance(myEncryptionScheme);
        key=mySecretKeyFactory.generateSecret(mykeySpec);
    }
    
    
    public String ENCRYPT(String unencryptedString) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] plainText= unencryptedString.getBytes(UNICODE_FORMAT);
        byte[] cipherText= cipher.doFinal(plainText);
        String encreptedString = Base64.getEncoder().encodeToString(cipherText);
        return encreptedString;
    }

    public String DECRYPT(String encreptedString) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedText = Base64.getDecoder().decode(encreptedString);
        byte[] plainText = cipher.doFinal(encryptedText);
        String decrptedText = bytes2String(plainText);
        return decrptedText;
    }


    private String bytes2String(byte[] bytes) {
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append((char)bytes[i]);
        }
        return sb.toString();
    }
    public static void main(String[] args) throws Exception {
        Student st=new Student();
        st.ToObj("MUFIX{1-Mustafa Khaled-01121751050-java}");
        DES d=new DES("Mufix.org");
        System.out.println(d.ENCRYPT(st.toString()));
        System.out.println(d.DECRYPT(d.ENCRYPT(st.toString())));
    }
}
