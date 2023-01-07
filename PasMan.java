package pasman;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class PasMan {

    Cipher cipher;
    SecureRandom sran;
    byte[] prod, salt = new byte[16];

    public PasMan() {
        sran = new SecureRandom();
    }

    public void reSalt() {
        sran.nextBytes(salt);
    }
    
    public void setSalt(byte[] salt){
    this.salt = salt;
    }

    public byte[] generateHash(char[] maspas) {
        try {
            SecretKeyFactory s = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(maspas, salt, 65536, 256);
            return s.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(PasMan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasMan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void encrypt(String pas, String masPas) {
        SecretKeySpec key;
        reSalt();
        try {
            cipher = Cipher.getInstance("AES");
            key = new SecretKeySpec(generateHash(masPas.toCharArray()), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            prod = cipher.doFinal(pas.getBytes());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException| InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            System.out.println("Error: No Such Padding/Algorithm, Invalid Key, Bad Padding or Illegal Block Size Exception");
        }
    }

    public void decrypt(String masPas, byte[] p, byte[] s) {
        SecretKeySpec key;
        try {
            cipher = Cipher.getInstance("AES");
            salt = s;
            key = new SecretKeySpec(generateHash(masPas.toCharArray()), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            prod = cipher.doFinal(p);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            System.out.println("Error: No Such Algorithm/Padding, Invalid Key, Bad Padding or Illegal Block Size Exception");
        }
    }

    public Password getPassword(String tag) {
        return new Password(tag, prod, salt);
    }
    
    public Password getPassword(byte[] masPas) {
        return new Password("UserPassword", masPas, salt);
    }

    public void clear() {
        prod = null;
    }
}
