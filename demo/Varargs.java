public class Varargs {
    static void fun(int i, Object... args) {
		System.out.println("i = " + i);
		for (Object o : args) {
			System.out.println("o = " + o);
		}
	}

	public static void main(String[] args) {
		fun(10, "aa", "bb", 20);
	}
}