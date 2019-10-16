import java.util.concurrent.*;
import java.util.concurrent.locks.*;

class BlockedMutex {
	private Lock lock = new ReentrantLock();

	public BlockedMutex() {
		lock.lock();
	}

	public void f() {
		try {
			lock.lockInterruptibly();
			System.out.println("lock acquired in f()");
		} catch (InterruptedException e) {
			System.out.println("Interrupted from lock acquisition in f()");
		}
	}
}

class Blocked implements Runnable {
	BlockedMutex blocked = new BlockedMutex();

	public void run() {
		System.out.println("Waiting for f() in BlockedMutex");
		blocked.f();
		System.out.println("Broken out of blocked call");
	}
}

public class Demo {
	public static void main(String[] args) throws Exception {
		Thread t = new Thread(new Blocked());
		t.start();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Issuing t.interrupt()");
		t.interrupt();
	}
}


/*
*/

