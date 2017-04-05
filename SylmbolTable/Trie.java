package SylmbolTable;

//stores information about tokens for the parser
public class Trie {
	
	private TrieNode rootNode = new TrieNode('~');
	private TrieNode currNode = this.rootNode;
	private int trieSize = 0;
	private boolean key = false;
	
	public Trie(){
		
	}
	
	public Trie(String[] keywords){
			this.key = true;  
			for(String keyword: keywords)
				processString(keyword, true);

			this.key = false;
	}
	
	public void insertNode(char nodeValue,TrieNode parent){
		TrieNode newNode = new TrieNode(nodeValue, parent);
		newNode.setNodeID(trieSize);

	}
	
	public void processChar(char character, boolean flag){
		if(!Character.isLetterOrDigit(character) && character != '?')
			return;

		TrieNode node = this.currNode.hasChild(character);
		if(node == null && flag ){
			this.insertNode(character, currNode);
			int lastIndex = this.currNode.getChildren().size() -1;
			this.currNode = this.currNode.getChildren().get(lastIndex);
		}

		else
			this.currNode = node;
	}
	
	public TrieNode getNode(boolean flag){
		if(flag || (this.currNode != null && this.currNode.isIdentifier())){
			if(key)
				this.currNode.setKey(true);
			else
				this.currNode.setIdentifier(true);
		}
		TrieNode x = this.currNode;
		reset();
		return x;
	}
	
	public TrieNode processString(String str, boolean flag){
		for (char c : str.toCharArray()){
			processChar(c, flag);
		}
		return getNode(flag);
	}

	public void reset(){
		this.currNode = this.rootNode;
	}

	public String getString(){
		String str = "";
		TrieNode node = this.currNode;

		while (node != rootNode ) {
			str = node.getNodeValue() + str;
			node = node.getParent();
		}

		return str;
	}
}
