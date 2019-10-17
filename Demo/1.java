import java.util.concurrent.*;
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

class PairManager1 extends PairManager {
	public synchronized void increment() {
		p.incrementX();
		p.incrementY();
		store(getPair());
	}
}

class PairManager2 extends PairManager {
	// 同步代码块会比同步整个方法运行得多一些
	public void increment() {
		Pair temp;
		synchronized (this) {
			p.incrementX();
			p.incrementY();
			temp = getPair();
		}
		store(temp);
	}
}

class PairManipulator implements Runnable {
	private PairManager pm;

	public PairManipulator(PairManager pm) {
		this.pm = pm;
	}

	public void run() {
		while (true) {
			pm.increment();
		}
	}

	public String toString() {
		return "Pair: " + pm.getPair() + " checkCounter = " + pm.checkCounter.get();
	}
}

// 跟踪运行测试的频度
class PairChecker implements Runnable {
	private PairManager pm;

	public PairChecker(PairManager pm) {
		this.pm = pm;
	}

	public void run() {
		while (true) {
			pm.checkCounter.incrementAndGet();
			pm.getPair().checkState();
		}
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
		PairManager p1 = new PairManager1();
		PairManager p2 = new PairManager2();
		testApproaches(p1, p2);
	}
}


/*
pm1: Pair: x: 200, y: 200 checkCounter = 56
pm2: Pair: x: 201, y: 201 checkCounter = 669459870
*/