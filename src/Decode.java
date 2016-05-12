//import java.io.*;
//
///**
// * Created by mark- on 12-May-16.
// */
//public class Decode {
//    public static void main(String[] args) {
//        int[] Occurances = new int[256];
//        try {
//            FileInputStream inFile = new FileInputStream(args[0]);
//            BitInputStream input = new BitInputStream(inFile);
//            FileOutputStream output = new FileOutputStream(new File(args[1]));
//            for (int i = 0; i < Occurances.length; i++) {
//                Occurances[i] = input.readInt();
//            }
//            Knot huffManTree = genHuffTree(Occurances);
//            int Letternumer;
//            int lettersleft = huffManTree.freq;
//            while (true) {
//                Letternumer = input.readBit();
//                Knot tempknot = huffManTree;
//
//                tempknot = Traverse(tempknot,Letternumer );
//                while (tempknot.key < 0) {
//                    Letternumer = input.readBit();
//                    tempknot = Traverse(tempknot,Letternumer );
//                }
//                output.write(tempknot.key);
//                lettersleft--;
//                if (lettersleft < 1){
//                    break;
//                }
//}
//            output.close();
//            input.close();
//            inFile.close();
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//    private static Knot genHuffTree(int[] occurances) {
//        PQHeap pqHeap = new PQHeap();
//        int i = 0;
//        while (i < 256) {
//            if (occurances[i] != 0) {
//                pqHeap.insert(new Element(occurances[i], i));
//            }
//            i++;
//        }
//        Element child = pqHeap.extractMin();
//        Knot child1 = new Knot(child.key, (Integer) child.data);
//        Knot parent = null;
//        while (pqHeap.getHeap().size() > 0) {
//            child = pqHeap.extractMin();
//            Knot child2 = new Knot(child.key, (Integer) child.data);
//            parent = new Knot(child1.freq + child2.freq);
//            parent.rightchild = child1;
//            child1.parent = parent;
//            parent.leftchild = child2;
//            child2.parent = parent;
//            child1 = parent;
//        }
//        return parent;
//    }
//
//    private static Knot Traverse(Knot tempknot, int letternumber){
//            if (letternumber == 0){
//                tempknot = tempknot.rightchild;
//            }else if(letternumber == 1){
//                tempknot = tempknot.leftchild;
//            }
//        return tempknot;
//    }
//
//}
