class Cup {
    Cup() {
    	System.out.println("cup init");
    }

	Cup(int marker) {
		System.out.println("marker is " + marker);
	}
}

// static不能作用于局部变量，只能作用于域
class Cups {
	static Cup cup1 = new Cup(11);   // 这样初始化
	static Cup cup2;                 // 这样不初始化

	static {    // 静态初始化动作只进行一次
		cup1 = new Cup(1);
		cup2 = new Cup(2);
	}

	Cups() {
		System.out.println("Cups");
	}
}

public class ExplicitStatic {
	public static void main(String[] args) {
		System.out.println("-------");
		Cups cups = new Cups();
	}
}