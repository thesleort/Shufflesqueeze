import java.io.*;

/**
 * Created by Mark jervelund <Mark@jervelund.com> on 10-May-16.
 */
public class Encode {

    private static int length = 256;
    private static String[] code = new String[length];
    static BitOutputStream output;

    public static void main(String[] args) {

        int[] Occurances = new int[length];
        FileInputStream inFile;
        try {
            inFile = new FileInputStream(args[0]);

            for (int i : Occurances) Occurances[i] = 0;
            while (true) {
                int tempNumber = inFile.read();
                if (tempNumber < 0) {
                    break;
                }
                System.out.print(tempNumber + " ");
                Occurances[tempNumber] += 1;
            }
            System.out.println();
            for (int i : Occurances) System.out.print(i + " ");
            System.out.println();
            System.out.println(Occurances[32]);

            inFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Knot parent = genHuffTree(Occurances);
        maketranslations(parent);



        System.out.println();
        System.out.println("------------------------------------- ");
        System.out.println();
//        for(String s: code) System.out.print(" "+s+" - ");

        try {
            inFile = new FileInputStream(args[0]);
            FileOutputStream outstream = new FileOutputStream( new File(args[1]));
            for (int i : Occurances) outstream.write(i);


            output = new BitOutputStream(outstream);
            while (true) {
                int tempNumber = inFile.read();
                if (tempNumber < 0) {
                    break;
                }
//                System.out.println(tempNumber);
                writeTraverse(parent,tempNumber);
            }
//            So the program writes remaining bits.
            output.close();
            outstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void maketranslations(Knot parent) {

        Traverse(parent, "");

    }

    private static void Traverse(Knot tempknot, String binary) {
        if (tempknot.leftchild != null) {
            Traverse(tempknot.leftchild, (binary + "0"));
        }
        if (tempknot.rightchild != null) {
            Traverse(tempknot.rightchild, (binary + "1"));
        }
        if (tempknot.key != -1) {
            code[tempknot.key] = binary;
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
    private static void writeTraverse(Knot tempknot, int letternumber) throws IOException {
        if (tempknot.leftchild != null && tempknot.leftchild.key == letternumber) {
            output.writeBit(1);
            writeTraverse(tempknot.leftchild, letternumber);
        }else if(tempknot.rightchild != null){
            output.writeBit(0);
            writeTraverse(tempknot.rightchild, letternumber);
        }
//        if (tempknot.key != -1) {
//            code[tempknot.key] = binary;
//        }
    }

}
