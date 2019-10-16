public class Demo {
	public synchronized void f1(int count) {
		if (count-- > 0) {
			System.out.println("f1() calling f2() with count " + count);
			f2(count);
		}
	}

	public synchronized void f2(int count) {
		if (count-- > 0) {
			System.out.println("f2() calling f1() with count " + count);
			f1(count);
		}
	}


	public static void main(String[] args) throws Exception {
		final Demo multiLock = new Demo();
		new Thread() {
			public void run() {
				multiLock.f1(10);
			}
		}.start();
	}
}


/*
*/

