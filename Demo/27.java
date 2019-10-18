// ActiveObjectDemo.java

import java.util.concurrent.*;
import java.util.*;

public class Demo {
	ExecutorService es = Executors.newCachedThreadPool();
	static Random rand = new Random(26);

	private void pause(int factor) {
		try {
			TimeUnit.MILLISECONDS.sleep(100+rand.nextInt(factor));
		} catch (InterruptedException e) {
			System.out.println("sleep() interrupted");
		}
	}

	public Future<Integer> calculateInt(final int x, final int y) {
		return es.submit(new Callable<Integer>() {
			public Integer call() {
				System.out.println("starting " + x + " + " + y);
				pause(500);
				return x + y;
			}
		});
	}

	public Future<Float> calculateFloat(final float x, final float y) {
		return es.submit(new Callable<Float>() {
			public Float call() {
				System.out.println("starting " + x + " + " + y);
				pause(2000);
				return x + y;
			}
		});
	}

	public void shutdown() {
		es.shutdown();
	}

	public static void main(String[] args) throws Exception {
		Demo d1 = new Demo();
		List<Future<?>> results = new CopyOnWriteArrayList<Future<?>>();
		for (float f = 0.0f; f < 1.0f; f += 0.2f) {
			results.add(d1.calculateFloat(f, f));
		}
		for (int i = 0; i < 5; i++) {
			results.add(d1.calculateInt(i, i));
		}
		System.out.println("All asynch calls made");
		while (results.size() > 0) {
			for (Future<?> f : results) {
				if (f.isDone()) {
					try {
						System.out.println(f.get());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					results.remove(f);
				}
			}
			d1.shutdown();
		}
	}
}


/*
*/

