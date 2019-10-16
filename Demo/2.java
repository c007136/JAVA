import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;
import java.util.*;

class Pair {
	private int x;
	private int y;

	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Pair() {
		this(0, 0);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void incrementX() {
		x++;
	}

	public void incrementY() {
		y++;
	}

	public String toString() {
		return "x: " + x + ", y: " + y;
	}

	public void checkState() {
		if (x != y) {
			throw new PairValuesNotEqualException();
		}
	}

	public class PairValuesNotEqualException extends RuntimeException {
		public PairValuesNotEqualException() {
			super("Pair values not equal: " + Pair.this);
		}
	}
}

abstract class PairManager {
	AtomicInteger checkCounter = new AtomicInteger(0);

	protected Pair p = new Pair();

	private List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());

	public synchronized Pair getPair() {
		return new Pair(p.getX(), p.getY());
	}

	protected void store(Pair p) {
		storage.add(p);

		try {
			TimeUnit.MILLISECONDS.sleep(50);
		} catch (InterruptedException e) {

		}
	}

	public abstract void increment();
}

class ExplicitPairManager1 extends PairManager {
	private Lock lock = new ReentrantLock();

	public synchronized void increment() {
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
		es.execute(pc2);

		//es.shutdown();

		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			System.out.println("pm1: " + pm1 + "\npm2: " + pm2);
			System.exit(0);
		}
	}


	public static void main(String[] args) {
		PairManager p1 = new ExplicitPairManager1();
		PairManager p2 = new ExplicitPairManager2();
		testApproaches(p1, p2);
	}
}


/*
*/
