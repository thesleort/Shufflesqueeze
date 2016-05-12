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
     * @param args It takes two arguments, where
     *             the first argument is the
     *             compressed input-file and the
     *             second argument is the
     *             uncompressed output-file.
     */
    public static void main(String[] args) {
        int[] Occurrences = new int[256];
        try {
            FileInputStream inFile = new FileInputStream(args[0]);
            BitInputStream input = new BitInputStream(inFile);
            FileOutputStream output = new FileOutputStream(new File(args[1]));
            for (int i = 0; i < Occurrences.length; i++) {
                Occurrences[i] = input.readInt();
            }
            Knot huffmanTree = genHuffTree(Occurrences);
            int letterNumber;
            int lettersLeft = huffmanTree.freq;
            System.out.println(lettersLeft);
            while (true) {
                letterNumber = input.readBit();
                Knot currentKnot = huffmanTree;

                currentKnot = Traverse(currentKnot,letterNumber );
                while (currentKnot.key < 0) {
                    letterNumber = input.readBit();
                    currentKnot = Traverse(currentKnot,letterNumber );
                }
                output.write(currentKnot.key);

                System.out.println("letters left "+lettersLeft);
                lettersLeft--;
                if (lettersLeft < 1){
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

    /**
     * This method is exactly the same as
     * the method (of the same name) in the
     * Encode class. It is here so that the
     * Decode class works independently
     * without the need of the Encode class.
     * @param occurrences An array of integers. Since the
     *                   letters are saved as integers, we
     *                   know exactly where a letter is in
     *                   the array - no search is needed.
     * @return The root node of the Huffman-tree.
     */
    private static Knot genHuffTree(int[] occurrences) {
        ArrayList<Element> treeParts = new ArrayList<>();
        int i = 0;
        while (i < 256) {
            if (occurrences[i] > 0) {
                System.out.println("inserting " + i + " with freq " + occurrences[i]);
                treeParts.add(new Element(occurrences[i], new Knot(occurrences[i],i)));
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
    private static Knot Traverse(Knot currentKnot, int letterNumber){
        if (letterNumber == 0){
            currentKnot = currentKnot.leftchild;
        }else if(letterNumber == 1){
            currentKnot = currentKnot.rightchild;
        }
        return currentKnot;
    }
}
