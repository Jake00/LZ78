package compress;

import java.util.HashMap;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class Trie {
	HashMap<Integer, TrieNode> top;
	
	public Trie() {
		top = new HashMap<Integer, TrieNode>(300);
	}
	
	public void addNode(Integer i, Character c) {
		top.put(i, new TrieNode(i,c));
	}
	
	public TrieNode getNode(Character c) {
		if(top.containsKey(c)) 
			return top.get(c);
		else
			return null;
	}

}
