package model;

import java.util.ArrayList;
import java.util.List;

public class Language 
{
    private List<String> words;

    public Language(List<String> words) {
        this.words = words;
    }

    public Language() {
        words = new ArrayList<>();
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }    

    public void add(String word) {
        if (!contains(word)) {
            words.add(word);
        }
    }

    public void remove(String word) {
        words.remove(word);
    }

    public int size() {
        return words.size();
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public Language leftQuotient(Language language) {
        Language result = new Language();
        for (String prefix : language.getWords()) {
            for (String word : words) {
                if (word.startsWith(prefix)) {
                    String suffix = word.substring(prefix.length());
                    result.add(suffix);
                }
            }
        }
        return result;
    }

    public Language union(Language language){
        Language result = new Language();
        for (String word : words) {
            result.add(word);
        }
        for (String word : language.getWords()) {
            result.add(word);
        }
        return result;
    }

    public boolean isCode() {
        List<Language> sequence = new ArrayList<>();
        sequence.add(this); // L0
        Language L1 = leftQuotient(this);
        L1.remove("");
        if (L1.size() == 0) {
            return true;
        }
        sequence.add(L1); // L1
        int n = 2;
        while (true) {
            Language previous = sequence.get(n-1);
            Language Ln = previous.leftQuotient(this).union(this.leftQuotient(previous));
            if (Ln.contains("")) {
                return false;
            }
            for (Language language : sequence) {
                if (language.getWords().equals(Ln.getWords())) {
                    return true;
                }
            }
            sequence.add(Ln);
            n++;
        }
    }
}
