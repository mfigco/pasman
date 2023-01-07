package pasman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.filechooser.FileSystemView;

public class Storer {

    public void store(Password p) {
        File file = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan/pas.txt");
        file.setWritable(true, true);
        try {
            FileWriter fr = new FileWriter(file, true);
            fr.write(p.encode());
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(Storer.class.getName()).log(Level.SEVERE, null, ex);
        }
        file.setWritable(false, false);
    }

    public Password retrieve(String tag) {
        String t, p = null, s = null;
        FileInputStream file = null;
        try {
            file = new FileInputStream(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan/pas.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Storer.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(file));
        try {
            while (!(t = br.readLine()).equals(tag)) {
            }
            p = br.readLine();
            s = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Storer.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] pas = Base64.getDecoder().decode(p);
        byte[] sal = Base64.getDecoder().decode(s);
        return new Password(tag, pas, sal);
    }

    public void setBox(JComboBox CB) {
        ArrayList <String> a = new ArrayList<>();
        String s;
        FileInputStream file = null;
        try {
            file = new FileInputStream(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan/pas.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Storer.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(file));
        try {
            while ((s = br.readLine()) != null) {
                a.add(s);
                br.readLine();
                br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(Storer.class.getName()).log(Level.SEVERE, null, ex);
        }
        a.sort(String::compareToIgnoreCase);
        for (String i : a) {
            CB.addItem(i);
        }
    }

    public void setFile() {
        File file = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan/pas.txt");
        try {
            file.createNewFile();
            file.setWritable(false, false);
        } catch (IOException ex) {
            Logger.getLogger(Storer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean masExists() {
        File file = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan/maspas.txt");
        return file.exists();
    }

    public void storeMas(Password mp) {
        File file = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan");
        file.mkdirs();
        file = new File(file, "/maspas.txt");
        try {
            file.createNewFile();
            FileWriter fr = new FileWriter(file, true);
            fr.write(mp.encode());
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(Storer.class.getName()).log(Level.SEVERE, null, ex);
        }
        file.setWritable(false, false);
    }

    public byte[][] getMasHash() {
        String h = null, s = null;
        FileInputStream file = null;
        try {
            file = new FileInputStream(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan/maspas.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Storer.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(file));
        try {
            br.readLine();
            h = br.readLine();
            s = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Storer.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] hash = Base64.getDecoder().decode(h);
        byte[] sal = Base64.getDecoder().decode(s);
        return new byte[][]{hash, sal};
    }

    public void delete(String tag) {
        FileInputStream file;
        File temp = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan/temp.txt");
        try {
            file = new FileInputStream(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/PasMan/pas.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            FileWriter fr = new FileWriter(temp, true);
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.equals(tag)) {
                    br.readLine();
                    br.readLine();
                    continue;
                }
                fr.write(currentLine + System.getProperty("line.separator"));
            }
            fr.close();
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Storer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Storer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
