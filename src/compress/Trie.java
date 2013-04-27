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
	
	public Trie(int maxbits) {
		top = new HashMap<Byte, TrieNode>(300);
		first = true;
		maxCount = (1 << maxbits) - 1;
		dictionaryCount = 0;
		isFull = false;
	}
	
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

	public TrieNode getNode(Byte c) {
		if (first) {
			if (top.containsKey(c)) {
				first = false;
				child = top.get(c);
				return child;
			}
		} else {
			TrieNode tn = child.getNode(c);
			if (tn != null) {
				child = tn;
				return child;
			}
		}
		return null;
	}
	
	public void setFirst() {
		first = true;
	}
	
	public int getPosition() {
		if (first) 
			return 0;
		else
			return child.position;
	}
	
}
