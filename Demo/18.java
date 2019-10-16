import java.util.concurrent.*;
import java.io.*;
import java.util.*;

class Sender implements Runnable {
	private Random rand = new Random(26);
	private PipedWriter out = new PipedWriter();

	public PipedWriter getPipedWriter() {
		return out;
	}

	public void run() {
		try {
			while (true) {
				for (char c = 'A'; c <= 'z'; c++) {
					out.write(c);
					TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
				}
			}
		} catch (IOException e) {
			System.out.println(e + " Sender write exception");
		} catch(InterruptedException e) {
	        System.out.println(e + " Sender sleep interrupted");
	    }
	}
}

class Receiver implements Runnable {
	private PipedReader in;

	public Receiver(Sender sender) throws IOException {
		in = new PipedReader(sender.getPipedWriter());
	}

	public void run() {
		try {
			while (true) {
				System.out.println("Read: " + (char)in.read() + ", ");
			}
		} catch (IOException e) {
			System.out.println(e + " Receiver read exception");
		}
	}
}

public class Demo {
	public static void main(String[] args) throws Exception {
		Sender sender = new Sender();
		Receiver receiver = new Receiver(sender);
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(sender);
		exec.execute(receiver);
		TimeUnit.SECONDS.sleep(4);
		exec.shutdownNow();
	}
}


/*
*/

