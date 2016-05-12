import java.io.*;
import java.util.ArrayList;

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
            output = new BitOutputStream(outstream);
            for (int i : Occurances) output.writeInt(i);


            String tobewritted;
            while (true) {
                int tempNumber = inFile.read();
                if (tempNumber < 0) {
                    break;
                }
//                System.out.println(tempNumber);
                tobewritted = code[tempNumber];
                for (int i = 0; i < tobewritted.length(); i++) {
                    output.writeBit(Character.getNumericValue(tobewritted.charAt(i)));
                }
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
            System.out.println("setting bin for key "+tempknot.key+" to " + binary);
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
            pqHeap.insert(parent);
        }
        Knot hufftree = pqHeap.extractMin().data;
        System.out.println(hufftree.freq);
        return hufftree;
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
