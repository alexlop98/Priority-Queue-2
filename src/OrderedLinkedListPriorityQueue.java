/**
 * Program #2
 * Ordered LinkedList implementation of PQ
 * CS310
 * 03/15/2018
 * @author jesuslopez cssc0736
 */
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedLinkedListPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
	// private static final int DEFAULT_MAX_CAPACITY = 1000;

	private Node<E> element;
	private Node<E> head;
	private int currentSize;
	private long modCounter;

	public OrderedLinkedListPriorityQueue() {
		head = null;
		currentSize = 0;
		modCounter = 0;
	}

	@Override
	// Inserts a new object into the priority queue. Returns true
	// if the insertion is successful.
	public boolean insert(E data) {
		Node<E> newNode = new Node<E>(data);
		Node<E> previous = null, current = head;
		while (current != null && ((Comparable<E>) data).compareTo(current.data) >= 0) {
			previous = current;
			current = current.next;
		}
		if (previous == null) { // it goes in the first place or list is empty
			newNode.next = head; // it goes in the first place. If empty newNode.next will be set to null
									// and head will be set to newNode
			head = newNode; // list is empty, head must be set
		} else { // in the middle or end
			previous.next = newNode;
			newNode.next = current;
		}
		modCounter++;
		currentSize++;
		return true;
	}

	@Override
	// Removes the object of highest priority that has been in the PQ
	// the longest. Returns null if PQ is empty.
	public E remove() { // removing at the head
		if (isEmpty())
			return null;// check for is empty
		E tmp = head.data;
		if (head.next == null)
			head = null;
		else {
			head = head.next;
		}
		currentSize--;
		modCounter++;
		return tmp;
	}

	@Override
	public boolean delete(E obj) {
		modCounter++;
		return deleteHelp(obj);
	}

	// Deletes all instances of the parameter obj from the PQ if found,
	// and returns true. Returns false if no match is found.
	public boolean deleteHelp(E obj) {
		if (!contains(obj))
			return false;
		Node<E> previous = null;
		Node<E> current = head;
		Node<E> prev = null;
		while (current != null && ((Comparable<E>) current.data).compareTo(obj) <= 0) { 
			// looks for all instances of the obj until it is equal to
			if (((Comparable<E>) current.data).compareTo(obj) == 0) {
				prev = current;
				currentSize--;
			}
			if (prev == null) {
				previous = current;
			}
			current = current.next;

		}
		if (prev != null) {
			if (previous == null) {
				head = prev.next;
			} else {
				previous.next = prev.next;
			}
			return true;
		}
		return false;
	}

	@Override
	// Returns the object of highest priority that has been in the
	// PQ the longest
	// false otherwise
	public E peek() {
		if (isEmpty())
			return null;
		return head.data;
	}

	@Override
	// Returns true if the priority queue contains the specified element
	// false otherwise
	public boolean contains(E obj) {
		Node<E> tmp = head;
		while (tmp != null) {
			if (tmp.data.compareTo(obj) == 0)
				return true;
			tmp = tmp.next;
		}
		return false;
	}

	@Override
	// Returns the number of objects currently in the priority queue
	public int size() {
		return currentSize;
	}

	@Override
	// Returns the priority queue to an empty state
	public void clear() {
		head = null;
		currentSize = 0;
	}

	@Override
	// Returns true if the priority queue is empty
	public boolean isEmpty() {
		if (currentSize == 0)
			return true;
		return false;
	}

	@Override
	// LinkedList should never be full
	public boolean isFull() {
		return false;
	}

	private class Node<E> {
		E data;
		Node<E> next;

		public Node(E data) {
			this.data = data;
			next = null;
		}
	}

	@Override
	public Iterator iterator() {
		return new LLIteratorHelper();
	}

	class LLIteratorHelper implements Iterator<E> {

		private Node<E> current;
		private long modCount;

		public LLIteratorHelper() {
			current = head;
			modCount = modCounter;
		}

		public boolean hasNext() {
			if (modCount != modCounter)
				throw new ConcurrentModificationException();
			return current != null;
		}

		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			E data = current.data;
			current = current.next;
			return data;
		}
	}

}