import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;
import java.util.*;

// ReentrantLock整体开销会比synchronized小很多
class ExplicitPairManager1 extends PairManager {
	private Lock lock = new ReentrantLock();

	public void increment() {
		// p.incrementX();
		// p.incrementY();
		// store(getPair());

		lock.lock();
		try {
			p.incrementX();
			p.incrementY();
			store(getPair());
		} finally {
			lock.unlock();
		}
	}
}

class ExplicitPairManager2 extends PairManager {
	private Lock lock = new ReentrantLock();

	public void increment() {
		Pair temp;
		lock.lock();
		try {
			p.incrementX();
			p.incrementY();
			temp = getPair();
		} finally {
			lock.unlock();
		}
		store(temp);
	}
}


public class Demo {
	static void testApproaches(PairManager p1, PairManager p2) {
		ExecutorService es = Executors.newCachedThreadPool();

		PairManipulator pm1 = new PairManipulator(p1);
		PairManipulator pm2 = new PairManipulator(p2);

		PairChecker pc1 = new PairChecker(p1);
		PairChecker pc2 = new PairChecker(p2);

		es.execute(pm1);
		es.execute(pm2);
		es.execute(pc1);
		//es.execute(pc2);

		es.shutdown();

		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("sleep interrupted");
		}

		System.out.println("pm1: " + pm1 + "\npm2: " + pm2);
		System.exit(0);
	}


	public static void main(String[] args) {
		PairManager p1 = new ExplicitPairManager1();
		PairManager p2 = new ExplicitPairManager2();
		testApproaches(p1, p2);
	}
}


/*
*/
