package compress;

import java.util.TreeMap;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class TrieNode {
	public TreeMap<Byte, TrieNode> children;
	public byte node;
	public int position;

	public TrieNode(byte c, int pos) {
		children = new TreeMap<Byte, TrieNode>();
		node = c;
		position = pos;
	}
	
	public void addNode(Byte c, int pos) {
		children.put(c, new TrieNode(c, pos));
	}
	
	public TrieNode getNode(Byte c) {
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
