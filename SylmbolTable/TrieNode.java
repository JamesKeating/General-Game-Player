package SylmbolTable;

import java.util.ArrayList;

public class TrieNode {
	
	private int nodeID;
	private boolean isIdentifier, isKey;
	private ArrayList<TrieNode> children = new ArrayList<TrieNode>();
	private char nodeValue;
		

	//TODO: give default values
	public TrieNode(){
		
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
	
	public boolean hasChild(char childValue) {
		for (TrieNode child : this.children){
			if(child.getNodeValue() == childValue){
				return true;
			}
		}
		return false;
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
	
}
