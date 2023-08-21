/*
I, <Dan Wiener> (<209413640>), assert that the work I submitted is entirely my own.
I have not received any part from any other student in the class,
nor did I give parts of it for use to others.
I realize that if my work is found to contain code that is not originally my own,
 a formal case will be opened against me with the BGU disciplinary committee.
*/

import java.util.Iterator;

public class BinaryNumber implements Comparable<BinaryNumber> {
	private static final BinaryNumber ZERO = new BinaryNumber(0);
	private static final BinaryNumber ONE = new BinaryNumber(1);
	private BitList bits;

	// Copy constructor
	// Do not chainge this constructor
	public BinaryNumber(BinaryNumber number) {
		bits = new BitList(number.bits);
	}

	// Do not chainge this constructor
	private BinaryNumber(int i) {
		bits = new BitList();
		bits.addFirst(Bit.ZERO);
		if (i == 1)
			bits.addFirst(Bit.ONE);
		else if (i != 0)
			throw new IllegalArgumentException("This Constructor may only get either zero or one.");
	}

	// Do not chainge this method
	public int length() {
		return bits.size();
	}

	// Do not change this method
	public boolean isLegal() {
		return bits.isNumber() & bits.isReduced();
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.1
	// ================================================
	public BinaryNumber(char c) {
		if (c < '0' | c > '9')
			throw new IllegalArgumentException("Illegal Character Input");
		int cValue = c - '0';
		bits = new BitList();
		while (cValue > 0) {
			int digit = cValue % 2;
			bits.addLast(new Bit(digit));
			cValue = cValue / 2;
		}
		bits.addLast(new Bit(0));
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.2
	// ================================================
	public String toString() {
		// Do not remove or change the next two lines
		if (!isLegal()) // Do not change this line
			throw new RuntimeException("I am illegal.");// Do not change this line
		String output = "";
		// Concatenating every bit from the left to reverse the number.
		for (Bit a : this.bits) {
			output = a + output;
		}
		return output;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.3
	// ================================================
	public boolean equals(Object other) {
		boolean isEqual = true;
		if (!(other instanceof BinaryNumber))
			isEqual = false;
		// If other is a BinaryNumber and both numbers have the same length, we compare
		// every bit.
		if (isEqual) {
			if (this.length() != ((BinaryNumber) other).length())
				return false;
			Iterator<Bit> iter1 = this.bits.iterator();
			BinaryNumber casted = (BinaryNumber) other;
			Iterator<Bit> iter2 = casted.bits.iterator();
			while (iter1.hasNext() & iter2.hasNext() & isEqual) {
				if (!(iter1.next().equals(iter2.next())))
					isEqual = false;
			}
		}
		return isEqual;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.4
	// ================================================
	public BinaryNumber add(BinaryNumber addMe) {
		if (!(addMe.isLegal()) | !(this.isLegal()))
			throw new IllegalArgumentException("One of the Input BinaryNumbers is Illegal");
		// Creating two copies of our binary numbers to not change the original ones.
		BinaryNumber A = new BinaryNumber(this);
		BinaryNumber B = new BinaryNumber(addMe);
		// We pad according to the bigger length + 1 in case we have a final carry.
		if (A.length() > addMe.length()) {
			B.bits.padding(A.length() + 1);
			A.bits.padding(A.length() + 1);
		} else if (A.length() < B.length()) {
			A.bits.padding(B.length() + 1);
			B.bits.padding(B.length() + 1);
		} else {
			B.bits.padding(B.length() + 1);
			A.bits.padding(B.length());
		}
		Iterator<Bit> iter1 = A.bits.iterator();
		Iterator<Bit> iter2 = B.bits.iterator();
		Bit carry = Bit.ZERO;
		BinaryNumber sum = new BinaryNumber(0);
		// The bit we add every iteration is the sum of every two parallel bits with the
		// carry from the previous addition.
		while (iter1.hasNext() & iter2.hasNext()) {
			Bit bit1 = iter1.next();
			Bit bit2 = iter2.next();
			sum.bits.addLast(Bit.fullAdderSum(bit1, bit2, carry));
			carry = Bit.fullAdderCarry(bit1, bit2, carry);
		}
		sum.bits.shiftRight();
		sum.bits.reduce();
		return sum;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.5
	// ================================================
	public BinaryNumber negate() {
		if (!(this.isLegal()))
			throw new IllegalArgumentException("Input BinaryNumber is Illegal");
		// Negating zero equals zero.
		if (this.equals(ZERO))
			return ZERO;
		// Creating the complement BitList and referring our BinaryNumber to it.
		BitList complement = this.bits.complement();
		BinaryNumber negative = new BinaryNumber(0);
		negative.bits = complement;
		// If we complement numbers we might have an illegal number which won't be
		// accepted to the "add"
		// Function , so we add a '0' bit which will be eventually removed.
		if (!negative.bits.isNumber())
			negative.bits.addLast(Bit.ZERO);
		negative.bits.reduce();
		negative = negative.add(ONE);
		// If we negated a positive number and "negative" represents a positive number
		// We remove the '0' and it will now represent a negative number.
		if (negative.bits.getLast().equals(Bit.ZERO) & !(this.bits.getLast().equals(Bit.ONE)))
			negative.bits.removeLast();
		negative.bits.reduce();
		ONE.bits.reduce();
		return negative;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.6
	// ================================================
	public BinaryNumber subtract(BinaryNumber subtractMe) {
		if (!(this.isLegal()) | !(subtractMe.isLegal()))
			throw new IllegalArgumentException("One of the Input BinaryNumbers is Illegal");
		// Representing subtraction as a-b = a + (-b).
		BinaryNumber A = new BinaryNumber(this);
		BinaryNumber B = new BinaryNumber(subtractMe);
		if (!(subtractMe.equals(ZERO)))
			B = B.negate();
		BinaryNumber sub = A.add(B);
		sub.bits.reduce();
		return sub;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.7
	// ================================================
	public int signum() {
		if (!(this.isLegal()))
			throw new IllegalArgumentException("Input BinaryNumber is Illegal");
		// We use length methods in a way that the last else condition exactly
		// represents zero.
		int signum;
		if (this.bits.getLast().equals(Bit.ONE) & this.length() > 1)
			signum = -1;
		else if (this.bits.getLast().equals(Bit.ZERO) & this.length() > 1)
			signum = 1;
		else
			signum = 0;
		return signum;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.8
	// ================================================
	public int compareTo(BinaryNumber other) {
		int output;
		if (!(this.isLegal()) | !(other.isLegal()))
			throw new IllegalArgumentException("One of the Input BinaryNumbers is Illegal");
		BinaryNumber A = new BinaryNumber(this);
		BinaryNumber B = new BinaryNumber(other);
		// Representing all cases of positive/negative inputs and using subtract as
		// follows :
		// if both a & b are positive or negative -- > a-b is positive iff a is bigger
		// than b.
		if (A.signum() == 1 & B.signum() == -1)
			output = 1;
		else if (A.signum() == -1 & B.signum() == 1)
			output = -1;
		else if (A.equals(B))
			output = 0;
		else if (A.subtract(B).signum() == 1)
			output = 1;
		else
			output = -1;
		return output;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.9
	// ================================================
	public int toInt() {
		// Do not remove or change the next two lines
		if (!isLegal()) // Do not change this line
			throw new RuntimeException("I am illegal.");// Do not change this line
		// We are representing BinaryNumbers in a way that int.maxvalue and int.minvalue are exactly 32 bits
		if (this.length() > 32)
			throw new RuntimeException("Input is either bigger than INT.MAXVALUE or smaller than Int.MINVALUE");
		if (this.equals(ZERO))
			return 0;
		int power = 1;
		int output = 0;
		BinaryNumber A = new BinaryNumber(this);
		Bit MSB = A.bits.removeLast();
		Iterator<Bit> iter = A.bits.iterator();
		while (iter.hasNext()) {
			Bit curr = iter.next();
			if (curr.equals(Bit.ONE))
				output = output + 1 * power;
			power = power * 2;
		}
		// In negative binary numbers , we can calculate their decimal value by accounting all 1 bits as positive ,
		// Except for the sign bit taken as negative. (known from Numerical Systems course)
		output = output - MSB.toInt() * power;
		return output;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.10
	// ================================================
	// Do not change this method
	public BinaryNumber multiply(BinaryNumber multiplyMe) {
		if (!(this.isLegal()) | !(multiplyMe.isLegal()))
			throw new IllegalArgumentException("One of the Input BinaryNumbers is Illegal");
		// making sure we only insert positive values to our recursive multiplying function.
		BinaryNumber product = new BinaryNumber(this);
		if (this.signum() == 1 & multiplyMe.signum() == 1)
			product = product.multiplyPositive(multiplyMe);
		else if ((this.signum() == 1 & multiplyMe.signum() == -1)) {
			multiplyMe = multiplyMe.negate();
			product = product.multiplyPositive(multiplyMe).negate();
		}
		else if ((this.signum() == -1 & multiplyMe.signum() == 1)) {
			product = product.negate();
			product = product.multiplyPositive(multiplyMe).negate();
		}
		else {
			multiplyMe = multiplyMe.negate();
			product = product.negate();
			product = product.multiplyPositive(multiplyMe);
		}
		return product;
	}

	private BinaryNumber multiplyPositive(BinaryNumber multiplyMe) {
		BinaryNumber product = new BinaryNumber(this);
		if (this.equals(ZERO) | multiplyMe.equals(ZERO))
			return ZERO;
		else if (this.equals(ONE))
			return multiplyMe;
		else if (multiplyMe.equals(ONE))
			return this;
		else if (multiplyMe.bits.getFirst().equals(Bit.ONE))
			// for x*y , if we use Bit shifting and y is odd , we need to add y to the product.
			// The recursive call is before we make the addition and when it goes back up it
			// Adds y the correct amount of times.
			product = product.multiplyBy2().multiplyPositive(multiplyMe.divideBy2()).add(this);
		else
			product = product.multiplyBy2().multiplyPositive(multiplyMe.divideBy2());
		product.bits.reduce();
		return product;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.11
	// ================================================
	// Do not change this method
	public BinaryNumber divide(BinaryNumber divisor) {
		// Do not remove or change the next two lines
		if (divisor.equals(ZERO)) // Do not change this line
			throw new RuntimeException("Cannot divide by zero."); // Do not change this line
		// making sure we only insert positive values to our division function
		BinaryNumber A = new BinaryNumber(this);
		BinaryNumber B = new BinaryNumber(divisor);
		BinaryNumber Q = new BinaryNumber(0);
		if (A.signum() == 1 & B.signum() == 1)
			Q = (A.dividePositive(B));
		else if (A.signum() == -1 & B.signum() == 1) {
			A = A.negate();
			Q = (A.dividePositive(B)).negate();
		} else if (A.signum() == 1 & B.signum() == -1) {
			B = B.negate();
			Q = (A.dividePositive(B)).negate();
		} else if (A.signum() == -1 & B.signum() == -1) {
			A = A.negate();
			B = B.negate();
			Q = (A.dividePositive(B));
		}
		return Q;
	}

	private BinaryNumber dividePositive(BinaryNumber divisor) {
		// Implementing long binary division.
		if (this.equals(divisor))
			return ONE;
		BinaryNumber A = new BinaryNumber(this);
		BinaryNumber B = new BinaryNumber(divisor);
		BinaryNumber Q = new BinaryNumber(0);
		BinaryNumber tmp = new BinaryNumber(0);
		while (A.length() > 0) {
			if (tmp.compareTo(B) == -1) {
				Q.bits.addFirst(Bit.ZERO);
				tmp.bits.addFirst(A.bits.removeLast());
				tmp.bits.reduce();
			} else {
				Q.bits.reduce();
				Q = Q.add(ONE);
				tmp = tmp.subtract(B);
			}
		}
		if (tmp.compareTo(B) >= 0) {
			Q.bits.reduce();
			Q = Q.add(ONE);
		}
		Q.bits.reduce();
		return Q;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.12
	// ================================================
	public BinaryNumber(String s) {
		// Using BinaryNumbers to represent the power and the output.
		BinaryNumber power = new BinaryNumber(1);
		BinaryNumber ten = new BinaryNumber('9').add(ONE);
		BinaryNumber output = ZERO;
		boolean negative = false;
		if (s.charAt(0) == '-') {
			s = s.substring(1);
			negative = true;
		}
		// Calculating the number according to transition between decimal to binary.
		for (int i = 1; i <= s.length(); i = i + 1) {
			if ('0' <= s.charAt(s.length() - i) | s.charAt(s.length() - i) <= '9') {
				BinaryNumber currIdx = new BinaryNumber(s.charAt(s.length() - i));
				output = output.add(currIdx.multiply(power));
				power = power.multiply(ten);
			}
		}
		// If the number is negative we negate the output and assign it to bits.
		if (negative) {
			output = output.negate();
			bits = output.bits;
		} else
			bits = output.bits;
	}

	// =========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.13
	// ================================================
	public static int toInt(char c) {
		return "0123456789".indexOf(c);
	}

	public static String decimalIncrement(String s, String acc) {
		if (toInt(s.charAt(s.length() - 1)) != 9) {
			if (s.length() > 1) {
				acc = acc + s.substring(0, s.length() - 1) + (toInt(s.charAt(s.length() - 1)) + 1);
			} else
				acc = acc + (toInt(s.charAt(s.length() - 1)) + 1);
		} else if (toInt(s.charAt(s.length() - 1)) == 9) {
			if (s.length() > 1) {
				acc = decimalIncrement(s.substring(0, s.length() - 1), acc) + "0";
			} else
				acc = "10" + acc;
		}
		return acc;
	}

	public static String decimalDouble(String s, String acc, int carry) {
		if (toInt(s.charAt(s.length() - 1)) < 5) {
			if (s.length() > 1) {
				acc = (toInt(s.charAt(s.length() - 1)) * 2 + carry) + acc;
				acc = decimalDouble(s.substring(0, s.length() - 1), acc, 0);
			} else
				acc = (toInt(s.charAt(s.length() - 1)) * 2 + carry) + acc;
		} else if (toInt(s.charAt(s.length() - 1)) >= 5) {
			if (s.length() > 1) {
				acc = ((toInt(s.charAt(s.length() - 1)) * 2) % 10 + carry) + acc;
				acc = decimalDouble(s.substring(0, s.length() - 1), acc, 1);
			} else
				acc = "1" + ((toInt(s.charAt(s.length() - 1)) * 2 % 10 + carry)) + acc;
		}
		return acc;
	}

	public static String binary2Decimal(BinaryNumber B, String acc) {
		Iterator<Bit> iter = B.bits.iterator();
		while (iter.hasNext()) {
			if (iter.next().equals(Bit.ONE)) {
				acc = decimalDouble(acc, "", 0);
				acc = decimalIncrement(acc, "");
				B.bits.removeFirst();
				acc = binary2Decimal(B, acc);
			} else {
				acc = decimalDouble(acc, "", 0);
				B.bits.removeFirst();
				acc = binary2Decimal(B, acc);
			}
		}
		return acc;
	}

	public String toIntString() {
		// Do not remove or change the next two lines
		if (!isLegal()) // Do not change this line
			throw new RuntimeException("I am illegal.");// Do not change this line
		// Using methods that act on Strings. decimal Increment returns a string that is incremented 
		// By one and decimalDouble returns a string that is doubled. 
		// When we want to work on a BinaryNumber we must reverse it first.
		// The sign bit is irrelevant so we remove it and use our functions to return the decimal string.
		BinaryNumber A = new BinaryNumber(this);
		boolean negative = false;
		if (A.signum() == -1) {
			negative = true;
			A = A.negate();
		}
		A.bits.removeLast();
		BinaryNumber B = new BinaryNumber(0);
		B.bits.shiftRight();
		Iterator<Bit> iter = A.bits.iterator();
		while (iter.hasNext())
			B.bits.addFirst(iter.next());
		String decimal = binary2Decimal(B, "0");
		if (negative)
			decimal = "-" + decimal;
		return decimal;

	}

	// Returns this * 2
	public BinaryNumber multiplyBy2() {
		BinaryNumber output = new BinaryNumber(this);
		output.bits.shiftLeft();
		output.bits.reduce();
		return output;
	}

	// Returns this / 2;
	public BinaryNumber divideBy2() {
		BinaryNumber output = new BinaryNumber(this);
		if (!equals(ZERO)) {
			if (signum() == -1) {
				output.negate();
				output.bits.shiftRight();
				output.negate();
			} else
				output.bits.shiftRight();
		}
		return output;
	}
}
