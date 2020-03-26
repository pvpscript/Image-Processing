package dataStructures;

public class BinaryTree<T> {
	private T content;
	private BinaryTree<T> left; // below
	private BinaryTree<T> right; // above
	
	public BinaryTree(T content) {
		this.content = content;
		
		this.left = null;
		this.right = null;
	}
	
	public BinaryTree<T> putLeft(T content) {
		this.left = new BinaryTree<T>(content);
		
		return this.left;
	}
	
	public BinaryTree<T> putRight(T content) {
		this.right = new BinaryTree<T>(content);
		
		return this.right;
	}
	
	public BinaryTree<T> getLeft() {
		return this.left;
	}
	
	public BinaryTree<T> getRight() {
		return this.right;
	}
	
	public T getContent() {
		return this.content;
	}
}
