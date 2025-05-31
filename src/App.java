import function.Function;
import model.Language;

public class App {
    public static void main(String[] args) throws Exception {
        // Function.encodeFromFile("text.txt", "alphabet.txt", "output.bin", "encoding.bin");
        Function.decodeFromFile("output.bin", "encoding.bin");
        // Language L = new Language();
        // L.add("000");
        // L.add("010");
        // L.add("011");
        // L.add("01001");
        // System.out.println(L.isCode());
    }
}
