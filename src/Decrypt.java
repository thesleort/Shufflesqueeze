import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by troels on 14-05-16.
 * <p/>
 * This file belongs to the project Huffman-Tree in
 * the package PACKAGE_NAME.
 */
public class Decrypt {
    public Decrypt(String inFile[], String keyFile) {
        int[] Occurrences = new int[256];
        try {
            FileInputStream inputFile = new FileInputStream(keyFile);
            BitInputStream input = new BitInputStream(inputFile);
            FileOutputStream outStream;
            if (inFile.length > 1) {
                outStream = new FileOutputStream(new File(inFile[1]+".out"));
            } else {
                outStream = new FileOutputStream(new File(inFile[0]+".out"));
            }
            for (int i = 0; i < Occurrences.length; i++) {
                Occurrences[i] = input.readInt();
            }
            inputFile.close();
            input.close();
            inputFile = new FileInputStream(inFile[0]);
            input = new BitInputStream(inputFile);
            Knot huffmanTree = new HuffmanTreeGenerator().getTree(Occurrences);
            System.out.println(Occurrences.length);
            int letterNumber;
            int lettersLeft = huffmanTree.freq;
            while (true) {
                letterNumber = input.readBit();
                Knot currentKnot = huffmanTree;
                currentKnot = Traverse(currentKnot, letterNumber);
                while (currentKnot.key < 0) {
                    letterNumber = input.readBit();
                    currentKnot = Traverse(currentKnot, letterNumber);
                }
                outStream.write(currentKnot.key);
                lettersLeft--;
                if (lettersLeft < 1) {
                    break;
                }
            }
            System.out.println("done");
            outStream.close();
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
     *
     * @param currentKnot  The knot from where it is checking
     *                     its children.
     * @param letterNumber The number to write the bytes for.
     *                     It writes one bit for every knot
     *                     it matches.
     * @return the current knot to tell what letter to write.
     */
    private static Knot Traverse(Knot currentKnot, int letterNumber) {
        if (letterNumber == 0) {
            currentKnot = currentKnot.leftchild;
        } else if (letterNumber == 1) {
            currentKnot = currentKnot.rightchild;
        }
        return currentKnot;
    }
}
