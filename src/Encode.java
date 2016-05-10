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

            for (int i : Occurances) Occurances[i] = 0;
            while (true) {
                int tempnumer = inFile.read();
                if (tempnumer < 0) {
                    break;
                }
                System.out.print(tempnumer + " ");
                Occurances[tempnumer] += 1;
            }
            System.out.println();
            for (int i : Occurances) System.out.print(i + " ");
            System.out.println();
            System.out.println(Occurances[32]);

            inFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int tempcountertoberenamed = 0;
        for (int i : Occurances) tempcountertoberenamed++;

        PQHeap pqHeap = new PQHeap();
        int i = 0;
        while (i < 256) {
            if (Occurances[i] != 0) {
                System.out.println("inserting " + i + " with freq " + Occurances[i]);
            }
            pqHeap.insert(new Element(Occurances[i], i));
            i++;
        }
        Element child = pqHeap.extractMin();
        Knot child1 = new Knot(child.key, (Integer) child.data);
        while (pqHeap.getHeap().size() > 0) {
            child = pqHeap.extractMin();
            Knot child2 = new Knot(child.key, (Integer) child.data);
            Knot parent = new Knot(child1.freq+child2.freq);
            parent.rightchild = child1;
            child1.parent = parent;
            parent.leftchild = child2;
            child2.parent = parent;
            child1 = parent;
        }
    }
}
