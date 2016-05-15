# Huffman-Tree
This is a simple compression tool made using the Huffman Tree algorithm.
---
It can be used in two ways. The first is simple file compression. Here it simply compresses the file and stores a decompression key in the same file.
The second method saves the decompression key in a separate file. This means that one cannot decompress the file without having
 the key-file.
 ---
#How to:

Simple compression:
java -jar Huffencrypt.jar -c inputfile outputfile(optional)
Simple decompression:
java -jar Huffencrypt.jar -dc inputfile.hzip outputfile(optional)

Encryption:
java -jar Huffencrypt.jar -e inputfile outputfile(optional)
Decryption:
java -jar Huffencrypt.jar -d inputfile.hec outputfile(optional) inputfile.key

---
I encourage everyone to try and break the encryption, so that we can possibly improve it.
 
 
