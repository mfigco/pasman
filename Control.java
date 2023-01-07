package pasman;

import java.io.File;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

public class Control {

    static PasMan pm;
    static Storer st;
    JFrame current;
    private String mp;

    public Control() {
        pm = new PasMan();
        st = new Storer();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        renameTemp();
        Control ctrl = new Control();
        if (!st.masExists()) {
            ctrl.current = new Login_1(ctrl);
        } else {
            ctrl.current = new Login(ctrl);
        }
    }

    public void login(char[] mp, JLabel l) {
        byte[][] hs = st.getMasHash();
        pm.setSalt(hs[1]);
        if (Arrays.equals(pm.generateHash(mp), hs[0])) {
            this.mp = new String(mp);
            newPMAN();
        } else {
            JOptionPane.showMessageDialog(current, "Contraseña incorrecta, intente otra vez");
        }
    }

    public void firstLogin(char[] mp, char[] mp2) {
        String pas = new String(mp).trim();
        String pas2 = new String(mp2).trim();
        if (pas.length() < 2) {
            JOptionPane.showMessageDialog(current, "Escriba una contraseña de más de un carácter");
        } else if (pas.isBlank() || pas2.isBlank()) {
            JOptionPane.showMessageDialog(current, "Escriba su contraseña en ambos espacios");
        } else {
            if (Arrays.equals(mp, mp2)) {
                pm.reSalt();
                st.storeMas(pm.getPassword(pm.generateHash(mp)));
                this.mp = new String(mp);
                st.setFile();
                newPMAN();
            } else {
                JOptionPane.showMessageDialog(current, "Las contraseñas no coinciden");
            }
        }
    }

    public void newPMAN() {
        current.setVisible(false);
        current.dispose();
        current = new PMAN(this);
    }

    public void newPAdd() {
        current.setVisible(false);
        current.dispose();
        current = new PAdd(this);
    }

    public void setComboBox(JComboBox CB) {
        st.setBox(CB);
    }

    public void retrievePas(JComboBox CB, JPasswordField PF) {
        Password pas = st.retrieve(CB.getSelectedItem().toString());
        pm.decrypt(mp, pas.getPas(), pas.getSalt());
        PF.setText(new String(pm.prod));
        pm.clear();
    }

    public void storePas(JTextField tf, char[] p, char[] p2) {
        String tag = tf.getText().trim();
        String pas = new String(p).trim();
        String pas2 = new String(p2).trim();
        if (tag.isBlank()) {
            JOptionPane.showMessageDialog(current, "Escriba un nombre para la contraseña");
        } else if (pas.length() < 2) {
            JOptionPane.showMessageDialog(current, "Escriba una contraseña de más de un carácter");
        } else if (pas.isBlank() || pas2.isBlank()) {
            JOptionPane.showMessageDialog(current, "Escriba su contraseña en ambos espacios");
        } else {
            if (Arrays.equals(p, p2)) {
                pm.encrypt(pas, mp);
                st.store(pm.getPassword(tag));
                pm.clear();
                JOptionPane.showMessageDialog(current, "Contraseña guardada exitosamente");
                tf.setText("");
            } else {
                JOptionPane.showMessageDialog(current, "Las contraseñas no coinciden");
            }
        }
    }

    public void deletePas(JComboBox CB) {
        String tag = CB.getSelectedItem().toString();
        int dialog = JOptionPane.showConfirmDialog(current, "Se eliminará la contraseña \"" + tag + "\"", "Precaución", JOptionPane.OK_CANCEL_OPTION);
        if (dialog == JOptionPane.OK_OPTION) {
            st.delete(tag);
            CB.removeItem(CB.getSelectedItem());
            dialog = JOptionPane.showConfirmDialog(current, "Para que se efectuen los cambios, se debe reiniciar la aplicación. Desea cerrar la aplicación ahora?", "Atención", JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.YES_OPTION) {
                System.exit(0);
            }

        }
    }

    public static void renameTemp() {
        File temp = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan/temp.txt");
        if (temp.exists()) {
            new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan/pas.txt").delete();
            temp.renameTo(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan/pas.txt"));
        }
    }
}
