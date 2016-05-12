import java.io.*;

/**
 * Created by Mark jervelund            <Mark@jervelund.com> &
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
     * @param args It takes two arguments, where
     *             the first argument is the
     *             compressed input-file and the
     *             second argument is the
     *             uncompressed output-file.
     */
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
            int letterNumber;
            int lettersLeft = huffManTree.freq;
            while (true) {
                letterNumber = input.readBit();
                Knot currentKnot = huffManTree;

                currentKnot = Traverse(currentKnot, letterNumber);
                while (currentKnot.key < 0) {
                    letterNumber = input.readBit();
                    currentKnot = Traverse(currentKnot, letterNumber);
                }
                output.write(currentKnot.key);
                lettersLeft--;
                if (lettersLeft < 1) {
                    break;
                }
            }
            output.close();
            input.close();
            inFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is exactly the same as
     * the method (of the same name) in the
     * Encode class. It is here so that the
     * Decode class works independently
     * without the need of the Encode class.
     * @param occurances An array of integers. Since the
     *                   letters are saved as integers, we
     *                   know exactly where a letter is in
     *                   the array - no search is needed.
     * @return The root node of the Huffman-tree.
     */
    private static Knot genHuffTree(int[] occurances) {
        PQHeap pqHeap = new PQHeap();
        int i = 0;
        while (i < 256) {
            if (occurances[i] != 0) {
                pqHeap.insert(new Element(occurances[i], i));
            }
            i++;
        }
        Element child = pqHeap.extractMin();
        Knot child1 = new Knot(child.key, (Integer) child.data);
        Knot parent = null;
        while (pqHeap.getHeap().size() > 0) {
            child = pqHeap.extractMin();
            Knot child2 = new Knot(child.key, (Integer) child.data);
            parent = new Knot(child1.freq + child2.freq);
            parent.rightchild = child1;
            child1.parent = parent;
            parent.leftchild = child2;
            child2.parent = parent;
            child1 = parent;
        }
        return parent;
    }

    /**
     * Traverse is very similar to the
     * method writeTraverse in the Encode
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
    private static Knot Traverse(Knot currentKnot, int letterNumber) {
        if (letterNumber == 0) {
            currentKnot = currentKnot.rightchild;
        } else if (letterNumber == 1) {
            currentKnot = currentKnot.leftchild;
        }
        return currentKnot;
    }

}