import java.util.concurrent.*;
import java.util.*;

class Accessor implements Runnable {
	private final int id;

	public Accessor(int id) {
		this.id = id;
	}

	public void run() {
		boolean b = Thread.currentThread().isInterrupted();
		while(!b) {
			Demo.increment();
			System.out.println(this);
			Thread.yield();
		}
	}

	public String toString() {
		return "#" + id + ":" + Demo.get();
	}
}

public class Demo {
	private static ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
		private Random rand = new Random(26);
		protected synchronized Integer initialValue() {
			return rand.nextInt(100000);
		}
	};

	public static void increment() {
		value.set(value.get() + 1);
	}

	public static int get() {
		return value.get();
	}

	public static void main(String[] args) throws Exception {
		ExecutorService es = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			es.execute(new Accessor(i));
		}
		TimeUnit.SECONDS.sleep(3);  // Run for a while
		es.shutdown();              // All Accessors will quit
	}
}


/*
*/

