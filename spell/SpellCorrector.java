package spell;

import java.io.File;
import java.io.IOException;
import java.lang.String;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class SpellCorrector implements ISpellCorrector {

    private Trie dictionary;
    private Map<String, Integer> possibleWords;
    private Map<String, Integer> nonceWords;

    public SpellCorrector() {
        dictionary = new Trie();
        possibleWords = new TreeMap<String, Integer>();
        nonceWords = new TreeMap<String, Integer>();
    }

    /**
     * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     * @param dictionaryFileName File containing the words to be used
     * @throws IOException If the file cannot be read
     */
    public void useDictionary(String dictionaryFileName) throws IOException {
        File filename = new File(dictionaryFileName);
        Scanner in = new Scanner(filename);
        while (in.hasNext()) {
            String word = in.next();
            word = word.toLowerCase();
            dictionary.add(word);
        }
        in.close();
    }

    public Trie getTrie() {
        return dictionary;
    }

    /**
     * Suggest a word from the dictionary that most closely matches
     * <code>inputWord</code>
     * @param inputWord
     * @return The suggestion or null if there is no similar word in the dictionary
     */
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();
        if (dictionary.find(inputWord) != null) {
            return inputWord;
        }
        deleteLetter(inputWord, possibleWords, nonceWords);
        transposeLetter(inputWord, possibleWords, nonceWords);
        alterLetter(inputWord, possibleWords, nonceWords);
        insertLetter(inputWord, possibleWords, nonceWords);
        Map.Entry<String, Integer> maxFreqEntry = null;
        for (Map.Entry<String, Integer> entry : possibleWords.entrySet()) {
            if (maxFreqEntry == null || entry.getValue().compareTo(maxFreqEntry.getValue()) > 0) {
                maxFreqEntry = entry;
            }
        }
        if (maxFreqEntry == null) {
            Map<String, Integer> possibleWords2 = new TreeMap<String, Integer>();
            Map<String, Integer> nonceWords2 = new TreeMap<String, Integer>();
            for (Map.Entry<String, Integer> entry : nonceWords.entrySet()) {
                String nonce = entry.getKey();
                deleteLetter(nonce, possibleWords2, nonceWords2);
                transposeLetter(nonce, possibleWords2, nonceWords2);
                alterLetter(nonce, possibleWords2, nonceWords2);
                insertLetter(nonce, possibleWords2, nonceWords2);
            }
            for (Map.Entry<String, Integer> entry : possibleWords2.entrySet()) {
                if (maxFreqEntry == null || entry.getValue().compareTo(maxFreqEntry.getValue()) > 0) {
                    maxFreqEntry = entry;
                }
            }
            if (maxFreqEntry == null) {
                return null;
            }
        }
        return maxFreqEntry.getKey();
    }

    public void deleteLetter(String word, Map<String, Integer> map, Map<String, Integer> map2) {
        for (int i = 0; i < word.length(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(word);
            stringBuilder.deleteCharAt(i);
            String s = stringBuilder.toString();
            Trie.Node n = null;
            if (dictionary.find(s) != null) {
                n = (Trie.Node) dictionary.find(s);
            }
            if (n != null) {
                int freq = n.getValue();
                map.put(s, freq);
            }
            else {
                int freq = 0;
                map2.put(s, freq);
            }
        }
    }

    public void transposeLetter(String word, Map<String, Integer> map, Map<String, Integer> map2) {
        for (int i = 0; i < (word.length() - 1); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(word);
            char c = stringBuilder.charAt(i + 1);
            stringBuilder.deleteCharAt(i + 1);
            stringBuilder.insert(i, c);
            String s = stringBuilder.toString();
            Trie.Node n = (Trie.Node) dictionary.find(s);
            if (n != null) {
                int freq = n.getValue();
                map.put(s, freq);
            }
            else {
                int freq = 0;
                map2.put(s, freq);
            }
        }
    }

    public void alterLetter(String word, Map<String, Integer> map, Map<String, Integer> map2) {
        for (int i = 0; i < word.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder stringBuilder = new StringBuilder();
                int x = (int) 'a' + j;
                char c = (char) x;
                stringBuilder.append(word);
                stringBuilder.deleteCharAt(i);
                stringBuilder.insert(i, c);
                String s = stringBuilder.toString();
                Trie.Node n = (Trie.Node) dictionary.find(s);
                if (n != null) {
                    int freq = n.getValue();
                    map.put(s, freq);
                }
                else {
                    int freq = 0;
                    map2.put(s, freq);
                }
            }
        }
    }

    public void insertLetter(String word, Map<String, Integer> map, Map<String, Integer> map2) {
        for (int i = 0; i <= word.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder stringBuilder = new StringBuilder();
                int x = (int) 'a' + j;
                char c = (char) x;
                stringBuilder.append(word);
                stringBuilder.insert(i, c);
                String s = stringBuilder.toString();
                Trie.Node n = (Trie.Node) dictionary.find(s);
                if (n != null) {
                    int freq = n.getValue();
                    map.put(s, freq);
                }
                else {
                    int freq = 0;
                    map2.put(s, freq);
                }
            }
        }
    }
}
