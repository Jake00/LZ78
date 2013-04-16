package compress;

import java.util.HashMap;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class Trie {
	HashMap<Character, TrieNode> top;
	
	public Trie() {
		top = new HashMap<Character, TrieNode>(300);
	}
	
	public void addNode(Character c) {
		top.put(c, new TrieNode(c));
	}
	
	public TrieNode getNode(Character c) {
		if(top.containsKey(c)) 
			return top.get(c);
		else
			return null;
	}

}
