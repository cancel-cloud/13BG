package de.lukas.datastructures;

public class SinglyLinkedList<T> {
	
	private static class Node<T> {
		private T data;

		private Node<T> next;

		public Node(T data) {
			this.data = data;
		}
	}
	
	private Node<T> head = null;
	
	public void add(T data) {
	}
	
	public void insert(int index, T data) {
	}
	
	public void set(int index, T data) {
	}

	public T get(int index) {
		T data = null;
		return data;
	}
	
	public boolean remove(int index) {
		return true;
	}
	
	public boolean remove(T data) {
		return true;
	}
	
	public int index (T Data) {
		int indexOfData = -1;
		return indexOfData;
	}
}
