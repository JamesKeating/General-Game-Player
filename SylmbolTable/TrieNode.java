package SylmbolTable;

import java.util.ArrayList;

//node of the trie
public class TrieNode {
	
	private int nodeID;
	private boolean isIdentifier = false, isKey = false;
	private ArrayList<TrieNode> children = new ArrayList<TrieNode>();
	private char nodeValue;
	private TrieNode parent;


	public TrieNode(char nodeValue, TrieNode parent){
		this.parent = parent;
		this.nodeValue = nodeValue;
		parent.addChild(this);

	}

	public TrieNode(char nodeValue){
		this.nodeValue = nodeValue;
	}
	
	public int getNodeID() {
		return nodeID;
	}

	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	public boolean isIdentifier() {
		return isIdentifier;
	}

	public void setIdentifier(boolean isIdentifier) {
		this.isIdentifier = isIdentifier;
	}

	public ArrayList<TrieNode> getChildren() {
		return children;
	}

	public void addChild(TrieNode child) {
		this.children.add(child);
	}
	
	public TrieNode hasChild(char childValue) {
		for (TrieNode child : this.children){
			if(child.getNodeValue() == childValue){
				return child;
			}
		}
		return null;
	}

	public char getNodeValue() {
		return nodeValue;
	}

	public void setNodeValue(char nodeValue) {
		this.nodeValue = nodeValue;
	}

	public boolean isKey() {
		return isKey;
	}

	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}

	public TrieNode getParent() {
		return parent;
	}

	public void setParent(TrieNode parent) {
		this.parent = parent;
	}
}
