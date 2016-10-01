package SylmbolTable;


public class Trie {
	
	private TrieNode rootNode = new TrieNode();
	private TrieNode currNode = this.rootNode;
	private int trieSize = 0;
	private boolean key = false;
	
	public Trie(){
		
	}
	
	public Trie(String[] keywords){
			this.key = true;  
			for(String keyword: keywords)
				processString(keyword, true);
	}
	
	public void insertNode(char nodeValue,TrieNode parent){
		TrieNode newNode = new TrieNode(nodeValue);
		newNode.setNodeID(trieSize);
		parent.addChild(newNode);
	}
	
	public void processChar(char character, boolean flag){
		
		if(!this.currNode.hasChild(character)){
			if(flag)
				this.insertNode(character, currNode);
			else{
				this.currNode = null;
				return;
			}
		}
		
		int lastIndex = this.currNode.getChildren().size() -1;
		this.currNode = this.currNode.getChildren().get(lastIndex);
	}
	
	public int getIdentifier(boolean flag){
		if(flag || (this.currNode != null && this.currNode.isIdentifier())){
			if(key)
				this.currNode.setKey(true);
			else
				this.currNode.setIdentifier(true);
			this.currNode = this.rootNode;
			return this.currNode.getNodeID();
		}
		this.currNode = this.rootNode;
		return -1;
	}
	
	public int processString(String str, boolean flag){
		for (char c : str.toCharArray()){
			processChar(c, flag);
		}
		return getIdentifier(flag);
	}

}
