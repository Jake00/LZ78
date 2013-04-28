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

	/**
	 * Constructs a new node for use in our dictionary. This
	 * is called by this node's parent.
	 * @param c The byte this node is referencing.
	 * @param pos A unique number representing the phrase that
	 * this node refers to.
	 */
	public TrieNode(byte c, int pos) {
		children = new TreeMap<Byte, TrieNode>();
		node = c;
		position = pos;
	}
	
	/**
	 * Adds a new child node (in relation to this node).
	 * @param c The byte that this node is referencing.
	 * @param pos A unique number representing the phrase that
	 * this node refers to.
	 */
	public void addNode(Byte c, int pos) {
		children.put(c, new TrieNode(c, pos));
	}
	
	/**
	 * Gets the node given, if it exists.
	 * @param c The byte that references the next node.
	 * @return The TrieNode, or null if it does not exist.
	 */
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
