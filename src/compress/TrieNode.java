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
	public int heightLevel;

	public TrieNode(byte c, int pos, int height) {
		children = new TreeMap<Byte, TrieNode>();
		node = c;
		position = pos;
		heightLevel = height;
	}
	
	public void addNode(Byte c, int pos) {
		children.put(c, new TrieNode(c, pos, heightLevel+1));
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
