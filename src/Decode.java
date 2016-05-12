import java.io.*;
import java.util.ArrayList;

/**
 * Created by mark- on 12-May-16.
 */
public class Decode {
    public static void main(String[] args) {
        int[] Occurances = new int[256];
        try {
            FileInputStream inFile = new FileInputStream(args[0]);
            BitInputStream input = new BitInputStream(inFile);
            FileOutputStream output = new FileOutputStream(new File(args[1]));
            for (int i = 0; i < Occurances.length; i++) {
                Occurances[i] = input.readInt();
            }
            Knot huffManTree = genHuffTree(Occurances);
            int Letternumer;
            int lettersleft = huffManTree.freq;
            System.out.println(lettersleft);
            while (true) {
                Letternumer = input.readBit();
                Knot tempknot = huffManTree;

                tempknot = Traverse(tempknot,Letternumer );
                while (tempknot.key < 0) {
                    Letternumer = input.readBit();
                    tempknot = Traverse(tempknot,Letternumer );
                }
                output.write(tempknot.key);

                System.out.println("letters left "+lettersleft);
                lettersleft--;
                if (lettersleft < 1){
                    break;
                }
}
            System.out.println("closing stuff");
            output.close();
            input.close();
            inFile.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private static Knot genHuffTree(int[] occurances) {
        ArrayList<Element> treeParts = new ArrayList<>();
        int i = 0;
        while (i < 256) {
            if (occurances[i] > 0) {
                System.out.println("inserting " + i + " with freq " + occurances[i]);
                treeParts.add(new Element(occurances[i], new Knot(occurances[i],i)));
            }
            i++;
        }

        PQHeap pqHeap = new PQHeap();

        for(Element e : treeParts){
            pqHeap.insert(e);
        }
        System.out.println("test print"+treeParts.size());
        for (int j = 0; j < treeParts.size() - 1; j++) {
            Element parent = new Element(0, new Knot(0));
            Knot child1 = pqHeap.extractMin().data;
            Knot child2 = pqHeap.extractMin().data;
            parent.key = (child1.freq + child2.freq);
            parent.data.freq = (child1.freq + child2.freq);
            parent.data.rightchild = child1;
            child1.parent = parent.data;
            parent.data.leftchild = child2;
            child2.parent = parent.data;
            System.out.println("inserting parent with key to list "+parent.key);
            System.out.println("parent has feq "+parent.data.freq);
            pqHeap.insert(parent);
        }
        Knot hufftree = pqHeap.extractMin().data;
        System.out.println(hufftree.freq);
        return hufftree;
    }

    private static Knot Traverse(Knot tempknot, int letternumber){
            if (letternumber == 0){
                tempknot = tempknot.leftchild;
            }else if(letternumber == 1){
                tempknot = tempknot.rightchild;
            }
        return tempknot;
    }

}
