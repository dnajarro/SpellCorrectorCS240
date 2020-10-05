package spell;

import java.util.Set;
import java.util.TreeSet;

public class Trie implements ITrie {
    private Node[] root;
    private int nodeCount;
    private int wordCount;
    private StringBuilder sb;

    public Trie() {
        this.root = new Node[26];
        this.nodeCount = 1;
        this.wordCount = 0;
        sb = new StringBuilder();
    }

    public Node[] getRoot() {
        return root;
    }

    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count
     *
     * @param word The word being added to the trie
     */
    public void add(String word) {
        Node[] current = root;
        Node endNode = null;
        int i = 0;
        while (i < word.length()) {
            char c = word.charAt(i);
            int index = c - 'a';
            if (current[index] == null) {
                Node n = new Node();
                current[index] = n;
                endNode = n;
                incrNodeCount();
                current = n.getNodes();
            }
            else {
                endNode = current[index];
                current = current[index].getNodes();
            }
            i++;
        }
        endNode.incrValue();
        incrWordCount();
    }

    /**
     * Searches the trie for the specified word
     *
     * @param word The word being searched for
     *
     * @return A reference to the trie node that represents the word,
     * 			or null if the word is not in the trie
     */
    public INode find(String word) {
        Node[] current = root;
        Node endNode = null;
        int i = 0;
        while (i < word.length()) {
            char c = word.charAt(i);
            int index = c - 'a';
            if (current[index] == null) {
                return null;
            }
            else {
                endNode = current[index];
                current = current[index].getNodes();
            }
            i++;
        }
        if (endNode != null) {
            if (endNode.getValue() > 0) {
                return endNode;
            }
        }
        return null;
    }

    /**
     * Returns the number of unique words in the trie
     *
     * @return The number of unique words in the trie
     */
    public int getWordCount() {
        return wordCount;
    }

    public void incrWordCount() { wordCount++; }

    public void incrNodeCount() {   nodeCount++; }

    /**
     * Returns the number of nodes in the trie
     *
     * @return The number of nodes in the trie
     */
    public int getNodeCount() {
        return nodeCount;
    }

    public void outputWord(StringBuilder currentSB, Node n, char c) {
        StringBuilder SBcopy = new StringBuilder();
        SBcopy.append(currentSB.toString() + c);
        if (n.getValue() > 0) {
            sb.append(SBcopy.toString() + '\n');
        }
        Node[] current = n.getNodes();
        for (int i = 0; i < 26; i++) {
            int index = 'a' + i;
            char x = (char) index;
            if (current[i] != null) {
                outputWord(SBcopy, current[i], x);
            }
        }
    }

    /**
     * The toString specification is as follows:
     * For each word, in alphabetical order:
     * <word>\n
     */
    @Override
    public String toString() {
        StringBuilder currentSB = new StringBuilder();
        Node[] current = getRoot();
        for (int i = 0; i < 26; i++) {
            int index = 'a' + i;
            char c = (char) index;
            if (current[i] != null) {
                outputWord(currentSB, current[i], c);
            }
            currentSB.setLength(0);
        }
        String output = sb.toString();
        sb.setLength(0);
        return output;
    }

    @Override
    public int hashCode() {
        return ((nodeCount * 13) + (wordCount * 31));
    }

    public boolean matchNodes(Node n1, Node n2) {
        if (n1.getValue() != n2.getValue()) {
            return false;
        }
        Node[] current1 = n1.getNodes();
        Node[] current2 = n2.getNodes();
        for (int i = 0; i < 26; i++) {
            if (current1[i] != null && current2[i] != null) {
                return matchNodes(current1[i], current2[i]);
            }
            else if (current1[i] == null && current2[i] == null) {
                continue;
            }
            else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Trie t = (Trie) o;
        if ((t.getWordCount() == getWordCount()) && (t.getNodeCount() == getNodeCount())) {
            Node[] current1 = getRoot();
            Node[] current2 = t.getRoot();
            for (int i = 0; i < 26; i++) {
                if (current1[i] != null && current2[i] != null) {
                    if (!matchNodes(current1[i], current2[i])) {
                        return false;
                    }
                }
                else if (current1[i] == null && current2[i] == null){
                    continue;
                }
                else {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Your trie node class should implement the ITrie.INode interface
     */
    public class Node implements INode {
        private int frequency;
        private Node[] nodes;

        /**
         * Returns the frequency count for the word represented by the node
         *
         * @return The frequency count for the word represented by the node
         */
        public Node() {
            this.frequency = 0;
            nodes = new Node[26];
        }

        public int getValue() {
            return frequency;
        }

        public void incrValue() {
            frequency++;
        }

        public Node getNode(char c) {
            int index = c - 'a';
            if (index < 26 && index >= 0) {
                return nodes[index];
            } else {
                return null;
            }
        }

        public Node[] getNodes() {
            return nodes;
        }
    }
	
}
