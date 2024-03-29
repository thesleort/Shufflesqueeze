import java.io.*;
import java.util.ArrayList;

/**
 * Created by Mark Jervelund            <Mark@jervelund.com> &
 *            Troels Blicher Petersen   <troels@newtec.dk> on 10-May-16.
 */
public class Decode {
    /**
     * The Decode class is simply described
     * the reverse action of the Encode class.
     * It reads a compressed file and
     * decompressed it.
     * It does so, by reading the compression-
     * code in the beginning of the file. From
     * this code it generates a Huffman-tree.
     * This Huffman-tree is then used to translate
     * the the bits into letters again.
     */
    public Decode(String[] inFile) {
        int[] Occurrences = new int[256];
        try {
            FileInputStream inputFile = new FileInputStream(inFile[0]);
            BitInputStream input = new BitInputStream(inputFile);
            FileOutputStream output = new FileOutputStream(new File(inFile[1]));
            for (int i = 0; i < Occurrences.length; i++) {
                Occurrences[i] = input.readInt();
            }
            Knot huffmanTree = new HuffmanTreeGenerator().getTree(Occurrences);
            int letterNumber;
            int lettersLeft = huffmanTree.freq;
            while (true) {
                letterNumber = input.readBit();
                Knot currentKnot = huffmanTree;
                currentKnot = Traverse(currentKnot,letterNumber );
                while (currentKnot.key < 0) {
                    letterNumber = input.readBit();
                    currentKnot = Traverse(currentKnot,letterNumber );
                }
                output.write(currentKnot.key);
                lettersLeft--;
                if (lettersLeft < 1){
                    break;
                }
            }
            output.close();
            input.close();
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Traverse is very similar to the
     * method Traverse in the Encode
     * class. The difference here is that
     * it doesn't write anything. It only
     * traverses down through the Huffman-
     * tree to find the letter to translate
     * into.
     * @param currentKnot The knot from where it is checking
     *                    its children.
     * @param letterNumber The number to write the bytes for.
     *                     It writes one bit for every knot
     *                     it matches.
     * @return the current knot to tell what letter to write.
     */
    private static Knot Traverse(Knot currentKnot, int letterNumber){
        if (letterNumber == 0){
            currentKnot = currentKnot.leftchild;
        }else if(letterNumber == 1){
            currentKnot = currentKnot.rightchild;
        }
        return currentKnot;
    }
}
