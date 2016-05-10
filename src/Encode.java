import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Mark jervelund <Mark@jervelund.com> on 10-May-16.
 */
public class Encode {



    public static void main(String[] args) {
        int[] Occurances = new int[256];
        Element Hudffmannode;
        try {
            FileInputStream inFile = new FileInputStream(args[0]);

            for(int i: Occurances) Occurances[i] = 0;
            while (inFile !=  null) {
                Occurances[inFile.read()] += 1;
            }
            for(int i: Occurances) System.out.println(Occurances[i]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
