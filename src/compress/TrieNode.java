package compress;

import java.util.TreeMap;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class TrieNode {
	public TreeMap<Character, TrieNode> children;
	public char node;

	public TrieNode(char c) {
		children = new TreeMap<Character, TrieNode>();
		node = c;
	}
	
	public void addNode(Character c) {
		children.put(c, new TrieNode(c));
	}
	
	public TrieNode getNode(Character c) {
		if(children.containsKey(c))
			return children.get(c);
		else
			return null;
	}
	
	@Override
	public String toString() {
		return String.valueOf(node);
	}
}
