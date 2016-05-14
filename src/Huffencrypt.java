import java.util.Arrays;

/**
 * Created by troels on 14-05-16.
 * <p/>
 * This file belongs to the project Huffman-Tree in
 * the package PACKAGE_NAME.
 */
public class Huffencrypt {
    public static void main(String[] args) {
        if (args[0].equals("-e")) {
            new Encrypt(Arrays.copyOfRange(args, 1, args.length));
        } else if (args[0].equals("-d")) {
            if (args.length > 3) {
                new Decrypt(Arrays.copyOfRange(args, 1, args.length - 1), args[args.length - 1]);
            } else {
                new Decrypt(Arrays.copyOfRange(args, 1, args.length - 1), args[args.length - 1]);
            }
        } else if (args[0].equals("-c")) {
            new Encode(Arrays.copyOfRange(args, 1, args.length));
        } else if (args[0].equals("-dc")) {
            new Decode(Arrays.copyOfRange(args, 1, args.length));
        } else if (args[0].equals("-h")) {
            System.out.println(
                    "--- Huffman encrypt ---\n" +
                            "-e : encrypt file\n" +
                            "-d : decrypt file\n" +
                            "-c : compress file\n" +
                            "-dc : decompress file\n" +
                            "-h : help");
        }
    }
}
