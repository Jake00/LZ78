package compress;

import java.util.HashMap;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class Trie {
	HashMap<Byte, TrieNode> top;
	
	public Trie() {
		top = new HashMap<Byte, TrieNode>(300);
	}
	
	public void addNode(Byte c, int pos) {
		top.put(c, new TrieNode(c, pos));
	}
	
	public TrieNode getNode(Byte c) {
		if(top.containsKey(c)) 
			return top.get(c);
		else
			return null;
	}

}
