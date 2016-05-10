/**
 * @implSpec extractMin() is supposed to extract the
 * minimum element of the heap.
 * @implSpec insert(Element e) should insert an element e
 * at the correct position.
 */
public interface EQ {
    public Element extractMin();
    public void insert(Element e);
}
