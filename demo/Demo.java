public class Demo {
	static <T> void f1(Holder<T> holder) {
		T t = holder.get();
		System.out.println(t.getClass().getSimpleName());
	}

	static void f2(Holder<?> holder) {
		f1(holder);    // 捕获转换
	}

    //@SuppressWarnings("unchecked")
	public static void main (String[] args) {
		Holder raw = new Holder<Integer>(1);
		//f1(raw);
		f2(raw);

		Holder rawBasic = new Holder();
		rawBasic.set(new Object());    // warning
		f2(rawBasic);                  // no warning

		Holder<?> wildcarded = new Holder<Double>(1.0);
		f2(wildcarded);
	}
}

/*
Integer
Object
Double
*/