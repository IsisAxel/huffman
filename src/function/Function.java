package function;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import model.Encoding;
import model.Language;
import model.Node;

public class Function 
{
    public static Node generateHuffmanTree(List<Node> source)
    {
        List<Node> huffmanTree = new ArrayList<>(source);
        while (huffmanTree.size() > 1) {
            huffmanTree.sort((a, b) -> Integer.compare(a.getProbability(), b.getProbability()));
            
            Node left = huffmanTree.remove(0);
            Node right = huffmanTree.remove(0);
            
            Node parent = new Node(left, right, left.getProbability() + right.getProbability());
            
            huffmanTree.add(parent);
        }
        return huffmanTree.get(0);
    }    

    public static List<Node> generateSource(String text, String alphabet)
    {
        
        List<Node> source = new ArrayList<>();
        for (char c : text.toCharArray()) 
        {
            boolean isInAlphabet = false;
            for (char symbol : alphabet.toCharArray()) 
            {
                if (c == symbol) 
                {
                    isInAlphabet = true;
                    break;
                }
            }
            if (!isInAlphabet) 
            {
                throw new IllegalArgumentException("Character " + c + " is not in the alphabet " + alphabet);
            }
            boolean found = false;
            for (Node node : source) 
            {
                if (node.getSymbol() == c) 
                {
                    node.setProbability(node.getProbability() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) 
            {
                source.add(new Node( 1, c));
            }
        }
        return source;
    }

    public static void generateCode(Node root, String code , List<Encoding> encodings) {
        if (root.getLeft() == null && root.getRigth() == null) {
            encodings.add(new Encoding(code, root.getSymbol()));
            return;
        }
        if (root.getLeft() != null) {
            generateCode(root.getLeft(), code + "0", encodings);
        }
        if (root.getRigth() != null) {
            generateCode(root.getRigth(), code + "1", encodings);
        }
    }

    public static String encode(String text , String alphabet) {
        List<Encoding> encodings = new ArrayList<>();
        List<Node> source = generateSource(text,alphabet);
        Node root = generateHuffmanTree(source);
        encodings = getEncodings(root);
        StringBuilder encodedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            for (Encoding encoding : encodings) {
                if (encoding.getSymbol() == c) {
                    encodedText.append(encoding.getCode());
                    break;
                }
            }
        }
        System.out.println("Decoded text: " + decode(encodedText.toString(), encodings));
        return encodedText.toString();
    }

    public static String decode(String encodedText, List<Encoding> encodings) {
        StringBuilder decodedText = new StringBuilder();
        String temp = "";
        for (char c : encodedText.toCharArray()) {
            temp += c;
            for (Encoding encoding : encodings) {
                if (encoding.getCode().equals(temp)) {
                    decodedText.append(encoding.getSymbol());
                    temp = "";
                    break;
                }
            }
        }
        return decodedText.toString();
    }

    public static void saveEncodingsToFile(List<Encoding> encodings, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(encodings);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Encoding> loadEncodingsFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Encoding>) ois.readObject();
        }
    }

    
    public static void encodeFromFile(String textFilePath, String alphabetFilePath, String outputBinPath, String encodingsPath) throws IOException {
        String text = readTextFile(textFilePath);
        String alphabet = readTextFile(alphabetFilePath);
    
        List<Node> source = generateSource(text, alphabet);
        Node root = generateHuffmanTree(source);
        List<Encoding> encodings = getEncodings(root);
    
        StringBuilder encodedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            for (Encoding encoding : encodings) {
                if (encoding.getSymbol() == c) {
                    encodedText.append(encoding.getCode());
                    break;
                }
            }
        }
    
        writeBinaryToFile(encodedText.toString(), outputBinPath);
        saveEncodingsToFile(encodings, encodingsPath);
    
        System.out.println("Texte encodé : " + encodedText);
        System.out.println("Texte décodé : " + decode(encodedText.toString(), encodings));
    }
    

    public static void decodeFromFile(String encodedBinPath, String encodingsPath) throws IOException, ClassNotFoundException {
        List<Encoding> encodings = loadEncodingsFromFile(encodingsPath);
        String encodedText = readBinaryFile(encodedBinPath);
        String decoded = decode(encodedText, encodings);
        System.out.println("Texte décodé depuis fichier : " + decoded);
    }
    

    public static List<Encoding> getEncodings(Node root) {
        List<Encoding> encodings = new ArrayList<>();
        generateCode(root, "", encodings);
        return encodings;
    }

    public static String readTextFile(String filePath) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath))).trim();
    }

    // Convertit un string binaire en tableau de bytes
    public static byte[] binaryStringToByteArray(String binaryString) {
        int byteLength = (binaryString.length() + 7) / 8;
        byte[] byteArray = new byte[byteLength];

        for (int i = 0; i < binaryString.length(); i++) {
            int byteIndex = i / 8;
            byteArray[byteIndex] <<= 1;
            if (binaryString.charAt(i) == '1') {
                byteArray[byteIndex] |= 1;
            }
        }

        int lastBits = binaryString.length() % 8;
        if (lastBits != 0) {
            byteArray[byteArray.length - 1] <<= (8 - lastBits);
        }
        return byteArray;
    }

    // Écrit la longueur + le contenu dans le fichier
    public static void writeBinaryToFile(String binaryString, String filePath) throws IOException {
        byte[] data = binaryStringToByteArray(binaryString);
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath))) {
            dos.writeInt(binaryString.length()); // 4 octets = longueur en bits
            dos.write(data); // données encodées
        }
    }

    public static String readBinaryFile(String filePath) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            int bitLength = dis.readInt(); // Lire les 4 premiers octets
            StringBuilder binaryString = new StringBuilder();

            int byteRead;
            while ((byteRead = dis.read()) != -1) {
                String binary = String.format("%8s", Integer.toBinaryString(byteRead & 0xFF)).replace(' ', '0');
                binaryString.append(binary);
            }

            // Tronquer à la vraie longueur de bits
            return binaryString.substring(0, bitLength);
        }
    }
}
