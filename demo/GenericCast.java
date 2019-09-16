import java.util.*;

class FixedSizeStack<T> {
	private int index = 0;
	private Object[] storage;

	public FixedSizeStack(int size) {
		storage = new Object[size];
	}

	public void push(T item) {
		storage[index++] = item;
	}

	@SuppressWarnings("unchecked")
	public T pop() {
		return (T)storage[--index];
	}
}

public class Demo {
	public static void main (String[] args) {
		FixedSizeStack<String> strings = new FixedSizeStack<String>(10);
		for (String s : "A B C D E F G H I J".split(" ")) {
			strings.push(s);
		}
		for (int i = 0; i < 10; i++) {
			String s = strings.pop();
			System.out.print(s + " ");
		}
		System.out.println("");
	}
}