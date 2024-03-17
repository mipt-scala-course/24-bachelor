package mipt.tinkoff.demo.p09_try;

import java.io.BufferedReader;
import java.io.FileReader;


public class TryWithResDemo {

    public static void main(String[] args) {

        try (BufferedReader reader1 = new BufferedReader(new FileReader("file2.txt"))) {
            try (BufferedReader reader2 = new BufferedReader(new FileReader("file1.txt"))) {
                System.out.println(reader1.readLine());
                System.out.println(reader2.readLine());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}


