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
     *
     * @param inFile
     */
    public Encode(String[] inFile) {

        int[] Occurrences = new int[length];
        FileInputStream inputFile;
        try {
            System.out.println(inFile.length);
            inputFile = new FileInputStream(inFile[0]);

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
        Knot parent = new HuffmanTreeGenerator().getTree(Occurrences);
        makeTranslations(parent);
        try {
            inputFile = new FileInputStream(inFile[0]);
            FileOutputStream outstream;
            if (inFile.length>1) {
                outstream = new FileOutputStream( new File(inFile[1]+".hzip"));
            } else {
                outstream = new FileOutputStream( new File(inFile[0]+".hzip"));
            }
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
     * or a 1 to a string that is then added to a translations-
     * list in the end.
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
}