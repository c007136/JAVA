// ListComparisons.java
// {Args: 1 10 10} 

import java.util.concurrent.*;
import java.util.*;

abstract class ListTest extends Tester<List<Integer>> {
	ListTest(String testId, int nReaders, int nWriters) {
		super(testId, nReaders, nWriters);
	}

	class Reader extends TestTask {
		long result = 0;

		void test() {
			for (int i = 0; i < testCycles; i++) {
				for (int index = 0; index < containerSize; index++) {
					result += testContainer.get(index);
				}
			}
		}

		void putResults() {
			readResult += result;
			readTime += duration;
		}
	}

	class Writer extends TestTask {
		void test() {
			for (int i = 0; i < testCycles; i++) {
				for (int index = 0; index < containerSize; index++) {
					testContainer.set(index, writeData[index]);
				}
			}
		}

		void putResults() {
			writeTime += duration;
		}
	}

	void startReadersAndWriters() {
		for (int i = 0; i < nReaders; i++) {
			exec.execute(new Reader());
		}
		for (int i = 0; i < nWriters; i++) {
			exec.execute(new Writer());
		}
	}
}

class SynchronizedArrayListTest extends ListTest {
	List<Integer> containerInitializer() {
		return Collections.synchronizedList(new ArrayList<Integer>(new CountingIntegerList(containerSize)));
	}

	SynchronizedArrayListTest(int nReaders, int nWriters) {
		super("SynchronizedArrayListTest", nReaders, nWriters);
	}
}

class CopyOnWriteArrayListTest extends ListTest {
	List<Integer> containerInitializer() {
		return new CopyOnWriteArrayList<Integer>(new CountingIntegerList(containerSize));
	}

	CopyOnWriteArrayListTest(int nReaders, int nWriters) {
		super("CopyOnWriteArrayListTest", nReaders, nWriters);
	}
}

public class Demo {
	public static void main(String[] args) throws Exception {
		Tester.initMain(args);
		new SynchronizedArrayListTest(10, 0);
		new SynchronizedArrayListTest(9, 1);
		new SynchronizedArrayListTest(5, 5);
		new CopyOnWriteArrayListTest(10, 0);
		new CopyOnWriteArrayListTest(9, 1);
		new CopyOnWriteArrayListTest(5, 5);
		Tester.exec.shutdown();
	}
}


/*
*/

