package compress;

import java.util.HashMap;

/**
 * @author Jake Bellamy 1130587 jrb46
 * @author Michael Coleman 1144239 mjc62
 */
public class Trie {
	private HashMap<Byte, TrieNode> top;
	private TrieNode child;
	private boolean first;
	private int maxCount;
	private int dictionaryCount;
	public boolean isFull;
	
	/**
	 * Constructs a new Trie dictionary for efficient lookups
	 * of prefix bytes. Each node represents one byte.
	 * @param maxbits The maximum number of bits to use for
	 * storing elements in this dictionary.
	 */
	public Trie(int maxbits) {
		top = new HashMap<Byte, TrieNode>(300);
		first = true;
		maxCount = (1 << maxbits) - 1;
		dictionaryCount = 0;
		isFull = false;
	}
	
	/**
	 * Adds a new node to this dictionary only if it is not full.
	 * @param c The byte to add.
	 * @param pos A unique number representing this node in the 
	 * dictionary. Also referred to as the phrase number.
	 */
	public void addNode(Byte c, int pos) {
		if (first) {
			top.put(c, new TrieNode(c, pos));
		} else if (!isFull) {
			child.addNode(c, pos);
			if (++dictionaryCount >= maxCount) {
				isFull = true;
			}
		}
	}

	/**
	 * Gets the next node from the dictionary if it exists. 
	 * @param c The byte to get.
	 * @return The node found, or null if it does not yet exist.
	 */
	public TrieNode getNode(Byte c) {
		/**
		 * Check that we are searching through the top level of
		 * the dictionary, ie. this is the first character in the
		 * phrase we are looking for.
		 */
		if (first) {
			if (top.containsKey(c)) {
				first = false;
				//Keep a link to the child we have found for the
				//next search.
				child = top.get(c);
				return child;
			}
		} else {
			TrieNode tn = child.getNode(c);
			if (tn != null) {
				//Update the link to be the next child node.
				child = tn;
				return child;
			}
		}
		return null;
	}
	
	/**
	 * Sets the dictionary to look through the top level for
	 * the next search.
	 */
	public void setFirst() {
		first = true;
	}
	
	/**
	 * Gets the phrase number of the current node.
	 * @return
	 */
	public int getPosition() {
		if (first) 
			return 0;
		else
			return child.position;
	}
	
}
