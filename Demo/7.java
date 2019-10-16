import java.util.concurrent.*;
import java.io.*;
import java.net.*;

class IOBlocked implements Runnable {
	private InputStream in;

	public IOBlocked(InputStream in) {
		this.in = in;
	}

	public void run() {
		try {
			System.out.println("Waiting for read():");
			in.read();
		} catch (IOException e) {
			if(Thread.currentThread().isInterrupted()) {
				System.out.println("Interrupted from blocked I/O");
			} else {
				throw new RuntimeException(e);
			}
		}
		System.out.println("Exiting IOBlocked.run()");
	}
}

public class Demo {
	public static void main(String[] args) throws Exception {
		ExecutorService es = Executors.newCachedThreadPool();
		ServerSocket ss = new ServerSocket(8080);
		InputStream in = new Socket("localhost", 8080).getInputStream();
		es.execute(new IOBlocked(in));
		es.execute(new IOBlocked(System.in));
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Shutting down all threads");
		es.shutdown();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Closing " + in.getClass().getName());
		in.close();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Closing " + System.in.getClass().getName());
		System.in.close();
	}
}


/*
*/

