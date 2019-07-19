import static util.Print.*;

import util.*;



class D implements I {
	public void f() { System.out.println("D F"); }
	public void g() { System.out.println("D G"); }
}

public class Demo {
	public static void main(String[] args) {
		SubP subP = new SubP();
		subP.F();
		
		P p = new P();
		p.F();
	}
}
