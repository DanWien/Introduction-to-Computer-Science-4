/*
I, <Dan Wiener> (<209413640>), assert that the work I submitted is entirely my own.
I have not received any part from any other student in the class,
nor did I give parts of it for use to others.
I realize that if my work is found to contain code that is not originally my own,
 a formal case will be opened against me with the BGU disciplinary committee.
*/

import java.util.LinkedList;
import java.util.Iterator;

public class BitList extends LinkedList<Bit> {
	private int numberOfOnes;

	// Do not change the constructor
	public BitList() {
		numberOfOnes = 0;
	}

	// Do not change the method
	public int getNumberOfOnes() {
		return numberOfOnes;
	}

//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.1 ================================================

	// Using super to call LinkedList methods , while making sure we do not add null 
	// And updating the numberOfOnes according to our action
	public void addLast(Bit element) {
		if (element == null)
			throw new IllegalArgumentException("Input is null");
		super.addLast(element);
		if (element.equals(Bit.ONE))
			this.numberOfOnes = this.numberOfOnes + 1;
	}

	public void addFirst(Bit element) {
		if (element == null)
			throw new IllegalArgumentException("Input is null");
		super.addFirst(element);
		if (element.equals(Bit.ONE))
			this.numberOfOnes = this.numberOfOnes + 1;
	}

	public Bit removeLast() {
		Bit output;
		output = super.removeLast();
		if (output.equals(Bit.ONE))
			this.numberOfOnes = this.numberOfOnes - 1;
		return output;
	}

	public Bit removeFirst() {
		Bit output;
		output = super.removeFirst();
		if (output.equals(Bit.ONE))
			this.numberOfOnes = this.numberOfOnes - 1;
		return output;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.2
	// ================================================
	public String toString() {
		String output = "";
		Iterator<Bit> iter = this.iterator();
		// Iterating over the BitLlist and concatenating to the string and finally adding the diamond parenthesis.
		while (iter.hasNext())
			output = iter.next() + output;
		return "<" + output + ">";
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.3
	// ================================================
	public BitList(BitList other) {
		// Making sure we do not copy a null BitList , and the numberOfOnes is updated via addLast action.
		if (other == null)
			throw new IllegalArgumentException("Input BitList is Null");
		this.numberOfOnes = 0;
		Iterator<Bit> iter = other.iterator();
		while (iter.hasNext()) {
			this.addLast(iter.next());
		}
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.4
	// ================================================
	public boolean isNumber() {
		boolean output = false;
		// Making sure the BitList is not empty via size , and then checking the other terms.
		if (this.size() > 0) {
			if ((this.numberOfOnes > 1 || this.getLast().equals(Bit.ZERO))) {
				output = true;
			}
		}
		return output;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.5
	// ================================================
	public boolean isReduced() {
		boolean minimal = false;
		String Number = this.toString();
		// First checking if the input is a legal number , and then if at least one of the terms is met.
		if (this.isNumber()) {
			if (this.size() <=2 & this.numberOfOnes > 0 | 
				Number.length() == 3 & this.numberOfOnes == 0)
				minimal = true;
			else if(!minimal) {
				if ((Number.charAt(1) == '0' && Number.charAt(2) == '1')| 
					(Number.charAt(1) == '1' && Number.charAt(2) == '0'))
					minimal = true;
				else if(!minimal)
					if (this.numberOfOnes == 2 && Number.charAt(1) == '1' && Number.charAt(2) == '1')
						minimal = true;
			}
		}
		return minimal;
	}

	public void reduce() {
		// We remove the last bit until the number is reduced.
		while (!(this.isReduced()))
			this.removeLast();
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.6
	// ================================================
	public BitList complement() {
		BitList comp = new BitList();
		Iterator<Bit> iter = this.iterator();
		for (int i = 0; i<size() ; i = i+1) {
			// Using the Bit.negate() to return the complement bit.
			comp.addLast(iter.next().negate());
		}
		return comp;
			
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.7
	// ================================================
	public Bit shiftRight() {
		Bit LSB = this.getFirst();
		this.removeFirst();
		return LSB;
	}

	public void shiftLeft() {
		this.addFirst(Bit.ZERO);
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.8
	// ================================================
	public void padding(int newLength) {
		// Making sure we are not padding an empty BitList
		if (this.size() == 0)
			throw new IllegalArgumentException("BitList is Empty");
		// Adding the most left bit n times , n = input - current length
		Bit addition = super.getLast();
		for (int i = size(); i <newLength; i = i + 1) {
			super.addLast(addition);
		}
	}

	// ----------------------------------------------------------------------------------------------------------
	// The following overriding methods must not be changed.
	// ----------------------------------------------------------------------------------------------------------
	public boolean add(Bit e) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public void add(int index, Bit element) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public Bit remove(int index) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public boolean offer(Bit e) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public boolean offerFirst(Bit e) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public boolean offerLast(Bit e) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public Bit set(int index, Bit element) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Do not use this method!");
	}
}
	