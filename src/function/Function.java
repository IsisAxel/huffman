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
        // List<Encoding> encodings = loadEncodingsFromFile(encodingsPath);
        List<Encoding> encodings = new ArrayList<>();
        encodings.add(new Encoding("11101011", 'v'));
        encodings.add(new Encoding("1110010", 'W'));
        encodings.add(new Encoding("1110111", ':'));
        encodings.add(new Encoding("1010011", '\''));
        encodings.add(new Encoding("0001", 'l'));
        encodings.add(new Encoding("011", 'e'));
        encodings.add(new Encoding("001110", 'w'));
        encodings.add(new Encoding("1110001", 'q'));
        encodings.add(new Encoding("11101010", 'p'));
        encodings.add(new Encoding("11101100", 'L'));
        encodings.add(new Encoding("11111", 'h'));
        encodings.add(new Encoding("0010", 'a'));
        encodings.add(new Encoding("10101", 'u'));
        encodings.add(new Encoding("00110", 'b'));
        encodings.add(new Encoding("1110011", 'k'));
        encodings.add(new Encoding("1110100", ','));
        encodings.add(new Encoding("1000", 'm'));
        encodings.add(new Encoding("00000", '\n'));
        encodings.add(new Encoding("1010010", '.'));
        encodings.add(new Encoding("1011", 'r'));
        encodings.add(new Encoding("0101", 'i'));
        encodings.add(new Encoding("00001", 'y'));
        encodings.add(new Encoding("110", ' '));
        encodings.add(new Encoding("1001", 't'));
        encodings.add(new Encoding("1110000", 'g'));
        encodings.add(new Encoding("001111", 'd'));
        encodings.add(new Encoding("11110", 'n'));
        encodings.add(new Encoding("11101101", 'f'));
        encodings.add(new Encoding("0100", 'o'));
        encodings.add(new Encoding("101000", 's'));

        String encodedText = readBinaryFile(encodedBinPath);
        encodedText = "111011000010001011010100011111001000001101001100101100011100010111000101011010011101011111011010000100101011001000100101110001110001100010000101111011100110100101010001010010101111101101000010010101100011100110101111100111100000000000111001011111011101101111001001001111110111011110100001111110110001100001010111110001111000100001110111011010100000100010100001110110100111111011110100110111010110011111111101001101011011100001110000011001110111110111110111100100100111111010111110111000011001011010001101001101110101011101001000000000001110010111110111011011110010010011111101110111101000011111101100010101101111000010101100001011001011001111110001100000111010000100101100100001010110010000111001001011110000100100011101110100110101101110000111000001100111011111011111001111101011011101100001100111111010111110111000011001011010001101110101001110111000010110011001011001111101001000000";
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
