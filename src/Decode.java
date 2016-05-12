import java.io.*;

/**
 * Created by mark- on 12-May-16.
 */
public class Decode {
    public static void main(String[] args) {
        Encode encode = new Encode();
        int[] Occurances = new int[256];

        try {
            FileInputStream inFile = new FileInputStream(args[0]);
            BitInputStream input = new BitInputStream(inFile);
            FileOutputStream output = new FileOutputStream(new File(args[1]));
            for (int i = 0; i < Occurances.length; i++) {
                Occurances[i] = input.readInt();
                System.out.println(Occurances[i]);
            }
            Knot hufftree = genHuffTree(Occurances);

            int letternumber;
            int lettersleft = hufftree.freq;
            while (true) {
                letternumber = input.readBit();
                Knot tempknot = hufftree;

                tempknot = Traverse(tempknot,letternumber );
                while (tempknot.key < 0) {
                    letternumber = input.readBit();
                    tempknot = Traverse(tempknot,letternumber );

                }
//                System.out.println("Writing "+tempknot.key+" To output");
                output.write(tempknot.key);
                lettersleft--;System.out.println(lettersleft);
                if (lettersleft < 1){
                    break;
                }
                System.out.println("printing something");
            }
            System.out.println("closing output streams.     ");
            output.close();
//            input.close();
//            inFile.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private static Knot genHuffTree(int[] occurances) {
        PQHeap pqHeap = new PQHeap();
        int i = 0;
        while (i < 256) {
            if (occurances[i] != 0) {
                System.out.println("inserting " + i + " with freq " + occurances[i]);
                pqHeap.insert(new Element(occurances[i], i));
            }
            i++;
        }
        Element child = pqHeap.extractMin();
        System.out.println(child.key);
        Knot child1 = new Knot(child.key, (Integer) child.data);
        Knot parent = null;
        while (pqHeap.getHeap().size() > 0) {
            child = pqHeap.extractMin();
            Knot child2 = new Knot(child.key, (Integer) child.data);
            System.out.println(child2.freq);
//            System.out.println(child2.key);
            parent = new Knot(child1.freq + child2.freq);
            parent.rightchild = child1;
            child1.parent = parent;
            parent.leftchild = child2;
            child2.parent = parent;
            child1 = parent;
        }
        return parent;
    }

    private static Knot Traverse(Knot tempknot, int letternumber){
            if (letternumber == 0){
                tempknot = tempknot.rightchild;
            }else if(letternumber == 1){
                tempknot = tempknot.leftchild;
            }
        return tempknot;
    }

}
