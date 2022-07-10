package simulation;

import java.io.Serializable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Magnet implements Serializable {
    
    private static final long serialVersionUID = 1L;

    public static final double radius = 10.0;
    
    private int x;
    private int y;
    private double c;
    
    public Magnet(int x, int y, double c) {
        this.x = x;
        this.y = y;
        this.c = c;
    }
    
    public static String magnetHash(ArrayList<Magnet> magnets) {
        Gson gson = new Gson();
        String hash = Integer.toHexString(gson.toJson(magnets).hashCode()); 
        return hash.substring(0, Math.min(3, hash.length()));
    }

    public static void saveMagnets(ArrayList<Magnet> magnets, String file) {
        try {
            FileWriter f = new FileWriter(
                    String.format("magnets/%s.txt", file));
            BufferedWriter b = new BufferedWriter(f);

            Gson gson = new Gson();
            b.write(gson.toJson(magnets));

            b.close();
            f.close();

            System.out.println(
                    String.format("Saved configuration to \"magnets/%s.txt\"", file));
        } catch (FileNotFoundException err) {
            System.out.println("Create directory \"magnets\" in order to save magnets");
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
    
    public static ArrayList<Magnet> loadMagnets(String file) {
        ArrayList<Magnet> magnets = null;
        try {
            FileReader f = new FileReader(String.format("magnets/%s.txt", file));
            BufferedReader b = new BufferedReader(f);

            Gson gson = new Gson();
            magnets = gson.fromJson(b.readLine(), new TypeToken<ArrayList<Magnet>>(){}.getType());

            b.close();
            f.close();

            System.out.printf("Loaded configuration from \"magnets/%s.txt\"\n", file);
        } catch (FileNotFoundException err) {
            System.out.println(String.format("File \"magnets/%s.txt\" not found", file));
        } catch (Exception err) {
            err.printStackTrace();
        }
        return magnets;
    }

    public int getXPos() { return x; }
    public int getYPos() { return y; }
    public double getCoef() { return c; }

    public void setxPos(int x) { this.x = x; }
    public void setyPos(int y) { this.y = y; }
    public void setCoef(double c) { this.c = c; }
}
