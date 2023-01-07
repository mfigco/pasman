package pasman;

import java.util.Base64;

public class Password {

    private byte[] salt, pas;
    private String tag;

    public Password() {
    }

    public Password(String tag, byte[] pas, byte[] salt) {
        this.salt = salt;
        this.pas = pas;
        this.tag = tag;
    }

    public byte[] getPas() {
        return pas;
    }

    public byte[] getSalt() {
        return salt;
    }

    public String getTag() {
        return tag;
    }

    public String encode() {
        String p = Base64.getEncoder().encodeToString(pas);
        String s = Base64.getEncoder().encodeToString(salt);
        String e = tag + System.getProperty("line.separator") + p + System.getProperty("line.separator") + s + System.getProperty("line.separator");
        return e;
    }
}
