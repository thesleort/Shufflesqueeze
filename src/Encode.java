import java.io.*;
import java.util.ArrayList;

/**
 * Created by Mark jervelund            <Mark@jervelund.com> &
 *            Troels Blicher Petersen   <troels@newtec.dk> on 10-May-16.
 */
public class Encode {

    private static int length = 256;
    static BitOutputStream output;

    /**
     * This is the encode class. This class encodes
     * and compresses a file using Huffman-trees.
     * It starts off by reading the input-file (1st
     * argument). All the letters of the file are read
     * and represented as integers in an array. The
     * integer representation of the letter is also
     * used as the location in the array of integers.
     * The algorithm therefore doesn't need to search
     * for the letters in the array. Number of times a
     * letter is found in the file, is therefore the
     * written in the array.
     * It then generates a Huffman using a priority-
     * queue heap.
     * @param args Input is first argument and output
     *             is the second argument.
     */
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
                Occurances[tempNumber] += 1;
            }
            inFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Knot parent = genHuffTree(Occurances);

        try {
            inFile = new FileInputStream(args[0]);
            FileOutputStream outputStream = new FileOutputStream( new File(args[1]));
            output = new BitOutputStream(outputStream);
            for (int i : Occurances) output.writeInt(i);


            String tobewritted;
            while (true) {
                int tempNumber = inFile.read();
                if (tempNumber < 0) {
                    break;
                }
                writeTraverse(parent,tempNumber);
//                System.out.println(tempNumber);
                tobewritted = code[tempNumber];
                for (int i = 0; i < tobewritted.length(); i++) {
                    output.writeBit(Character.getNumericValue(tobewritted.charAt(i)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * genHuffTree generates a Huffman-tree based on the
     * occurrence of each letter.
     * First it inserts the occurrences in a priority-queue
     * heap (pqHeap) which sorts the letters by occurrence.
     * The insertion is an Element, where first parameter is
     * how many times the letter occurs and the second
     * parameter is the letter represented as an integer.
     * Afterwards it extracts the letter with the least
     * occurrence and inserts it as the deepest node of the
     * Huffman-tree. It keeps on doing this until it reaches
     * root of the huffman-tree.
     * @param occurrences An array of integers. Since the
     *                    letters are saved as integers, we
     *                    know exactly where a letter is in
     *                    the array - no search is needed.
     * @return The root node of the Huffman-tree.
     */
    private static Knot genHuffTree(int[] occurrences) {
        ArrayList<Element> treeParts = new ArrayList<>();
        PQHeap pqHeap = new PQHeap();
        int i = 0;
        while (i < 256) {
            if (occurrences[i] > 0) {
                System.out.println("inserting " + i + " with freq " + occurrences[i]);
                Element e = new Element(occurrences[i], new Knot(occurrences[i],i));
                treeParts.add(e);
                pqHeap.insert(e);
            }
            i++;
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

    /**
     * writeTraverse checks if the letter is in the right or
     * the left child.
     * It checks if it is either, it writes a 1 or a 0 and
     * keeps traversing down through that child.
     * It doesn't return anything and therefore terminates
     * when a knot doesn't have any children.
     * @param currentKnot The knot from where it is checking
     *                    its children.
     * @param letternumber The number to write the bytes for.
     *                     It writes one bit for every knot
     *                     it matches.
     * @throws IOException If something happens to the file.
     */
    private static void writeTraverse(Knot currentKnot, int letternumber) throws IOException {
        if (currentKnot.leftchild != null && currentKnot.leftchild.key == letternumber) {
            output.writeBit(1);
            writeTraverse(currentKnot.leftchild, letternumber);
        }else if(currentKnot.rightchild != null){
            output.writeBit(0);
            writeTraverse(currentKnot.rightchild, letternumber);
        }
    }
}