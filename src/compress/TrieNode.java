package compress;

import java.util.TreeMap;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class TrieNode {
	public TreeMap<Integer, TrieNode> children;
	public int nodeindex;
	public char nodevalue;

	public TrieNode(Integer i, Character c) {
		children = new TreeMap<Integer, TrieNode>();
		nodeindex = i;
		nodevalue = c;
	}
	
	public void addNode(Integer i, Character c) {
		children.put(i, new TrieNode(i,c));
	}
	
	public TrieNode getNode(Integer i) {
		if(children.containsKey(i))
			return children.get(i);
		else
			return null;
	}
	
	@Override
	public String toString() {
		return String.valueOf(nodevalue);
	}
}
