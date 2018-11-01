/**
 * Program #2
 * Unordered LinkedList implementation of PQ
 * CS310
 * 03/15/2018
 * @author jesuslopez cssc0736
 */
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnorderedLinkedListPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
	// private static final int DEFAULT_MAX_CAPACITY = 1000;

	private Node<E> head;
	private int currentSize;
	private long modCounter;

	public UnorderedLinkedListPriorityQueue() {
		head = null;
		currentSize = 0;
		modCounter = 0;
	}

	@Override
	// Inserts a new object into the priority queue. Returns true
	// if the insertion is successful.
	public boolean insert(E data) {
		Node<E> newNode = new Node<E>(data);
		if (head == null)
			head = newNode; // how to fix this for NullPointerException?
		else {
			newNode.next = head; // insert at the head
			head = newNode;
		}
		currentSize++;
		return true;
	}

	@Override
	// Removes the object of highest priority that has been in the PQ
	// the longest. Returns null if PQ is empty.
	public E remove() {
		if (head == null)
			return null;
		Node<E> current = head;
		Node<E> previous = head;
		Node<E> highest = head;
		Node<E> pHigh = head;
		while (current != null) {
			if (current.data.compareTo(highest.data) <= 0) {
				highest = current;
				pHigh = previous;
			}
			previous = current;
			current = current.next;
		}
		if (highest == head) {
			head = highest.next;
		} else {
			pHigh.next = highest.next;
		}
		currentSize--;
		modCounter++;
		return highest.data;
	}

	@Override
	// Deletes all instances of the parameter obj from the PQ if found,
	// and returns true. Returns false if no match is found.
	public boolean delete(E obj) { // same for ordered, except for ordered need to be changed for efficiency
		boolean removed = false;
		Node<E> current = head;
		Node<E> previous = null;

		while (current != null) {
			if (((Comparable<E>) obj).compareTo(current.data) == 0) {
				if (current == head) {
					head = current.next;
				} else {
					previous.next = current.next;
				}
				currentSize--;
				modCounter++;
				removed = true;
				current = current.next;
			} else {
				previous = current;
				current = current.next;
			}
		}
		return removed;
	}

	@Override
	// Returns the object of highest priority that has been in the
	// PQ the longest, false otherwise.
	public E peek() {
		if (head == null)
			return null;
		Node<E> current = head;
		Node<E> highP = head;
		while (current.next != null) {
			if (((Comparable<E>) current.data).compareTo(highP.data) <= 0) {
				highP = current;
			}
			current = current.next;
		}
		return highP.data;
	}

	@Override
	// Returns true if the priority queue contains the specified element
	// false otherwise
	public boolean contains(E obj) {
		Node<E> tmp = head;
		while (tmp != null) {
			if (tmp.data.equals(obj))
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
		currentSize = 0;
		head = null;
	}

	@Override
	// Returns true if the priority queue is empty
	public boolean isEmpty() {
		return head == null;
	}

	@Override
	// LinkedList should never be full
	public boolean isFull() {
		return false;
	}

	private class Node<T> {
		T data;
		Node<T> next;

		public Node(T data) {
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
