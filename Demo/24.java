// MapComparisons.java
// {Args: 1 10 10} 

import java.util.concurrent.*;
import java.util.*;

abstract class MapTest extends Tester<Map<Integer, Integer>> {
	MapTest(String testId, int nReaders, int nWriters) {
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
					testContainer.put(index, writeData[index]);
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

class SynchronizedHashMapTest extends MapTest {
	Map<Integer, Integer> containerInitializer() {
		return Collections.synchronizedMap(
			new HashMap<Integer, Integer>(
				MapData.map(
					new CountingGenerator.Integer(), 
					new CountingGenerator.Integer(), 
					containerSize
				)
			)
		);
	}

	SynchronizedHashMapTest(int nReaders, int nWriters) {
		super("SynchronizedHashMapTest", nReaders, nWriters);
	}
}

class ConcurrentHashMapTest extends MapTest {
	Map<Integer, Integer> containerInitializer() {
		return new ConcurrentHashMap<Integer, Integer>(
			MapData.map(
				new CountingGenerator.Integer(), 
				new CountingGenerator.Integer(), 
				containerSize
			)
		);
	}

	ConcurrentHashMapTest(int nReaders, int nWriters) {
		super("ConcurrentHashMapTest", nReaders, nWriters);
	}
}

public class Demo {
	public static void main(String[] args) throws Exception {
		Tester.initMain(args);
		new SynchronizedHashMapTest(10, 0);
		new SynchronizedHashMapTest(9, 1);
		new SynchronizedHashMapTest(5, 5);
		new ConcurrentHashMapTest(10, 0);
		new ConcurrentHashMapTest(9, 1);
		new ConcurrentHashMapTest(5, 5);
		Tester.exec.shutdown();
	}
}


/*
*/

