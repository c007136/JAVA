import java.util.concurrent.*;

public class EvenChecker implements Runnable {
	private IntGenerator generator;

	private final int id;

	public EvenChecker(IntGenerator g, int ident) {
		generator = g;
		id = ident;
	}

	public void run() {
		while (!generator.isCanceled()) {
			int val = generator.next();
			// 加上这句话就感觉可以无限循环了
			//System.out.println("id is " + id + " val is " + val + " current thread is " + Thread.currentThread());
			if (val % 2 != 0) {
				System.out.println("id is " + id + " val is " + val + " not even current thread is " + Thread.currentThread());
				generator.cancel();
			}
		}
	}

	public static void test(IntGenerator g, int count) {
		System.out.println("Press Conctol-C to exit");
		ExecutorService es = Executors.newCachedThreadPool();
		for (int i = 0; i < count; i++) {
			es.execute(new EvenChecker(g, i));
		}
		es.shutdown();
	}

	public static void test(IntGenerator g) {
		test(g, 10);
	}
}