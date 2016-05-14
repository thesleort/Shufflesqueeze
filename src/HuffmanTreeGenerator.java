import java.util.ArrayList;

/**
 * Created by troels on 14-05-16.
 * <p/>
 * This file belongs to the project Huffman-Tree in
 * the package PACKAGE_NAME.
 */
public class HuffmanTreeGenerator {
    /**
     * getTree generates a Huffman-tree based on the
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
    public Knot getTree(int[] occurrences) {
        ArrayList<Element> treeParts = new ArrayList();
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
