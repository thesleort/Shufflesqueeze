/**
 * Created by trpet15 - Troels Blicher Petersen <troels@newtec.dk> on 3/4/16.
 */

/**
 * This object holds a key and an object.
 * The key is the one to be sorted and the
 * object may hold whatever data.
 */
public class Element {
    public int key;
    public Knot data;

    public Element(int i, Knot o) {
        this.key = i;
        this.data = o;
    }
}


