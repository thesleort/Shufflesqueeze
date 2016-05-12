import java.io.*;
import java.util.ArrayList;

/**
 * Created by Mark Jervelund            <Mark@jervelund.com> &
 *            Troels Blicher Petersen   <troels@newtec.dk> on 10-May-16.
 */
public class Encode {

    private static int length = 256;
    private static BitOutputStream output;
    private static String[] code = new String[length];

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

        int[] Occurrences = new int[length];
        FileInputStream inputFile;
        try {
            inputFile = new FileInputStream(args[0]);

            for (int i : Occurrences) Occurrences[i] = 0;
            while (true) {
                int tempNumber = inputFile.read();
                if (tempNumber < 0) {
                    break;
                }
                Occurrences[tempNumber] += 1;
            }
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Knot parent = genHuffTree(Occurrences);
        makeTranslations(parent);
        try {
            inputFile = new FileInputStream(args[0]);
            FileOutputStream outstream = new FileOutputStream( new File(args[1]));
            output = new BitOutputStream(outstream);
            for (int i : Occurrences) output.writeInt(i);


            String bitsToWrite;
            while (true) {
                int letterNumber = inputFile.read();
                if (letterNumber < 0) {
                    break;
                }
                bitsToWrite = code[letterNumber];
                for (int i = 0; i < bitsToWrite.length(); i++) {
                    output.writeBit(Character.getNumericValue(bitsToWrite.charAt(i)));
                }
            }
//            So the program writes remaining bits.
            output.close();
            outstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the traversal through the Huffman-tree.
     * @param parent The root knot.
     */
    private static void makeTranslations(Knot parent) {
        Traverse(parent, "");
    }

    /**
     * Traverses down through the Huffman-tree and adds a 0
     * or a 1 to a string that is then added to a leaf-knot
     * in the end.
     * @param currentKnot The knot it currently points to
     * @param binary A string that holds the binary string
     *               representation of the leaf-knot. It is
     *               continuously made one bit larger for
     *               every knot it traverses through.
     */
    private static void Traverse(Knot currentKnot, String binary) {
        if (currentKnot.leftchild != null) {
            Traverse(currentKnot.leftchild, (binary + "0"));
        }
        if (currentKnot.rightchild != null) {
            Traverse(currentKnot.rightchild, (binary + "1"));
        }
        if (currentKnot.key != -1) {
            code[currentKnot.key] = binary;
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
     * Afterwards it extracts the two smallest nodes,
     * merges them and inserts them back into the queue.
     * this is done n times, where is the number of elements
     * in occurrences.
     *
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
                Element e = new Element(occurrences[i], new Knot(occurrences[i],i));
                treeParts.add(e);
                pqHeap.insert(e);
            }
            i++;
        }
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
            pqHeap.insert(parent);
        }
        Knot huffTree = pqHeap.extractMin().data;
        return huffTree;
    }
}