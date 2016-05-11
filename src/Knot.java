/**
 * Created by Troels Blicher Petersen <troels@newtec.dk>    <trpet15>
 *          & Mark jervelund          <Mark@jervelund.com>  <Mjerv15> on 30-Mar-16.
 */

/**
 * This object holds a key.
 * The key is the one to be sorted.
 *
 * It also holds information about who its children are
 * and what parent it has.
 */
public class Knot {
    public int key;
    public int freq;
    public Knot leftchild;
    public Knot rightchild;
    public Knot parent;

    public Knot(int j, int i) {
        this.key = i;
        this.freq = j;
        this.leftchild = null;
        this.rightchild = null;
        this.parent =null;
    }
    public Knot(int j) {
        this.key = -1;
        this.freq = j;
        this.leftchild = null;
        this.rightchild = null;
        this.parent =null;
    }
}