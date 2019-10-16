// {Args: 1100}

import java.util.concurrent.*;

class NeedsCleanup {
	private final int id;

	public NeedsCleanup(int id) {
		this.id = id;
		System.out.println("NeedsCleanup " + id);
	}

	public void cleanup() {
		System.out.println("Cleaning up " + id);
	}
}

class Blocked implements Runnable {
	private volatile double d = 0.0;

	public void run() {
		try {
			while (!Thread.interrupted()) {
				NeedsCleanup n1 = new NeedsCleanup(1);

				try {
					System.out.println("Sleeping");
					TimeUnit.SECONDS.sleep(1);

					NeedsCleanup n2 = new NeedsCleanup(2);

					try {
						System.out.println("Calculating");
						for (int i = 0; i < 2500000; i++) {
							d = d + (Math.PI + Math.E) / d;
						}
						System.out.println("Finished time-consuming operation");
					} finally {
						n2.cleanup();
					}
				} finally {
					n1.cleanup();
				}
				System.out.println("Exiting via while() test");
			}
		} catch (InterruptedException e ) {
			System.out.println("Exiting via InterruptedException");
		}
	}
}

public class Demo {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("usage: java InterruptingIdiom delay-in-mS");
			System.exit(1);
		}

		Thread t = new Thread(new Blocked());
		t.start();
		TimeUnit.MILLISECONDS.sleep(new Integer(args[0]));
		t.interrupt();
	}
}


/*
*/

