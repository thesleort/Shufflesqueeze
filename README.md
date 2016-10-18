# Shufflesqueeze
This is a simple compression tool made using the Huffman Tree algorithm.
---
It can be used in two ways. The first is simple file compression. Here it simply compresses the file and stores a decompression key in the same file.
The second method saves the decompression key in a separate file. This means that one cannot decompress the file without having
 the key-file.
 ---
#How to:

Simple compression:<br>
`java -jar Shufflesqueeze.jar -c inputfile outputfile(optional)`<br>
Simple decompression:<br>
`java -jar Shufflesqueeze.jar -dc inputfile.hzip outputfile(optional)`<br>

Encryption:<br>
`java -jar Shufflesqueeze.jar -e inputfile outputfile(optional)`<br>
Decryption:<br>
`java -jar Shufflesqueeze.jar -d inputfile.hec outputfile(optional) inputfile.key`<br>

---
I encourage everyone to do whatever they want with the code. The program was a Computer Science project at University of Southern Denmark. I have since improved the functionality a little, but the core program is still the same.
 
 
