// ReaderWriterList.java

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;

class ReaderWriterList<T> {
	private ArrayList<T> lockedList;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

	public ReaderWriterList(int size, T initialValue) {
		lockedList = new ArrayList<T>(Collections.nCopies(size, initialValue));
	}

	public T set(int index, T element) {
		Lock wlock = lock.writeLock();
		wlock.lock();
		try {
			return lockedList.set(index, element);
		} finally {
			wlock.unlock();
		}
	}

	public T get(int index) {
		Lock rlock = lock.readLock();
		rlock.lock();
		try {
			if (lock.getReadLockCount() > 1) {
				System.out.println(lock.getReadLockCount());
			}
			return lockedList.get(index);
		} finally {
			rlock.unlock();
		}
	}
}

class ReaderWriterListTest {
	ExecutorService exec = Executors.newCachedThreadPool();
	private final static int SIZE = 100;
	static Random rand = new Random(26);

	private ReaderWriterList<Integer> list = new ReaderWriterList<Integer>(SIZE, 0);

	private class Writer implements Runnable {
		public void run() {
			try {
				for (int i = 0; i < 20; i++) {
					list.set(i, rand.nextInt());
					TimeUnit.MILLISECONDS.sleep(100);
				}
			} catch (InterruptedException e) {

			}
			System.out.println("Writer finished, shutting down");
			exec.shutdownNow();
			System.exit(1);
		}
	}

	private class Reader implements Runnable {
		public void run() {
			try {
				while (!Thread.interrupted()) {
					for (int i = 0; i < SIZE; i++) {
						list.get(i);
						TimeUnit.MILLISECONDS.sleep(1);
					}
				}
			} catch (InterruptedException e) {

			}
		}
	}

	public ReaderWriterListTest(int nReaders, int nWriters) {
		for (int i = 0; i < nReaders; i++) {
			exec.execute(new Reader());
		}
		for (int i = 0; i < nWriters; i++) {
			exec.execute(new Writer());
		}
	}
}

public class Demo {
	public static void main(String[] args) throws Exception {
		new ReaderWriterListTest(30, 1);
	}
}


/*
*/

